package cd.struct.recipe

import arc.scene.ui.layout.Table
import cd.struct.recipe.CDCraft.{CDCondition, CDConsume, CDProduce, CDRecipePart}
import cd.ui.ElementArrow
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Floatp, lamdba2Prov}
import mindustry.ctype.UnlockableContent
import mindustry.gen.Building

case class Recipe(consume: CDConsume, produce: CDProduce,
                  condition: CDCondition, iconItem: UnlockableContent, craftTime: Float) extends
  Iterator[CDRecipePart] {
  val serialVersionUID: Long = 732473L
  private[this] var ordinal = -1


  override def hasNext: Boolean = ordinal > 2

  override def isTraversableAgain: Boolean = true

  override def next(): CDRecipePart = {
    ordinal += 1
    ordinal match {
      case 0 => consume
      case 1 => produce
      case 2 => condition
      case 3 => ordinal -= 3; consume
    }
  }

  def genReactionTable(): Table = new Table((t: Table) => {
    //Reactants' table
    t.add(consume.genReaction())

    //Conditions' table
    val conditionTable = new Table((ct: Table) => {
      ct.table((up: Table) => {
        up.add(condition.genReaction(): _*)
      }).row()
      ct.add(new ElementArrow(96)).row()
      ct.add(new Table()).row()
    })
    t.add(conditionTable)

    //Products' table
    t.add(produce.genReaction());()
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

    def sufficient(building: Building): Boolean = recipe.condition.sufficient(building) && recipe.consume.sufficient(building,efficiency) && recipe.produce.canProduce(building)

    @volatile var efficiency, progress: Float = 0f;
    @volatile var enabled: Boolean = true
  }
}
