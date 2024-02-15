package cd.struct.recipe

import arc.scene.ui.layout.Table
import cd.struct.recipe.CDCraft.CDProduce
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Floatp, lamdba2Prov}
import mindustry.`type`.LiquidStack
import mindustry.gen.Building
import mindustry.ui.LiquidDisplay
import mindustry.world.Block

class CDProduceLiquids(val liquids: Array[LiquidStack]) extends CDProduce {
  /** Only trigger once in a craft process */
  override def triggerOnce(build: Building): Unit = {}
  
  /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
  override def triggerPerTick(build: Building, efficiency: Float): Unit = {
    for(stack <- liquids) {
      build.liquids.add(stack.liquid, stack.amount * build.delta() * efficiency)
    }
  }
  
  /** Whether the build can produce the things this produce */
  override def canProduce(building: Building): Boolean = liquids.forall { stack => building.block.liquidCapacity - building.liquids.get(stack.liquid) >= stack.amount }
  
  override def buildTable(build: Building, table: Table): Table =
    table.add("Not now").getTable
  
  override def init(block: Block): Unit = {}
  
  override def genReaction(): Table = {
    new Table((ta: Table) => {
      liquids.foreach(stack => ta.add(new LiquidDisplay(stack.liquid, stack.amount * 60, true)))
    })
  }
}
