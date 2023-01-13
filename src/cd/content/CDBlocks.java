package cd.content;

import arc.graphics.*;
import cd.entities.component.*;
import cd.type.blocks.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
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
    basicCl3FCrafter,
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
                size = 4;

                researchCostMultiplier = 1.2f;
                craftTime = 10f;
                rotate = true;
                invertFlip = true;

                liquidCapacity = 50f;

                consumeLiquid(Liquids.water, 10f / 60f);
                consumePower(1f);

                drawer = new DrawMulti(
                new DrawRegion("-bottom"),
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
                new DrawLiquidOutputs(),
                new DrawGlowRegion(){
                    {
                        alpha = 0.7f;
                        color = Color.valueOf("c4bdf3");
                        glowIntensity = 0.3f;
                        glowScale = 6f;
                    }
                });

                ambientSound = Sounds.electricHum;
                ambientSoundVolume = 0.08f;

                regionRotated1 = 3;
                outputLiquids = LiquidStack.with(Liquids.ozone, 4f / 60, Liquids.hydrogen, 6f / 60);
                liquidOutputDirections = new int[]{1, 3};
            }
        };
        pneuConduit = new ComponentBlock("conduit"){{
            requirements(Category.distribution,
            with(Items.copper, 50, Items.lead, 40, Items.silicon, 130, Items.graphite, 80));
            addComp(new PneuComponent());
            hasPressure = true;
        }};
        chlorineExtractor = new ComponentCrafter("crafter"){{
            addComp(new PneuComponent(){{
                canConsumePressure = true;
            }});
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            outputItem = new ItemStack(Items.graphite, 1);
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consumeItem(Items.coal, 2);
            hasPressure = true;
        }};
        airCompressor = new ComponentCrafter("compressor"){{
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            outputItem = new ItemStack(Items.graphite, 1);
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consumeItem(Items.coal, 2);
            addComp(new PneuComponent(){{
                canProvidePressure = true;
            }});
            hasPressure = true;
        }};
        basicCl3FCrafter = new ComponentCrafter("multi-crafter"){{
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            outputItem = new ItemStack(Items.graphite, 1);
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consumeItem(Items.coal, 2);
            addComp(new PneuComponent(){{
                        canConsumePressure = true;
            }},
            new CatalyzerCrafterComponent(){{
                catalyzer = with(CDItems.platinum, 1, Items.lead, 1);
                catalyzerScale = new float[]{2f, 3f};
                maxEfficiency = 5f;
                catalyzerNecessity = false;
            }});
            hasPressure = true;
        }};

        
        basicLaserRepeater = new ComponentBlock("dLR2"){{
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            addComp(new LaserEnergyComponent(){{
                provideLaserEnergy = true;
                acceptLaserEnergy = true;
            }});
            allowDiagonal = false;
            drawArrow = true;
            update = true;
            rotate = true;
            rotateDraw = true;
        }};
        fluorineExtractor = new ComponentCrafter("laser-crafter"){{
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            craftTime = 120f;
            size = 3;
            hasItems = true;
            consumeItem(Items.coal, 2);
            addComp(new LaserEnergyComponent(){{
                acceptLaserEnergy = true;
                consumeLaserEnergy = 0.125f;
            }});
        }};

        basicCO2Laser = new ComponentCrafter("laser-source"){{
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            craftTime = 90f;
            size = 3;
            hasItems = true;
            consumeItem(Items.coal, 2);
            addComp(new LaserEnergyComponent(){{
                provideLaserEnergy = true;
                laserEnergyOutput = 0.25f;
                range = 10;
                laserColor2 = Color.valueOf("80b3c4");
            }});
            rotate = true;
            drawArrow = true;
        }};

        basicChipCrafter = new ComponentCrafter("super-crafter"){{
                requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
                outputItem = new ItemStack(Items.pyratite, 2);
                craftTime = 120f;
                size = 5;
                hasItems = true;
                hasLiquids = true;
                consumeItems(with(Items.coal,9,Items.scrap,7,Items.sand,6));
                consumeLiquid(Liquids.oil,12f/60f);
                consumePower(1f);
                consumeCoolant(0.1f);
                addComp(
                new PneuComponent(){{
                            canConsumePressure = true;
                            pressureConsume = 7f;
                        }},
                new CatalyzerCrafterComponent(){{
                    catalyzer = with(CDItems.platinum, 1, Items.surgeAlloy, 2);
                    catalyzerScale = new float[]{2f, 5f};
                    maxEfficiency = 7f;
                    catalyzerNecessity = false;
                }},
                new LaserEnergyComponent(){{
                    acceptLaserEnergy = true;
                }});
        }};
    }
}

