package cd.content;

import arc.graphics.*;
import cd.entities.component.*;
import cd.type.blocks.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;

import static mindustry.type.ItemStack.with;

public class CDBlocks{

    public static Block
    basicFreezer,
    basicDirectlyH2O2Crafter,
    basicElectrolyzer,
    pneuConduit,
    chlorineExtractor,
    airCompressor,
    basicClF3Crafter,
    basicCO2Laser,
    basicLaserRepeater,
    fluorineExtractor,
    basicChipCrafter;

    public void load(){
        basicFreezer = new GenericCrafter("basic-freezer"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
                craftEffect = CDFx.iceCraft;
                outputItem = new ItemStack(CDItems.ice, 1);
                craftTime = 60f;
                size = 4;
                hasPower = true;
                hasLiquids = false;
                drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
                ambientSound = Sounds.smelter;
                ambientSoundVolume = 0.07f;
                consumeLiquid(Liquids.water, 0.1f);
            }
        };
        basicDirectlyH2O2Crafter = new ComponentCrafter("basic-directly-h2o2-crafter"){
            {
                requirements(Category.crafting,
                with(Items.lead, 20, Items.silicon, 60, Items.titanium, 80, Items.graphite, 100));
                craftEffect = Fx.freezing;
                outputLiquid = new LiquidStack(CDLiquids.H2O2, 0.1f);
                craftTime = 120f;
                size = 4;
                hasPower = true;
                hasLiquids = false;
                drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("7666c6")));
                ambientSound = Sounds.smelter;
                ambientSoundVolume = 0.07f;
                consumeLiquids(new LiquidStack(Liquids.hydrogen, 0.05f),
                new LiquidStack(Liquids.ozone, 2f / 60f));
                consumeItem(CDItems.ice,1);
                addComp(new CatalyzerCrafterComponent(){{
                    catalyzerNecessity = true;
                    catalyzer = with(CDItems.platinum, 2);
                    catalyzerScale = new float[]{1f};
                    catalyzerChance = 0.0001f;
                }});
            }
        };
        basicElectrolyzer = new GenericCrafter("basic-electrolyzer"){
            {
                requirements(Category.crafting,
                with(Items.copper, 50, Items.lead, 40, Items.silicon, 130, Items.graphite, 80));
                size = 3;

                craftTime = 10f;
                rotate = true;
                rotateDraw = true;
                invertFlip = true;

                liquidCapacity = 50f;

                consumeLiquid(Liquids.water, 10f / 60f);
                consumePower(1f);

                drawer = new DrawMulti(
                new DrawLiquidTile(Liquids.water, 2f),
                new DrawBubbles(Color.valueOf("7693e3")){
                    {
                        sides = 10;
                        recurrence = 3f;
                        spread = 6;
                        radius = 1.5f;
                        amount = 20;
                    }
                },
                new DrawRegion(),
                new DrawLiquidOutputs());

                ambientSound = Sounds.electricHum;
                ambientSoundVolume = 0.08f;

                regionRotated1 = 3;
                outputLiquids = LiquidStack.with( Liquids.ozone, 4f / 60, Liquids.hydrogen, 6f / 60);
                liquidOutputDirections = new int[]{1, 3};
            }
        };
        pneuConduit = new ComponentBlock("pneu-conduit"){{
            size = 1;
            requirements(Category.distribution,
            with(Items.lead,5,Items.graphite,10));
            addComp(new PneuComponent());
        }};
        chlorineExtractor = new ComponentCrafter("chlorine-extractor"){{
            addComp(new PneuComponent(){{
                canConsumePressure = true;
                pressureConsume = 1f;
            }});
            requirements(Category.crafting, with( Items.copper, 30,Items.lead, 30,Items.silicon, 50,Items.metaglass,50));
            outputLiquid =  new LiquidStack(CDLiquids.chlorine,5f/60f);
            craftTime = 60f;
            size = 3;
            consumePower(0.1f);
        }};
        airCompressor = new ComponentCrafter("air-compressor"){{
            requirements(Category.crafting, with(Items.copper, 40, Items.graphite, 35, Items.lead, 50, Items.silicon, 35));
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consume(new ConsumeItemFlammable());
            addComp(new PneuComponent(){{
                canProvidePressure = true;
                maxOperatePressure = 9f;
            }});
        }};
        basicClF3Crafter = new ComponentCrafter("basic-clf3-crafter"){{
            requirements(Category.crafting, with(Items.copper, 150, Items.graphite, 135, Items.titanium, 60));
            outputLiquid = new LiquidStack(CDLiquids.ClF3,0.2f);
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consumeLiquids(LiquidStack.with(CDLiquids.chlorine,0.1f,CDLiquids.fluorine,0.3f));
            consumePower(0.4f);
            addComp(new PneuComponent(){{
                        canConsumePressure = true;
            }},
            new CatalyzerCrafterComponent(){{
                catalyzer = with(CDItems.platinum, 1, Items.surgeAlloy, 1);
                catalyzerScale = new float[]{2f, 3f};
                maxEfficiency = 5f;
                catalyzerNecessity = false;
            }});
        }};

        
        basicLaserRepeater = new ComponentBlock("basic-laser-repeater"){{
            requirements(Category.crafting, with(CDItems.lanthanum, 5));
            addComp(new LaserEnergyComponent(){{
                provideLaserEnergy = true;
                acceptLaserEnergy = true;
            }});
            size = 1;
            allowDiagonal = false;
            drawArrow = true;
            update = true;
            rotate = true;
            rotateDraw = true;
        }};
        fluorineExtractor = new ComponentCrafter("fluorine-extractor"){{
            requirements(Category.crafting, with( Items.copper, 30,Items.lead, 60,Items.silicon, 25,Items.metaglass,50));
            craftTime = 120f;
            size = 3;
            hasItems = true;
            outputLiquid = new LiquidStack(CDLiquids.fluorine,0.1f);
            consumePower(0.2f);
            addComp(new LaserEnergyComponent(){{
                acceptLaserEnergy = true;
                consumeLaserEnergy = 0.125f;
            }});
        }};

        basicCO2Laser = new ComponentCrafter("basic-co2-laser"){{
            requirements(Category.crafting, with(CDItems.lanthanum, 25, Items.silicon,15));
            craftTime = 90f;
            size = 1;
            hasItems = true;
            consume(new ConsumeItemFlammable());
            addComp(new LaserEnergyComponent(){{
                provideLaserEnergy = true;
                laserEnergyOutput = 0.25f;
                range = 10;
                laserColor2 = Color.valueOf("80b3c4");
            }});
            rotate = true;
            drawArrow = true;
        }};

        basicChipCrafter = new ComponentCrafter("basic-chip-crafter"){{
                requirements(Category.crafting, with(Items.plastanium, 40, Items.titanium, 100, Items.silicon, 150, Items.thorium, 80));
                outputItem = new ItemStack(CDItems.basicChip, 2);
                craftTime = 120f;
                size = 3;
                hasItems = true;
                hasLiquids = true;
                consumeItems(with(Items.silicon,5,Items.thorium,6));
                consumeLiquid(Liquids.cryofluid,12f/60f);
                consumePower(1f);
                addComp(
                new PneuComponent(){{
                            canConsumePressure = true;
                            pressureConsume = 7f;
                        }},
                new CatalyzerCrafterComponent(){{
                    catalyzer = with(CDItems.platinum, 1, Items.phaseFabric, 2);
                    catalyzerScale = new float[]{2f, 5f};
                    maxEfficiency = 7f;
                    catalyzerNecessity = false;
                }},
                new LaserEnergyComponent(){{
                    acceptLaserEnergy = true;
                    consumeLaserEnergy = 0.5f;
                }});
        }};
    }
}

