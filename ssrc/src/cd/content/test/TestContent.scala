package cd.content.test

import arc.Events
import arc.graphics.Color
import cd.content.{Applyable, CDItems, CDLiquids}
import cd.entities.CDBaseBuilding
import cd.struct.meta.Meta
import cd.struct.recipe.{CDConditionExistItems, CDConsumeItems, CDProduceItems}
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Prov}
import cd.world.blocks.TrashBlock
import cd.world.blocks.lot.{LoTCore, LoTInputPort, LoTOutputPort}
import cd.world.component._
import mindustry.`type`.{Category, ItemStack, LiquidStack}
import mindustry.content.{Fx, Items, Liquids}
import mindustry.game.EventType.UnitDestroyEvent
import mindustry.game.Team
import mindustry.gen.{Building, Sounds}
import mindustry.world.Block
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.consumers.ConsumeItemFlammable
import mindustry.world.draw.{DrawDefault, DrawFlame, DrawMulti}
import mindustry.world.meta.BuildVisibility

object TestContent extends Applyable {
  val a: Block = new GenericCrafter("aiko-a") with CatalyzerComp {
    
    {
      requirements(Category.crafting, ItemStack.`with`(Items.lead, 20.asInstanceOf[AnyRef], Items.silicon, 60.asInstanceOf[AnyRef], Items.titanium, 80.asInstanceOf[AnyRef], Items.graphite, 100.asInstanceOf[AnyRef]))
      craftEffect = Fx.freezing
      outputLiquid = new LiquidStack(CDLiquids.H2O2, 0.1f)
      craftTime = 120f
      size = 4
      hasPower = true
      hasLiquids = true
      drawer = new DrawMulti(new DrawDefault, new DrawFlame(Color.valueOf("7666c6")))
      ambientSound = Sounds.smelter
      ambientSoundVolume = 0.07f
      consumeLiquids(new LiquidStack(Liquids.hydrogen, 0.05f), new LiquidStack(Liquids.ozone, 2f / 60f))
      consumeItem(CDItems.ice, 1)
      
      catalyzerNecessity = true
      catalyzer = ItemStack.`with`(CDItems.platinum, 2.asInstanceOf[AnyRef])
      catalyzerScale = Array[Float](2f)
      catalyzerChance = 0.0001f
      
      buildType = () => {
        (new GenericCrafterBuild() with CatalyzerBuildingComp).asInstanceOf[Building]
      }
    }
  }
  
  val b: Block = new Block("aiko-conduit") with PneuComp {
    {
      size = 1
      requirements(Category.distribution, ItemStack.`with`(Items.lead, 5.asInstanceOf[AnyRef], Items.graphite, 10.asInstanceOf[AnyRef]))
      buildType = () => {
        new CDBaseBuilding() with PneuBuildingComp {}.asInstanceOf[Building]
      }
    }
  }
  
  val c: Block = new GenericCrafter("aiko-chlorine") with PneuComp {
    {
      requirements(Category.crafting, ItemStack.`with`(Items.copper, 30.asInstanceOf[AnyRef], Items.lead, 30.asInstanceOf[AnyRef], Items.silicon, 50.asInstanceOf[AnyRef], Items.metaglass, 50.asInstanceOf[AnyRef]))
      outputLiquid = new LiquidStack(CDLiquids.chlorine, 5f / 60f)
      craftTime = 60f
      size = 3
      consumePower(0.1f)
      
      canConsumePressure = true
      pressureConsume = 1f
      
      buildType = () => {
        new GenericCrafterBuild() with PneuBuildingComp with PneuCrafterComp {}.asInstanceOf[Building]
      }
    }
  }
  
  val d: Block = new GenericCrafter("aiko-compress") with PneuComp {
    {
      requirements(Category.crafting, ItemStack.`with`(Items.copper, 40.asInstanceOf[AnyRef], Items.graphite, 35.asInstanceOf[AnyRef], Items.lead, 50.asInstanceOf[AnyRef], Items.silicon, 35.asInstanceOf[AnyRef]))
      craftTime = 90f
      size = 2
      hasItems = true
      consume(new ConsumeItemFlammable)
      canProvidePressure = true
      maxOperatePressure = 9f
      buildType = () => {
        new GenericCrafterBuild() with PneuBuildingComp with PneuCrafterComp {}.asInstanceOf[Building]
      }
    }
  }
  
  val e: Block = new GenericCrafter("aiko-clf") with PneuComp with CatalyzerComp {
    {
      requirements(Category.crafting, ItemStack.`with`(Items.copper, 150.asInstanceOf[AnyRef], Items.graphite, 135.asInstanceOf[AnyRef], Items.titanium, 60.asInstanceOf[AnyRef]))
      outputLiquid = new LiquidStack(CDLiquids.ClF3, 0.2f)
      craftTime = 90f
      size = 2
      hasItems = true
      consumeLiquids(LiquidStack.`with`(CDLiquids.chlorine, 0.1f.asInstanceOf[AnyRef], CDLiquids.fluorine, 0.3f.asInstanceOf[AnyRef]): _*)
      consumePower(0.4f)
      
      canConsumePressure = true
      
      catalyzer = ItemStack.`with`(CDItems.platinum, 1.asInstanceOf[AnyRef], Items.surgeAlloy, 1.asInstanceOf[AnyRef])
      catalyzerScale = Array[Float](2f, 3f)
      maxEfficiency = 5f
      catalyzerNecessity = false
      
      buildType = () => {
        new GenericCrafterBuild() with PneuBuildingComp with PneuCrafterComp with CatalyzerBuildingComp {}.asInstanceOf[Building]
      }
    }
  }
  
  val f: Block = new Block("aiko-recipe-test-1") with CDBaseCrafter {
    {
      requirements(Category.crafting, ItemStack.`with`(Items.copper, 150.asInstanceOf[AnyRef], Items.graphite, 135.asInstanceOf[AnyRef], Items.titanium, 60.asInstanceOf[AnyRef]))
      addRecipe(consume = new CDConsumeItems(Array(new ItemStack(Items.copper, 1))),
        product = new CDProduceItems(Array(new ItemStack(Items.lead, 1))),
        condition = new CDConditionExistItems(Array(new ItemStack(Items.graphite, 1))),
        craftTime = 30f)
      
      buildType = () => {
        new CDBaseBuilding() with CDBaseCrafterBuild {}.asInstanceOf[Building]
      }
    }
  }
  
  /*  val h: Block = new Block("aiko-multi-test-1") with CDBaseCrafter with MultiBlockComp{
      {
        requirements(Category.crafting, ItemStack.`with`(Items.copper, 150.asInstanceOf[AnyRef]))
        
        buildType = () => {
          new CDBaseBuilding() with CDBaseCrafterBuild with MultiBlockBuildingComp {}.asInstanceOf[Building]
        }
        
        readStructure("bXNjaAF4nCWMOw7CMBBEJ7FDk4oLcIMtOA+iWOwFWdix5Y+inB4W8aaYmebBYjGwGyeB7dI6Vi/N1VB6yBuAU+SHxIb5drc4u1yKVNo5RopcX4KVQ6Unu57rgYvLKY3kR+v1IA7vTGnEHuhnpisWH7IXtVr8mTDDTB9Fx6wXRqP1Be8wKF8=")
      }
    }*/
  
  val h: Block = new TrashBlock("aiko-trash1", Map(
    Items.lead -> 20, Items.silicon -> 10, Meta.meta("lithium").ingot -> 10
  )) {
    {
      category = Category.crafting
      buildVisibility = BuildVisibility.shown
    }
  }
  
  val trashBody = new TrashBlock("trash-body", Map(
    CDItems.platinum -> 5,
    CDItems.lanthanum -> 4,
    CDItems.cerium -> 3
  )) {
    category = Category.crafting
    buildVisibility = BuildVisibility.shown
    Events.on(classOf[UnitDestroyEvent], (e: UnitDestroyEvent) => {
      val tile = e.unit.tileOn()
      if(tile.build == null) {tile.setBlock(this, Team.derelict)}
    })
  }
  
  val core: Block = new LoTCore("aiko-lot-core"){{
    category = Category.effect
    buildVisibility = BuildVisibility.shown
  }}
  
  val output: Block = new LoTOutputPort("aiko-lot-output") {
    {
      category = Category.effect
      buildVisibility = BuildVisibility.shown
    }
  }
  
  val input: Block = new LoTInputPort("aiko-lot-input") {
    {
      category = Category.effect
      buildVisibility = BuildVisibility.shown
    }
  }
  
  val proxyFactory: Block = new GenericCrafter("aiko-proxy") with ChainComp{
    {
      selectSize = 5
      requirements(Category.crafting, ItemStack.`with`(Items.copper, 40.asInstanceOf[AnyRef], Items.graphite, 35.asInstanceOf[AnyRef], Items.lead, 50.asInstanceOf[AnyRef], Items.silicon, 35.asInstanceOf[AnyRef]))
      craftTime = 90f
      size = 2
      hasItems = true
      consumeItem(Items.coal, 2)
      consumePower(1f)
      outputItem = new ItemStack(Items.copper,1)
      buildType = () => {
        (new GenericCrafterBuild with ChainBuildComp with ManualFetch) .asInstanceOf[Building]
      }
    }
  }
  
  val proxyFactory2: Block = new GenericCrafter("aiko-proxy2") with ChainComp {
    {
      selectSize = 7
      requirements(Category.crafting, ItemStack.`with`(Items.copper, 40.asInstanceOf[AnyRef], Items.graphite, 35.asInstanceOf[AnyRef], Items.lead, 50.asInstanceOf[AnyRef], Items.silicon, 35.asInstanceOf[AnyRef]))
      craftTime = 90f
      size = 3
      hasItems = true
      consumeItem(Items.lead, 2)
      consumePower(1f)
      outputItem = new ItemStack(Items.pyratite, 1)
      buildType = () => {
        (new GenericCrafterBuild with ChainBuildComp with ManualFetch).asInstanceOf[Building]
      }
    }
  }
}