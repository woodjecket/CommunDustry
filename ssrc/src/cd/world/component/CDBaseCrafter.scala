package cd.world.component


import arc.Core
import arc.scene.ui.Button
import arc.scene.ui.layout.Table
import arc.struct.EnumSet
import cd.struct.recipe.CDCraft.{CDCondition, CDConsume, CDProduce}
import cd.struct.recipe.Recipe.RecipeEntity
import cd.struct.recipe.Recipe
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Runnable}
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import mindustry.content.Items
import mindustry.gen.{Sounds, Tex}
import mindustry.ui.Styles
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
  
  configurable = true
  
  val recipes: ArrayBuffer[Recipe] = new ArrayBuffer[Recipe]()
  
  
  def addRecipe(consume: CDConsume, product: CDProduce, condition: CDCondition, craftTime: Float): Unit =
    recipes += new Recipe(consume = consume, produce = product, condition = condition, iconItem = Items.copper, craftTime = craftTime)
  
  override def init(): Unit = {
    super.init()
    recipes.map(_.map(_.init(this)))
  }
  
  trait CDBaseCrafterBuild extends CDBuildingComp {
    //Wonderful grammar that the case class is also a producer function
    private[this] val recipeEntity: ArrayBuffer[RecipeEntity] = recipes.map(RecipeEntity)
    
    override def buildConfiguration(table: Table): Unit = {
      
      table.table(Tex.buttonTrans, (t: Table) => {
        t.defaults.grow.marginTop(0).marginBottom(0).marginRight(5).marginRight(5)
        t.add(Core.bundle.get("select-recipe")).padLeft(5).padTop(5).padBottom(5)
          .row()
        t.pane((buttons: Table) => {
          recipeEntity.foreach(r => {
            buttons.button(
              (b: Button) => {
                b.add(r.recipe.genReactionTable())
              },
              Styles.underlineb, () => r.enabled = !r.enabled
            ).update((b: Button) => b.setChecked(r.enabled))
              .fillY.growX().left().margin(5).marginTop(8).marginBottom(8).pad(4)
              .row()
          })
        }).fill().maxHeight(280f);
      })
      
      table.row()
    }
    
    override def updateTile(): Unit = {
      super.updateTile()
      recipeEntity.foreach { recipeEntity =>
        if(recipeEntity.sufficient(this)) {
          recipeEntity.triggerPerTick(this)
          if(recipeEntity.progress >= 1) {
            recipeEntity.craft(this)
            recipeEntity.progress %= 1
          }
        }
      }
    }
    
    override def shouldConsume(): Boolean =
      recipeEntity.exists { _.recipe.produce.canProduce(this) }
    
  }
}
