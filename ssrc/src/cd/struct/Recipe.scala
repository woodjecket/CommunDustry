package cd.struct

import arc.scene.ui.layout.Table
import cd.struct.CDCraft.{CDCondition, CDConsume, CDProduce}
import mindustry.ctype.UnlockableContent
import mindustry.gen.Building

case class Recipe(consume: CDConsume, produce: CDProduce, condition: CDCondition, iconItem: UnlockableContent) {
  def buildTable(build: Building, table: Table): Table = condition.buildTable(build, produce.buildTable(build, consume.buildTable(build, table)))

}

object Recipe {
  case class RecipeEntity(recipe: Recipe) {
    @volatile var efficiency, progress: Float = 0f;
    @volatile var enabled: Boolean = false
  }
}
