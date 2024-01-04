package cd.struct.meta

import arc.Core
import arc.graphics.g2d.TextureRegion
import arc.graphics.{Color, Pixmap}
import arc.util.serialization.{Json, JsonReader}
import cd.CDMod
import mindustry.Vars
import mindustry.`type`.Item
import mindustry.graphics.MultiPacker.PageType
import mindustry.graphics.{Drawf, MultiPacker}

import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.collection.mutable

case class Meta(name: String) {
  var classification: String = null
  var meltingPoint, boilingPoint = 0
  var density, hardness = 0
  var colors: Array[Color] = Array[Color](Color.white, Color.red, Color.white)
  
  
  val dust: Item = new Item(s"$name-dust", colors(0)) {
    {
      //cannot be mined
      hardness = Int.MaxValue
    }
    
    override def createIcons(packer: MultiPacker): Unit = Meta.genSprite(packer, Meta.dustBaseRegion, this, colors)
  }
  val ingot: Item = new Item(s"$name-ingot", colors(0)) {
    {
      //cannot be mined
      hardness = Int.MaxValue
    }
    
    override def createIcons(packer: MultiPacker): Unit = Meta.genSprite(packer, Meta.ingotBaseRegion, this, colors)
    
  }
}

object Meta {
  val meta: mutable.Map[String,Meta] = mutable.Map[String,Meta]()
  lazy val dustBaseRegion: TextureRegion = Core.atlas.find("commumdustry-meta-dust")
  lazy val ingotBaseRegion: TextureRegion = Core.atlas.find("commumdustry-meta-ingot")
  
  private def genSprite(packer: MultiPacker, region: TextureRegion, item: Item, colors: Array[Color]): Unit = {
    val baseRegion = Core.atlas.getPixmap(region)
    val out = new Pixmap(baseRegion.width, baseRegion.height)
    for(x <- 0 until baseRegion.width; y <- 0 until baseRegion.height) {
      val rawColor = baseRegion.get(x, y)
      val index = rawColor match {
        case 0xffffffff => 0
        case 0xdcc6c6ff | 0xdbc5c5ff => 1
        case 0x9d7f7fff | 0x9e8080ff => 2
        case _ => -1
      }
      out.setRaw(x, y, if(index == -1) baseRegion.get(x, y)
      else colors(index).rgba8888())
    }
    Drawf.checkBleed(out)
    packer.add(PageType.main, Vars.content.transformName(item.name), out)
  }
  
  def init(): Unit = {
    val parser = new Json()
    val cet = Vars.mods.getMod(classOf[CDMod])
    val metas = cet.root.child("metas.json")
    val metaDom = new JsonReader().parse(metas)
    val items = metaDom.get("items")
    for(i <- items) {
      val name = i.get("name")
      val metaItem = new Meta(name.asString())
      meta += name.asString -> metaItem
      metaItem.classification = i.get("classification").asString()
      metaItem.meltingPoint = i.get("meltingPoint").asInt()
      metaItem.boilingPoint = i.get("boilingPoint").asInt()
      metaItem.hardness = i.get("hardness").asInt()
      metaItem.density = i.get("density").asInt()
      metaItem.colors = i.get("colors").asStringArray() map Color.valueOf
    }
  }
}
