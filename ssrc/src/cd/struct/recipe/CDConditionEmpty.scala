package cd.struct.recipe

import arc.scene.Element
import arc.scene.ui.layout.Table
import cd.struct.recipe.CDCraft.CDCondition
import cd.util.SAMConversation.{lamdba2Floatp, lamdba2Prov}
import mindustry.gen.Building
import mindustry.world.Block

object CDConditionEmpty extends CDCondition {
  override def sufficient(building: Building): Boolean = true

  override def buildTable(build: Building, table: Table): Table = table

  override def init(block: Block): Unit = {}

  override def genReaction(): Array[Element] = {Array[Element]()}
}
