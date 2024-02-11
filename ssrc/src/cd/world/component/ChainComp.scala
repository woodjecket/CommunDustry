package cd.world.component

import arc.Events
import arc.math.geom.Point2
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Runnable}
import cd.util.SLog
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import cd.world.component.ChainGroup.build2oldItems
import mindustry.Vars
import mindustry.game.EventType.{BlockBuildEndEvent, Trigger}
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.world.Tile
import mindustry.world.modules.ItemModule

import scala.collection.mutable

trait ChainComp extends CDBlockComp {

}

trait ChainBuildComp extends CDBuildingComp with ChainImpl {
  var group: ChainGroup = null
  
  override def init(tile: Tile, team: Team, shouldAdd: Boolean, rotation: Int): Building = {
    super.init(tile, team, shouldAdd, rotation)
    build2oldItems += (this.pos() -> this.items())
    this
  }
  
  private def chainUpdate(source: ChainBuildComp): Unit = {
    //Avoid stackoverflow
    if(hasUpdated) return
    hasUpdated = true
    
    source.chain.foreach(p => {
      val c = Vars.world.build(p).asInstanceOf[ChainBuildComp]
      c.chain += this.pos()
      this.chain += c.pos()
    }
    )
    
    proximity.toArray().foreach {
      case chainNext: ChainBuildComp => chainNext.chainUpdate(source)
      case _ => {}
    }
  }
  
  private def chainCancel(): Unit = {
    hasUpdated = false
    proximity.toArray().foreach {
      case chainNext: ChainBuildComp if chainNext.hasUpdated => chainNext.hasUpdated = false; chainCancel()
      case _ => {}
    }
  }
  
  
  override def updateTile(): Unit = {
    super.updateTile()
    tileChange {
      chainUpdate(this)
      chainCancel()
      group = ChainGroup(chain)
      //SLog.info(f"$this : $chain")
    }
  }
  
  
}

trait ChainImpl {
  this: ChainBuildComp =>
  protected var hasUpdated = false
  
  def chain: mutable.Set[Int] = mutable.HashSet(this.pos())
  
  
}

class ChainGroup private(val chainPos: mutable.Set[Int]){
  private val chainItems = new ItemModule
  SLog.info(f"ChainGroup created:$chain")
  Events.run(Trigger.update, update _)
  Events.on(classOf[BlockBuildEndEvent], (e: BlockBuildEndEvent) => {
    if(e.breaking) {
      e.tile.build match {
        case chain: ChainBuildComp => chain.group.redistribute()
        case _ =>
      }
    }
  })
  
  def chain:mutable.Set[ChainBuildComp] = chainPos.map(Vars.world.build(_).asInstanceOf[ChainBuildComp])
  
  
  def update(): Unit = {
    chain.foreach(build => {
      if(build.items != chainItems) {
        chainItems.add(build.items)
        build.items().clear()
        build.items(chainItems)
      }
    })
  }
  
  private def redistribute(): Unit = {
    val length = chain.size
    chain.foreach { b => b.chain.clear(); b.chain += b.pos() }
    Vars.content.items().toArray().foreach(item => {
      val amount = chainItems.get(item)
      val arrange = amount / length
      var mode = amount % length
      chain.foreach { b => build2oldItems(b.pos()).add(item, if(mode > 1) arrange + 1 else arrange); mode -= 1 }
    })
    chain.foreach { b => b.items = build2oldItems(b.pos()) }
  }
  
}

object ChainGroup {
  val pool: mutable.HashMap[Int, ChainGroup] = mutable.HashMap()
  val build2oldItems: mutable.HashMap[Int, ItemModule] = mutable.HashMap()
  
  def apply(chain: mutable.Set[Int]): ChainGroup = {
    SLog.info(f"Fetched Group :${chain.map(Point2.unpack)}")
    pool.getOrElseUpdate(contentHash(chain), new ChainGroup(chain))
  }
  
  def contentHash(seq: Traversable[_]): Int = seq.foldRight(0)(_.hashCode() + _)
}