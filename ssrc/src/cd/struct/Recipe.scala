package cd.struct

import arc.scene.ui.layout.Table
import cd.struct.CDCraft.{CDCondition, CDConsume, CDProduce}
import mindustry.ctype.UnlockableContent
import mindustry.gen.Building

case class Recipe(consume: CDConsume, produce: CDProduce, condition: CDCondition, iconItem: UnlockableContent, craftTime : Float) {
  def buildTable(build: Building, table: Table): Table = condition.buildTable(build, produce.buildTable(build, consume.buildTable(build, table)))


}

object Recipe {
  case class RecipeEntity(recipe: Recipe) {
    def craft(building: Building) = {
      recipe.consume.triggerOnce(building)
      recipe.produce.triggerOnce(building)
    }

    def triggerPerTick(building: Building) = {
      recipe.consume.triggerPerTick(building,efficiency)
      recipe.produce.triggerPerTick(building,efficiency)
      progress += 1 / recipe.craftTime
    }

    def sufficient(building: Building): Boolean = recipe.condition.sufficient(building) && recipe.consume.sufficient(building) && recipe.produce.canProduce(building)

    @volatile var efficiency, progress: Float = 0f;
    @volatile var enabled: Boolean = true
  }
}
