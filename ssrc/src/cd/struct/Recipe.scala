package cd.struct

import arc.scene.ui.layout.Table
import cd.struct.CDCraft.{CDCondition, CDConsume, CDProduce, CDRecipePart}
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Floatp, lamdba2Prov}
import mindustry.ctype.UnlockableContent
import mindustry.gen.{Building, Icon}

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
      ct.table((arrow: Table) => {
        //TODO Automatic check
        val lineNum: Int = 7
        for (i <- 1 to lineNum ) {
          arrow.image(Icon.lineSmall)
        }
        arrow.image(Icon.rightSmall)
        ()
      }).row()
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

    def sufficient(building: Building): Boolean = recipe.condition.sufficient(building) && recipe.consume.sufficient(building) && recipe.produce.canProduce(building)

    @volatile var efficiency, progress: Float = 0f;
    @volatile var enabled: Boolean = true
  }
}
