package cd.world.component

import arc.graphics.g2d.Draw
import cd.util.PointS
import cd.util.SAMConversation.lamdba2Cons
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import mindustry.Vars
import mindustry.game.Schematic.Stile
import mindustry.game.{Schematics, Team}
import mindustry.gen.Building
import mindustry.world.{Block, Tile}

import scala.collection.mutable.ArrayBuffer

trait MultiBlockComp extends CDBlockComp {
  ???
  var structure: Array[Map[PointS, Block]] = null
  
  def readStructure(schematic: String): Unit = {
    val a = new ArrayBuffer[Stile]
    Schematics.readBase64(schematic).tiles.each((p: Stile) => {a += p; () })
    val origin = a.find(_.block == this).get
    val (ox, oy) = (origin.x, origin.y)
    val repr = (a.map(st => PointS(st.x - ox, st.y - oy, st.rotation)) zip a.map(_.block)).toMap
    structure = PointS.processPosition(repr.keys.toArray, this.size % 2 == 1, repr.map(_._2.size % 2 == 1).toArray).map(_.zip(repr.values).toMap)
  }
  
  trait MultiBlockBuildingComp extends CDBuildingComp {
    var realPosition: Array[Map[PointS, Block]] = null
    
    var warmups: Int = 0
    
    override def init(tile: Tile, team: Team, shouldAdd: Boolean, rotation: Int): Building = {
      
      val a = super.init(tile, team, shouldAdd, rotation)
      
      if(structure != null) {
        realPosition = structure.map(_.map({
          p =>
            (new PointS(p._1.x + tileX(), p._1.y + tileY(), p._1.rot), p._2)
        }))
      } else {
        enabled(false)
      }
      a
    }
    
    def isSufficienct: Boolean = realPosition == null || realPosition.exists {
      _.forall(p => {
        Array(Option(Vars.world.build(p._1.x, p._1.y)).orNull).forall { a => a != null && a.tile().x == p._1.x && a.tile().y == p._1.y && (!p._2.rotate || a.rotation == p._1.rot) }
      })
    }
    
    override def draw(): Unit = {
      super.draw()
      if(isSufficienct) return;
      Draw.alpha(0.5f);
      realPosition(warmups).foreach(p => {
        Draw.rect(p._2.region, p._1.x.toFloat, p._1.y.toFloat, p._1.rot)
      }
      )
      Draw.alpha(1f);
    }
  }
  
}
