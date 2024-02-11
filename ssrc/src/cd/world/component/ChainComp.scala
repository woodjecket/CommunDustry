package cd.world.component

import arc.Events
import cd.util.SAMConversation.{lamdba2Prov, lamdba2Runnable}
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import cd.world.component.ChainBuildComp.U
import mindustry.Vars
import mindustry.game.EventType.Trigger
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
  val chainedItems = new ItemModule
  var orientItems: ItemModule = null
  
  Events.run(Trigger.update, ()=>{
    download()
    parent = null
    recursiveMerge(this)
  })
  
  override def init(tile: Tile, team: Team, shouldAdd: Boolean, rotation: Int): Building = {
    super.init(tile, team, shouldAdd, rotation)
    orientItems = items
    this
  }
  
  private def recursiveMerge(start: ChainBuildComp): Unit = {
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
  
  /** Downloads resource from the shared*/
  private def download(): Unit = {
    val items1 = root.asInstanceOf[ChainBuildComp].chainedItems
    group.foreach(b => b.items = b.orientItems)
    val a = group.size
    Vars.content.items().toArray.foreach { item =>
      val sum = items1.get(item)
      val arrange = sum / a
      var mode = sum % a
      group.foreach(build => {
        build.items.set(item, arrange + (if(mode > 0) 1 else 0));
        mode -= 1
      })
      
    }
    items1.clear()
  }
  
  private def upload(): Unit = {
    val filtered = U.filter(node => node.root == root || node == this)
    val items1 = root.asInstanceOf[ChainBuildComp].chainedItems;
    filtered.foreach(build => {
      items1.add(build.items)
      build.items = items1
    })
  }
  
  private def distribute(): Unit = Vars.content.items().toArray.foreach { item =>
    val filtered = U.filter(node => node.root == root || node == this)
    val sum = filtered.map(_.items.get(item)).sum
    val a = amount
    val arrange = sum / a
    var mode = sum % a
    filtered.foreach(build => {
      build.items.set(item, arrange + (if(mode > 0) 1 else 0));
      mode -= 1
    })
  }
  
  override def updateTile(): Unit = {
    super.updateTile()
    tileChange {
      upload()
    }
  }
  
  def group: mutable.Set[ChainBuildComp] = U.filter(node => node.root == root || node == this)
}

object ChainBuildComp {
  val U: mutable.Set[ChainBuildComp] = mutable.HashSet()
}

trait DSLImpl {
  var parent: DSLImpl = null
  
  @tailrec
  final def root: DSLImpl = if(parent != null) parent.root else this
  
  def amount: Int = U.count(node => node.parent == this || node == this)
  
  def <--(another: DSLImpl): Unit = {
    if(root != another.root) {
      another.root.parent = this.root
    }
  }
}