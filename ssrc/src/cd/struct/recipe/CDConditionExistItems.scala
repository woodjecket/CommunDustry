package cd.struct.recipe

import arc.scene.Element
import arc.scene.ui.layout.Table
import cd.struct.CDCraft.CDCondition
import cd.util.SAMConversation.{lamdba2Floatp, lamdba2Prov}
import mindustry.`type`.ItemStack
import mindustry.gen.Building
import mindustry.ui.ItemDisplay
import mindustry.world.Block

class CDConditionExistItems(val items: Array[ItemStack]) extends CDCondition {
  override def sufficient(building: Building): Boolean = building.items.has(items)

  override def buildTable(build: Building, table: Table): Table = table

  override def init(block: Block): Unit = {
    items.foreach(stack => block.itemFilter(stack.item.id) = true)
  }

  override def genReaction(): Array[Element] =
    items.map(stack => {
      val a = new ItemDisplay(stack.item, stack.amount)
      a.scaleBy(0.5f)
      a
    } )
}
