package cd.world.component


import arc.struct.EnumSet
import cd.struct.CDCraft.{CDCondition, CDConsume, CDProduce}
import cd.struct.Recipe
import cd.struct.Recipe.RecipeEntity
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import mindustry.content.Items
import mindustry.gen.Sounds
import mindustry.world.meta.BlockFlag

import scala.collection.mutable.ArrayBuffer

trait CDBaseCrafter extends CDBlockComp {
  update = true
  solid = true
  hasItems = true
  ambientSound = Sounds.machine
  sync = true
  ambientSoundVolume = 0.03f
  flags = EnumSet.of(BlockFlag.factory)
  drawArrow = false

  val recipes: ArrayBuffer[Recipe] = new ArrayBuffer[Recipe]()

  def addRecipe(consume:CDConsume, product:CDProduce, condition: CDCondition, craftTime:Float):Unit =
    recipes += new Recipe(consume = consume, produce = product, condition = condition, iconItem = Items.copper, craftTime = craftTime)

  override def init(): Unit = {
    super.init()
    recipes.foreach(r=>{
      r.produce.init(this)
      r.consume.init(this)
      r.condition.init(this)
    })
  }

  trait CDBaseCrafterBuild extends CDBuildingComp {
    //Wonderful grammar that the case class is also a producer function
    val recipeEntity: ArrayBuffer[RecipeEntity] = recipes.map(RecipeEntity)
    @inline private[this] def applyToAllRecipeEntities[A](f: RecipeEntity => A): ArrayBuffer[A] = {
      recipeEntity.map(f)
    }
    override def updateTile(): Unit = {
      super.updateTile()
      applyToAllRecipeEntities { recipeEntity =>
        if (recipeEntity.sufficient(this)) {
          recipeEntity.triggerPerTick(this)
          if (recipeEntity.progress >= 1) {
            recipeEntity.craft(this)
            recipeEntity.progress %= 1
          }
        }
      }
    }

    override def shouldConsume(): Boolean =
      applyToAllRecipeEntities { _.recipe.produce.canProduce(this) }.reduce(_ || _)

  }
}
