package cd.content;

import arc.graphics.*;
import arc.math.geom.*;
import cd.world.blocks.*;
import cd.world.blocks.multi.structure.*;
import cd.world.component.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;

import static mindustry.type.ItemStack.with;

public class CDBlocks{

    public static Block
    basicFreezer, basicDirectlyH2O2Crafter, basicElectrolyzer,
    pneuConduit, chlorineExtractor, airCompressor, basicClF3Crafter,
    basicCO2Laser, basicLaserRepeater, fluorineExtractor, basicChipCrafter,
    //multi-oil
    basicLiquidInPort, basicLiquidOutPort, basicItemOutPort,
    basicStructBlock, basicStructBlockLarge, oilDistillatorTower,
    //env
    ashWall, ashFloor, ashBoulder, deadSapling, enrichedSandFloor, enrichedSandWall, enrichedSandBoulder,
    graniteFloor, graniteWall, graniteBoulder,
    permafrostFloor, permafrostWall, vine;

    public static void load(){
        ashWall = new StaticWall("ash-wall");
        enrichedSandWall = new StaticWall("enriched-sand-wall");
        graniteWall = new StaticWall("granite-wall");
        permafrostWall = new StaticWall("permafrost-wall");

        ashFloor = new Floor("ash-floor",3);
        enrichedSandFloor = new Floor("enriched-sand-floor",3);
        graniteFloor = new Floor("granite-floor",3){
            {
                wall = graniteWall;
                decoration = graniteBoulder;
            }
        };
        permafrostFloor = new Floor("permafrost-floor");

        ashBoulder = new Prop("ash-boulder"){{
            ashFloor.asFloor().decoration = this;
        }};
        enrichedSandBoulder = new Prop("enriched-sand-boulder"){{
            enrichedSandFloor.asFloor().decoration = this;
        }};
        graniteBoulder = new Prop("granite-boulder"){{
            graniteFloor.asFloor().decoration = this;
        }};
        deadSapling = new StaticTree("dead-sapling");
        vine = new Prop("vine"){{
            variants = 2;
            alwaysReplace = false;
            instantDeconstruct = false;
        }};
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
                consumeItem(CDItems.ice, 1);
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
                outputLiquids = LiquidStack.with(Liquids.ozone, 4f / 60, Liquids.hydrogen, 6f / 60);
                liquidOutputDirections = new int[]{1, 3};
            }
        };
        pneuConduit = new ComponentBlock("pneu-conduit"){{
            size = 1;
            requirements(Category.distribution,
            with(Items.lead, 5, Items.graphite, 10));
            addComp(new PneuComponent());
        }};
        chlorineExtractor = new ComponentCrafter("chlorine-extractor"){{
            addComp(new PneuComponent(){{
                canConsumePressure = true;
                pressureConsume = 1f;
            }});
            requirements(Category.crafting, with(Items.copper, 30, Items.lead, 30,
            Items.silicon, 50, Items.metaglass, 50));
            outputLiquid = new LiquidStack(CDLiquids.chlorine, 5f / 60f);
            craftTime = 60f;
            size = 3;
            consumePower(0.1f);
        }};
        airCompressor = new ComponentCrafter("air-compressor"){{
            requirements(Category.crafting, with(Items.copper, 40, Items.graphite,
            35, Items.lead, 50, Items.silicon, 35));
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
            outputLiquid = new LiquidStack(CDLiquids.ClF3, 0.2f);
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consumeLiquids(LiquidStack.with(CDLiquids.chlorine, 0.1f, CDLiquids.fluorine, 0.3f));
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
            addComp(new LaserComponent(){{
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
            requirements(Category.crafting, with(Items.copper, 30, Items.lead, 60,
            Items.silicon, 25, Items.metaglass, 50));
            craftTime = 120f;
            size = 3;
            hasItems = true;
            outputLiquid = new LiquidStack(CDLiquids.fluorine, 0.1f);
            consumePower(0.2f);
            addComp(new LaserComponent(){{
                acceptLaserEnergy = true;
                consumeLaserEnergy = 0.125f;
            }});
        }};

        basicCO2Laser = new ComponentCrafter("basic-co2-laser"){{
            requirements(Category.crafting, with(CDItems.lanthanum, 25, Items.silicon, 15));
            craftTime = 90f;
            size = 1;
            hasItems = true;
            consume(new ConsumeItemFlammable());
            addComp(new LaserComponent(){{
                provideLaserEnergy = true;
                laserEnergyOutput = 0.25f;
                laserRange = 10;
                laserColor2 = Color.valueOf("80b3c4");
            }});
            rotate = true;
            drawArrow = true;
        }};

        basicChipCrafter = new ComponentCrafter("basic-chip-crafter"){{
            requirements(Category.crafting, with(Items.plastanium, 40,
            Items.titanium, 100, Items.silicon, 150, Items.thorium, 80));
            outputItem = new ItemStack(CDItems.basicChip, 2);
            craftTime = 120f;
            size = 3;
            hasItems = true;
            hasLiquids = true;
            consumeItems(with(Items.silicon, 5, Items.thorium, 6));
            consumeLiquid(Liquids.cryofluid, 12f / 60f);
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
            new LaserComponent(){{
                acceptLaserEnergy = true;
                consumeLaserEnergy = 0.5f;
            }});
        }};

        basicLiquidInPort = new MultiStructPort("basic-liquid-input-port"){{
            requirements(Category.distribution, with(Items.copper,
            40, Items.lead, 20, Items.silicon, 15, Items.metaglass, 10));
            isInputItem = false;
            size = 3;
        }};

        basicLiquidOutPort = new MultiStructPort("basic-liquid-output-port"){{
            requirements(Category.distribution, with(Items.copper,
            40, Items.lead, 20, Items.silicon, 15, Items.metaglass, 10));
            isInputLiquid = isInputItem = false;
            isOutputLiquid = true;
            size = 2;
        }};

        basicItemOutPort = new MultiStructPort("basic-item-output-port"){{
            requirements(Category.distribution, with(Items.copper, 40, Items.lead, 20, Items.silicon, 15));
            isInputLiquid = isInputItem = false;
            isOutputItem = true;
            size = 2;
        }};

        basicStructBlock = new Block("basic-structure-block"){{
            requirements(Category.distribution, with(Items.copper, 12, Items.lead, 15,
            Items.graphite, 5));
            update = true;
        }};

        basicStructBlockLarge = new Block("basic-structure-block-large"){{
            requirements(Category.distribution, with(Items.copper, 48, Items.lead, 60,
            Items.graphite, 20, Items.silicon, 5));
            update = true;
            size = 2;
        }};
        oilDistillatorTower = new ComponentCrafter("oil-distillator-tower"){{
            requirements(Category.crafting, with(Items.copper, 400, Items.lead, 400, Items.metaglass, 100,
            Items.titanium, 100, Items.silicon, 150, Items.graphite, 80));
            buildCostMultiplier = 0.1f;
            size = 5;
            addComp(new MainMultiComponent(){{
                /**  1 2 3 4 5 6 7 8 9 0 A B C D E
                 * 1 F F F F F F F F F F F F F F F
                 * 2 F F F F F F F F F F F F F F F
                 * 3 F F F F F F F F F F F F F F F
                 * 4 F F F L L B B S A A L L F F F
                 * 5 F F F L L B B S A A L L F F F
                 * 6 F F F Y Y O O O O O T T F F F
                 * 7 F F F Y Y O O O O O T T F F F
                 * 8 F F F S S O O C O O S S F F F
                 * 9 F F F Z Z O O O O O W W F F F
                 * 0 F F F Z Z O O O O O W W F F F
                 * A F F F L L S I I I S L L F F F
                 * B F F F L L S I I I S L L F F F
                 * C F F F F F F I I I F F F F F F
                 * D F F F F F F F F F F F F F F F
                 * E F F F F F F F F F F F F F F F
                 * */
                //Original Point is (8,8)
                dataOf(basicLiquidOutPort, 3, 1, basicLiquidOutPort, 3, -2, basicLiquidOutPort, -4, 1,
                basicLiquidOutPort, -4, -2,
                basicLiquidInPort, -1, -5, basicItemOutPort, 1, 3, basicItemOutPort, -2, 3,
                basicStructBlock, 0, 3, basicStructBlock, 0, 4,
                basicStructBlock, 3, 0, basicStructBlock, 4, 0, basicStructBlock, -3, 0, basicStructBlock, -4, 0,
                basicStructBlock, -2, -3, basicStructBlock, -2, -4, basicStructBlock, 2, -3, basicStructBlock, 2, -4,
                basicStructBlockLarge, 3, -4, basicStructBlockLarge, -4, -4, basicStructBlockLarge, 3, 3,
                basicStructBlockLarge, -4, 3);
                liquidDirectionOf(CDLiquids.petrol, new Point2(3, 1), CDLiquids.kerosene,
                new Point2(3, -2),
                CDLiquids.diesel, new Point2(-4, 1), CDLiquids.lubricatingOil, new Point2(-4, -2));
                itemDirectionOf(CDItems.bitumen, new Point2(1, 3), CDItems.wax, new Point2(-2, 3));
            }});

            consumeLiquid(Liquids.oil, 2f);//real 120/s!
            consumePower(2f);

            outputLiquids = LiquidStack.with(CDLiquids.petrol, 15f / 60f, CDLiquids.kerosene, 15f / 60f,
            CDLiquids.diesel, 15f / 60f, CDLiquids.lubricatingOil, 15f / 60f);

            outputItems = with(CDItems.bitumen, 2, CDItems.wax, 3);


        }};
    }
}

