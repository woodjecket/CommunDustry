package cd.struct.recipe

import arc.scene.ui.layout.Table
import cd.struct.CDCraft.CDCondition
import mindustry.`type`.ItemStack
import mindustry.gen.Building
import mindustry.world.Block

class CDConditionExistItems(val items:Array[ItemStack]) extends CDCondition{
  override def sufficient(building: Building): Boolean = building.items.has(items)

  override def buildTable(build: Building, table: Table): Table = table

  override def init(block: Block): Unit = {
    items.foreach(stack=>block.itemFilter(stack.item.id) = true)
  }
}
