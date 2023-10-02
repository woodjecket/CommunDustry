package cd.content.test

import arc.graphics.Color
import cd.content.{Applyable, CDItems, CDLiquids}
import cd.util.SAMConversation.lamdba2Prov
import cd.util.Utils.{iwith, lwith}
import cd.world.component.{CatalyzerComp, PneuComp}
import mindustry.`type`.{Category, LiquidStack}
import mindustry.content.{Fx, Items, Liquids}
import mindustry.gen.{Building, Sounds}
import mindustry.world.Block
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.consumers.ConsumeItemFlammable
import mindustry.world.draw.{DrawDefault, DrawFlame, DrawMulti}

object TestContent extends Applyable {
  val a: Block = new GenericCrafter("aiko-a") with CatalyzerComp {
    {
      requirements(Category.crafting, iwith(Items.lead, 20.asInstanceOf[AnyRef], Items.silicon, 60.asInstanceOf[AnyRef], Items.titanium, 80.asInstanceOf[AnyRef], Items.graphite, 100.asInstanceOf[AnyRef]))
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
      catalyzer = iwith(CDItems.platinum, 2.asInstanceOf[AnyRef])
      catalyzerScale = Array[Float](2f)
      catalyzerChance = 0.0001f

      buildType = () => {
        (new GenericCrafterBuild() with CatalyzerBuildingComp).asInstanceOf[Building]}
    }
  }

  val b:Block = new Block("aiko-conduit") with PneuComp {{
    size = 1
    requirements(Category.distribution, iwith(Items.lead, 5, Items.graphite, 10))
    buildType = () => {
      (new Building() with PneuBuildingComp {}).asInstanceOf[Building]
    }
  }}

  val c:Block = new GenericCrafter("aiko-chlorine") with PneuComp{{
    requirements(Category.crafting, iwith(Items.copper, 30, Items.lead, 30, Items.silicon, 50, Items.metaglass, 50))
    outputLiquid = new LiquidStack(CDLiquids.chlorine, 5f / 60f)
    craftTime = 60f
    size = 3
    consumePower(0.1f)

    canConsumePressure = true
    pressureConsume = 1f

    buildType = () => {
      (new GenericCrafterBuild() with PneuBuildingComp with PneuCrafterComp {}).asInstanceOf[Building]
    }
  }
  }

  val d:Block = new GenericCrafter("aiko-compress") with PneuComp {{
    requirements(Category.crafting, iwith(Items.copper, 40, Items.graphite, 35, Items.lead, 50, Items.silicon, 35))
    craftTime = 90f
    size = 2
    hasItems = true
    consume(new ConsumeItemFlammable)
    canProvidePressure = true
    maxOperatePressure = 9f
    buildType = () => {
      (new GenericCrafterBuild() with PneuBuildingComp with PneuCrafterComp {}).asInstanceOf[Building]
    }
  }}

  val e:Block = new GenericCrafter("aiko-clf") with PneuComp with CatalyzerComp {{
    requirements(Category.crafting, iwith(Items.copper, 150, Items.graphite, 135, Items.titanium, 60))
    outputLiquid = new LiquidStack(CDLiquids.ClF3, 0.2f)
    craftTime = 90f
    size = 2
    hasItems = true
    consumeLiquids(lwith(CDLiquids.chlorine, 0.1f, CDLiquids.fluorine, 0.3f):_*)
    consumePower(0.4f)

    canConsumePressure = true

    catalyzer = iwith(CDItems.platinum, 1, Items.surgeAlloy, 1)
    catalyzerScale = Array[Float](2f, 3f)
    maxEfficiency = 5f
    catalyzerNecessity = false

    buildType = () => {
      (new GenericCrafterBuild() with PneuBuildingComp with PneuCrafterComp with CatalyzerBuildingComp {}).asInstanceOf[Building]
    }
  }}

}