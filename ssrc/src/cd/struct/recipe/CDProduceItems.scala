package cd.struct.recipe

import arc.scene.ui.layout.Table
import cd.struct.CDCraft.CDProduce
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Floatp, lamdba2Prov}
import mindustry.`type`.ItemStack
import mindustry.gen.Building
import mindustry.ui.ItemDisplay
import mindustry.world.Block

import scala.collection.convert.WrapAsJava.asJavaIterable

class CDProduceItems(val items: Array[ItemStack]) extends CDProduce {
  /** Only trigger once in a craft process */
  override def triggerOnce(build: Building): Unit =
    build.items.add(items.toIterable)

  /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
  override def triggerPerTick(build: Building, efficiency: Float): Unit = {
  }

  /** Whether the build can produce the things this produce */
  override def canProduce(building: Building): Boolean = items.forall { stack => building.block.itemCapacity - building.items.get(stack.item) >= stack.amount }

  override def buildTable(build: Building, table: Table): Table =
    table.add("Not now").getTable

  override def init(block: Block): Unit = {}

  override def genReaction(): Table = {
    new Table((ta: Table) => {
      items.foreach (stack => ta.add(new ItemDisplay (stack.item, stack.amount)) )
    })
  }
}
