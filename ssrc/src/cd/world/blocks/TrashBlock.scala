package cd.world.blocks

import arc.Events
import arc.math.Rand
import cd.util.SAMConversation.lamdba2Cons
import cd.world.blocks.TrashBlock.rand
import mindustry.`type`.Item
import mindustry.game.EventType.BlockBuildEndEvent
import mindustry.gen.Building
import mindustry.world.blocks.environment.Prop

import scala.collection.mutable.ArrayBuffer

class TrashBlock(name: String, val treasure: Map[Item, Int]) extends Prop(name) {
  private val monitorList: ArrayBuffer[TrashBuilding] = ArrayBuffer.empty[TrashBuilding]
  Events.on(classOf[BlockBuildEndEvent], (e: BlockBuildEndEvent) => {
    if(e.breaking && monitorList.contains(e.tile.build)) {
      treasure.map { p => (p._1, rand.random(0, p._2)) }
        .foreach(s => {
          val coreBuild = e.team.core()
          coreBuild.items.add(s._1, {
            Math.min(s._2, coreBuild.block.itemCapacity - coreBuild.items.get(s._1))
          })
        })
    }
  })
  destructible = true
  
  class TrashBuilding() extends Building() {
    monitorList += this
  }
}

object TrashBlock {
  val rand: Rand = new Rand()
}
