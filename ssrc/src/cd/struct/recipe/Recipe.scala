package cd.struct

import arc.scene.ui.layout.Table
import cd.struct.CDCraft.{CDCondition, CDConsume, CDProduce, CDRecipePart}
import cd.ui.ElementArrow
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Floatp, lamdba2Prov}
import mindustry.ctype.UnlockableContent
import mindustry.gen.{Building, Icon}

case class Recipe(consume: CDConsume, produce: CDProduce,
                  condition: CDCondition, iconItem: UnlockableContent, craftTime: Float) {
  val serialVersionUID: Long = 732473L

  def genReactionTable(): Table = new Table((t: Table) => {
    //Reactants' table
    t.add(consume.genReaction()).pad(4f)

    //Conditions' table
    val conditionTable = new Table((ct: Table) => {
      ct.table((up: Table) => {
        up.add(condition.genReaction(): _*)
      }).row()
      ct.table((arrow: Table) => {
        arrow.add(new ElementArrow(96))
      }).fill().row()
      ct.add(new Table()).row()
    })
    t.add(conditionTable).fill()

    //Products' table
    t.add(produce.genReaction()).pad(4f);()
  }
  )
}

object Recipe {
  case class RecipeEntity(recipe: Recipe) {
    val serialVersionUID: Long = 732473368489L

    def craft(building: Building): Unit = {
      recipe.consume.triggerOnce(building)
      recipe.produce.triggerOnce(building)
    }

    def triggerPerTick(building: Building): Unit = {
      recipe.consume.triggerPerTick(building, efficiency)
      recipe.produce.triggerPerTick(building, efficiency)
      progress += 1 / recipe.craftTime
    }

    def sufficient(building: Building): Boolean = recipe.condition.sufficient(building) && recipe.consume.sufficient(building) && recipe.produce.canProduce(building)

    @volatile var efficiency, progress: Float = 0f;
    @volatile var enabled: Boolean = true
  }
}
