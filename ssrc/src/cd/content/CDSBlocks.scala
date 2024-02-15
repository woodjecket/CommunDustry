package cd.content

import cd.entities.LiquidFiltered
import cd.struct.recipe._
import cd.util.SAMConversation.lamdba2Prov
import cd.world.component.CDBaseCrafter
import cd.world.component.ChainBuildComp.Chain.rand
import mindustry.`type`._
import mindustry.content.{Fx, Items, Liquids}
import mindustry.gen.{Building, Sounds}
import mindustry.world.Block
import mindustry.world.blocks.defense.Wall
import mindustry.world.blocks.liquid.{Conduit, LiquidRouter}
import mindustry.world.blocks.power.{Battery, ConsumeGenerator}
import mindustry.world.blocks.production.Pump
import mindustry.world.blocks.storage.Unloader
import mindustry.world.consumers.ConsumeLiquids
import mindustry.world.meta.BlockGroup

import scala.language.implicitConversions

object CDSBlocks extends Applyable {
  implicit private def toLS(old: (Liquid, Float)): LiquidStack = new LiquidStack(old._1, old._2)
  
  implicit private def toIS(old: (Item, Int)): ItemStack = new ItemStack(old._1, old._2)
  
  val smallBoiler: Block = new CDBaseCrafter("small-boiler") {
    {
      addRecipe(
        consume = new CDConsumeLiquids(Array((Liquids.water, 0.1f))),
        condition = CDConditionEmpty,
        product = new CDProduceLiquids(Array((CDSLiquids.steam, 0.1f))),
        craftTime = 60f
      )
      size = 2
      health = 90
      requirements(Category.power, ItemStack.`with`(Items.copper, 40.asInstanceOf[AnyRef], CDSItems.iron, 35.asInstanceOf[AnyRef],
        Items.lead, 50.asInstanceOf[AnyRef]))
      buildType = () => {
        new CDBaseCrafterBuild() {}.asInstanceOf[Building]
      }
    }
  }
  val copperConduit: Block = new Conduit("copper-conduit") {
    {
      requirements(Category.liquid, ItemStack.`with`(Items.lead, 2.asInstanceOf[AnyRef], Items.copper, 1.asInstanceOf[AnyRef]))
      health = 45
    }
  }
  val steamDistribution: Block = new LiquidRouter("steam-distribution") {
    {
      requirements(Category.liquid, ItemStack.`with`(Items.lead, 2.asInstanceOf[AnyRef], Items.copper, 1.asInstanceOf[AnyRef]))
      liquidCapacity = 20f
      underBullets = true
      solid = false
      liquidFilter(Liquids.water.id) = true
      liquidFilter(CDSLiquids.steam.id) = true
      buildType = () => {
        new LiquidRouterBuild with LiquidFiltered {
          override def updateTile(): Unit = {
            if(liquids.currentAmount > 0.01f) {
              dumpLiquid(liquids.current)
              liquids.remove(liquids.current, rand.range(0.05f / 60f))
            }
          }
        }.asInstanceOf[Building]
      }
    }
  }
  val pressurePump: Block = new Pump("pressure-pump") {
    {
      requirements(Category.liquid, ItemStack.`with`(Items.lead, 2.asInstanceOf[AnyRef], Items.copper, 1.asInstanceOf[AnyRef]))
      pumpAmount = 24f / 60f / 4f
      liquidCapacity = 160f
      size = 2
    }
  }
  val highPressureTank: Block = new LiquidRouter("high-pressure-tank") {
    {
      requirements(Category.liquid, ItemStack.`with`(Items.lead, 2.asInstanceOf[AnyRef], Items.copper, 1.asInstanceOf[AnyRef]))
      size = 3
      solid = true
      liquidCapacity = 1800f
      health = 500
      liquidFilter(CDSLiquids.steam.id) = true
      buildType = () => {
        new LiquidRouterBuild with LiquidFiltered {}.asInstanceOf[Building]
      }
    }
  }
  
  val steamForge: Block = new CDBaseCrafter("steam-forge") {
    {
    
    }
  }
  val steelSmelter: Block = new CDBaseCrafter("steel-smelter") {
    {
      addRecipe(
        consume = new CDConsumeItems(Array((CDSItems.iron, 1), (Items.coal, 2))),
        condition = CDConditionEmpty,
        product = new CDProduceItems(Array((CDSItems.steel, 1))),
        craftTime = 60f
      )
      size = 2
      health = 180
      requirements(Category.crafting, ItemStack.`with`(Items.copper, 40.asInstanceOf[AnyRef], CDSItems.iron, 35.asInstanceOf[AnyRef],
        Items.lead, 50.asInstanceOf[AnyRef]))
      buildType = () => {
        new CDBaseCrafterBuild() {}.asInstanceOf[Building]
      }
    }
  }
  
  val steelWall: Block = new Wall("steel-wall") {
    {
      requirements(Category.defense, ItemStack.`with`(CDSItems.steel, 4.asInstanceOf[AnyRef]))
      health = 640
    }
  }
  
  val steelWallLarge: Block = new Wall("steel-wall-large") {
    {
      requirements(Category.defense, ItemStack.`with`(CDSItems.steel, 16.asInstanceOf[AnyRef]))
      health = 640 * 4
      size = 2
    }
  }
  
  val mechanicUnloader: Block = new Unloader("mechanic-unloader") {
    {
      requirements(Category.defense, ItemStack.`with`(CDSItems.steel, 16.asInstanceOf[AnyRef]))
      health = 540
      size = 2
      speed = 4f / 60f
      group = BlockGroup.transportation
    }
  }
  //TODO make generator rcp-ize
  val steamTurbine:Block = new ConsumeGenerator("steam-turbine"){{
    requirements(Category.defense, ItemStack.`with`(CDSItems.steel, 16.asInstanceOf[AnyRef]))
    consume(new ConsumeLiquids(Array((CDSLiquids.steam, 0.2f))))
    powerProduction = 2f
    hasLiquids = true
    ambientSound = Sounds.smelter
    ambientSoundVolume = 0.03f
    generateEffect = Fx.generatespark
  }}
  //TODO region conveyor-like-ize
  val powerPipe:Block = new Battery("power-pipe"){{
    requirements(Category.defense, ItemStack.`with`(CDSItems.steel, 16.asInstanceOf[AnyRef]))
    consumePowerBuffered(400f)
    baseExplosiveness = 1f
  }}
}
