package cd.world.component

import arc.Events
import arc.func.Cons2
import arc.graphics.Color
import arc.graphics.g2d.{Draw, Lines}
import arc.math.Rand
import arc.math.geom.Point2
import arc.struct.IntSeq
import arc.util.io.Writes
import cd.struct.TileChange
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Cons2, lamdba2Prov}
import cd.util.SLog
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import cd.world.component.ChainBuildComp.Chain.rand
import cd.world.component.ChainBuildComp.{Chain, U}
import mindustry.Vars
import mindustry.game.EventType.TilePreChangeEvent
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.graphics.{Drawf, Pal}
import mindustry.world.modules.ItemModule
import mindustry.world.{Block, Tile}

import scala.annotation.tailrec
import scala.collection.convert.wrapAsScala.iterableAsScalaIterable
import scala.collection.mutable

trait ChainComp extends CDBlockComp {
  var selectSize: Int = -1
  var maxNode: Int = 8
  conductivePower = true
  
  override def init(): Unit = {
    super.init()
    if(selectSize > 0) {
      configurable = true
      config(classOf[Integer], (entity: ManualFetch, value: Integer) => {
        val other = Vars.world.build(value).asInstanceOf[ManualFetch]
        val contain = entity.tmp.contains(other)
        SLog.info(f"Enter config: contain:$contain, other:$other, valid: ${linkValid(entity, other)}")
        if(contain) {
          //remove
          other.parent = null
          //check
          entity.tmp.foreach(b => if(!b.linkTo(entity)) b.parent = null)
        } else if(linkValid(entity, other)) {
          if(entity.parent == null) { //add
            entity <-- other.asInstanceOf[entity.type]
          }
        }
        
      })
      config(classOf[Array[Point2]], (tile: ManualFetch, value: Array[Point2]) => {
        val old = new IntSeq(tile.power.links)
        //clear old
        for(i <- 0 until old.size) {
          configurations.get(classOf[Integer]).asInstanceOf[Cons2[ManualFetch, Integer]].get(tile, old.get(i))
        }
        //set new
        for(p <- value) {
          configurations.get(classOf[Integer]).asInstanceOf[Cons2[ManualFetch, Integer]].get(tile, Point2.pack(p.x + tile.tileX, p.y + tile.tileY))
        }
        
      })
    }
  }
  
  def linkValid(tile: ManualFetch, link: ManualFetch): Boolean = {
    if(tile == link || link == null || link.block != tile.block || tile.team != link.team) return false;
    val deltaX = ((tile.x - link.x) / 8f).abs
    val deltaY = ((tile.y - link.y) / 8f).abs
    SLog.info(f"deltaX:$deltaX deltaY:$deltaY")
    deltaX < selectSize && deltaY < selectSize && tile.tmp.size - 1 < maxNode && link.tmp.size - 1 < maxNode
    && (tile.tmp.exists(_.proximity.contains(link)) || tile.proximity.contains(link))
  }
}

trait ChainBuildComp extends CDBuildingComp with DSLImpl with BaseFetcher {
  U += this
  var orientItems: ItemModule = null
  private val tmp = mutable.HashSet(this)
  
  override def init(tile: Tile, team: Team, shouldAdd: Boolean, rotation: Int): Building = {
    super.init(tile, team, shouldAdd, rotation)
    orientItems = items
    this
  }
  
  
  override def onRemoved(): Unit = {
    super.onRemoved()
    U -= this
  }
  
  override def updateTile(): Unit = {
    super.updateTile()
    fetch()
    fetchChild_m(tmp)
    chain.upload()
  }
  
  override def draw(): Unit = {
    super.draw()
    Draw.color(Pal.accent)
    Lines.stroke(1.0F)
    Drawf.line(Chain(tmp).color, this.x - 3f, this.y - 3f, root.x + 3f, root.y + 3f)
    chain.draw()
  }
  
  private def chain = Chain(tmp)
  
  override def writeBase(write: Writes): Unit = {
    chain.download()
    super.writeBase(write)
  }
}

object ChainBuildComp {
  private[component] val U: mutable.Set[ChainBuildComp] = mutable.HashSet()
  
  
  private[component] class Chain private(val group: mutable.Set[ChainBuildComp]) extends TileChange {
    private val chainItems = new ItemModule
    
    val color = new Color(rand.random(Integer.MAX_VALUE - 1))
    
    private val eventToUnit = (_: TilePreChangeEvent) => {download(); U.foreach(_.parent = null) }
    Events.on(classOf[TilePreChangeEvent], eventToUnit)
    val draw: () => Unit = () => {
      group.foreach(b => {
        Draw.color(color)
        Lines.stroke(1.0F)
        Lines.square(b.x, b.y, (b.block.size * 8).toFloat / 2.0F + 1.0F)
        Draw.reset()
      })
    }
    
    def download(): Unit = {
      if(group.forall(_.items == chainItems)) {
        val length = group.size
        Vars.content.items().toArray().foreach(item => {
          val amount = chainItems.get(item)
          val arrange = amount / length
          var mode = amount % length
          group.foreach { b => b.orientItems.add(item, if(mode > 0) arrange + 1 else arrange); mode -= 1 }
        })
        chainItems.clear()
        group.foreach { b => b.items = b.orientItems }
      }
    }
    
    def upload(): Unit = {
      for(elem <- group) {
        if(elem.items != chainItems) {
          chainItems.add(elem.items)
          elem.items.clear()
          elem.items = chainItems
        }
      }
    }
    
    def dispose(): Unit = {
      Events.remove(classOf[TilePreChangeEvent], eventToUnit)
    }
    
  }
  
  private[component] object Chain {
    val rand = new Rand
    private val pool = mutable.HashMap[mutable.Set[ChainBuildComp], Chain]()
    
    def apply(group: mutable.Set[ChainBuildComp]): Chain = {
      pool.retain((s, _) => s.exists(U.contains))
      pool.getOrElseUpdate(group, new Chain(group))
    }
  }
}

trait DSLImpl {
  private[component] var parent: this.type = null
  
  @tailrec
  private[component] final def root: this.type = if(parent != null) parent.root else this
  
  
  private[component] def <--(another: this.type): Unit = {
    if(root != another.root) {
      another.root.parent = this.root
    }
  }
}


trait BaseFetcher extends CDBuildingComp with DSLImpl {
  def fetch(): Unit
  
  def fetchChild_m(set: mutable.HashSet[ChainBuildComp]): Unit
}

trait AutoFetcher extends BaseFetcher {
  this: ChainBuildComp =>
  
  @inline private def flyweight: Block with ChainComp = block.asInstanceOf[Block with ChainComp]
  
  @inline private def selectSize: Int = flyweight.selectSize
  
  
  override def fetch(): Unit = {
    recursiveMerge(this, 1)
  }
  
  override def fetchChild_m(set: mutable.HashSet[ChainBuildComp]): Unit = {
    set.clear()
    U.foreach(node => if(node.root == root) set += node)
  }
  
  private def recursiveMerge(start: AutoFetcher, depth: Int): Unit = {
    this <-- start.asInstanceOf[this.type]
    proximity.toArray().foreach {
      case chainBuild: AutoFetcher if start.root != chainBuild.root && chainBuild.block == start.block
      => chainBuild.recursiveMerge(start, depth + 1)
      case _ =>
    }
    
  }
}

trait ManualFetch extends BaseFetcher {
  this: ChainBuildComp =>
  private var lookedUp = false
  
  def linkTo(source: ManualFetch): Boolean = {
    SLog.info(f"$source -> $this")
    lookedUp = true
    var result = false
    proximity.toArray().foreach {
      case `source` =>
        lookedUp = false
        return true
      case fetch: ManualFetch if fetch.root == this.root && !lookedUp => result |= linkTo(source)
      case _ =>
    }
    lookedUp = false
    result
  }
  
  val tmp: mutable.HashSet[ManualFetch] = mutable.HashSet(this)
  
  @inline private def flyweight: Block with ChainComp = block.asInstanceOf[Block with ChainComp]
  
  @inline private def selectSize: Int = flyweight.selectSize
  
  
  def fetch(): Unit = {}
  
  def fetchChild_m(set: mutable.HashSet[ChainBuildComp]): Unit = {
    set.clear()
    tmp.clear()
    U.foreach(node => {
      if(node.root == root) {
        tmp += node.asInstanceOf[ManualFetch]
        set += node
      }
    })
    
  }
  
  override def drawConfigure(): Unit = {
    super.drawConfigure()
    //Draw links
    for(elem <- tmp) {
      val ex = elem.x
      val ey = elem.y
      Draw.color(Pal.accent)
      Lines.stroke(1.0F)
      Lines.square(ex, ey, (elem.block.size * 8).toFloat / 2.0F - 1.0F)
      Draw.reset()
      //Draw available unlinked
      for(ee <- elem.proximity) {
        ee match {
          case comp: ManualFetch if !tmp.contains(comp) && tmp.size < selectSize =>
            Draw.color(Pal.gray)
            Lines.stroke(1.0F)
            Lines.square(ee.x, ee.y, (ee.block.size * 8).toFloat / 2.0F - 1.0F)
            Draw.reset()
          case _ =>
        }
      }
    }
  }
  
  override def onConfigureBuildTapped(other: Building): Boolean = {
    other match {
      case fetch1: ManualFetch =>
        SLog.info(f"tmp:$tmp, other:$other")
        if(tmp.exists(b => b.proximity.contains(other) || b == other) && flyweight.linkValid(this, fetch1)) {
          configure(other.pos)
          return false
        }
        fetch1.parent != null
      case _ => false
    }
  }
}
