package cd.world.component

import arc.Events
import cd.struct.TileChange
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Prov}
import cd.util.SLog
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import cd.world.component.ChainBuildComp.U
import mindustry.Vars
import mindustry.game.EventType.TilePreChangeEvent
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.world.Tile
import mindustry.world.modules.ItemModule

import scala.annotation.tailrec
import scala.collection.mutable

trait ChainComp extends CDBlockComp {

}

trait ChainBuildComp extends CDBuildingComp with DSLImpl {
  U += this
  var orientItems: ItemModule = null
  
  override def init(tile: Tile, team: Team, shouldAdd: Boolean, rotation: Int): Building = {
    super.init(tile, team, shouldAdd, rotation)
    orientItems = items
    this
  }
  
  private def recursiveMerge(start: ChainBuildComp): Unit = {
    SLog.info(f"$this from ${start.toString}")
    this <-- start
    proximity.toArray().foreach {
      case chainBuild: ChainBuildComp if (chainBuild.root == null || start.root != chainBuild.root)
      => chainBuild.recursiveMerge(start)
      case _ =>
    }
    
  }
  
  override def onRemoved(): Unit = {
    super.onRemoved()
    isRemoved = true
    U -= this
  }
  
  override def updateTile(): Unit = {
    super.updateTile()
    tileChange {
      recursiveMerge(this)
      Chain(U.filter(node => node.root == root || node.root == this)).update()
      SLog.info(U.filter(node => node.root == root || node.root == this).toString())
    }
  }
}

object ChainBuildComp {
  val U: mutable.Set[ChainBuildComp] = mutable.HashSet()
}

trait DSLImpl {
  var parent: DSLImpl = null
  
  @tailrec
  final def root: DSLImpl = if(parent != null) parent.root else this
  
  
  def <--(another: DSLImpl): Unit = {
    if(root != another.root) {
      another.root.parent = this.root
    }
  }
}

class Chain private(val group: mutable.Set[ChainBuildComp]) extends TileChange {
  private val chainItems = new ItemModule
  Events.on(classOf[TilePreChangeEvent], (_:TilePreChangeEvent)=>download())
  
  private def download(): Unit = {
    SLog.info("$group downloaded")
    val length = group.size
    Vars.content.items().toArray().foreach(item => {
      val amount = chainItems.get(item)
      val arrange = amount / length
      var mode = amount % length
      group.foreach { b => b.orientItems.add(item, if(mode > 1) arrange + 1 else arrange); mode -= 1 }
    })
    chainItems.clear()
    group.foreach { b => b.items = b.orientItems }
    U.foreach(_.parent = null)
  }
  
  def update(): Unit = {
    for(elem <- group) {
      if(elem.items != chainItems) {
        chainItems.add(elem.items)
        elem.items.clear()
        elem.items = chainItems
      }
    }
  }
  
}

object Chain {
  private val pool = mutable.HashMap[mutable.Set[ChainBuildComp], Chain]()
  
  def apply(group: mutable.Set[ChainBuildComp]): Chain = {
    val returnValue = pool.getOrElseUpdate(group, { new Chain(group) })
    returnValue
  }
}
