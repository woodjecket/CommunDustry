package cd.content.test;

import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import cd.content.*;
import cd.type.*;
import cd.world.blocks.*;
import cd.world.blocks.multi.*;
import cd.world.component.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

import static mindustry.type.ItemStack.with;

@SuppressWarnings("unused")
public class TestContent{
    public static Seq<MetaDust> g = new Seq<>();
    /*
    * Chinese：thanatus是这个版本的代号，直接打版本号太长了所以在测试内容 前面加了这个名字
    * */

    public static Block portIn = new MultiStructPort("thanatus-port-input"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        size = 1;
    }};

    public static Block pressureIn = new MultiStructPort("thanatus-port-pressure-input"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isInputItem = isInputLiquid = false;
        isInputPressure = true;
        size = 1;
    }};

    public static Block laserIn = new MultiStructPort("thanatus-port-laser-input"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isInputItem = isInputLiquid = false;
        isInputLaser = true;
        size = 1;
    }};

    public static Block portOut = new MultiStructPort("thanatus-port-output"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isOutputLiquid = isOutputItem = true;
        isInputItem = isInputLiquid = false;
        size = 1;
    }};

    public static Block portOutLarge = new MultiStructPort("thanatus-port-output-large"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isOutputLiquid = isOutputItem = true;
        isInputItem = isInputLiquid = false;
        size = 2;
    }};

    public static Block portInLarge = new MultiStructPort("thanatus-port-input-large"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isOutputLiquid = isOutputItem = false;
        isInputItem = isInputLiquid = true;
        size = 3;
    }};

    public static ComponentCrafter massiveCrafter = new ComponentCrafter("thanatus-massive-crafter"){{
        addComp(
        new MainMultiComponent(){{
            /*
             * 0 0 0 0 0 k k k 0 0
             * 0 0 0 0 0 k k k 0 0
             * 0 0 | | | k k k 0 0
             * 0 0 l l l l - k k k
             * 0 0 l l l l - k k k
             * j j l C l l - k k k
             * j j l l l l 0 0 0 0
             * 0 0 0 j j 0 0 0 0 0
             * 0 0 0 j j 0 0 0 0 0
             * 0 0 0 0 0 0 0 0 0 0 */
            dataOf(portInLarge, 4, 0, portInLarge, 2, 3, portOutLarge, 0, -3, portOutLarge, -3, -1,
            Blocks.titaniumConveyor, 3, 0, Blocks.conveyor, 3, 1, Blocks.armoredConveyor, 3, 2,
            new RotatedBlock(Blocks.diode, 1), -1, 3, new RotatedBlock(Blocks.plastaniumConveyor, 1),
            0, 3, new RotatedBlock(CDBlocks.basicCO2Laser, 1), 1, 3);

            liquidDirectionOf(Liquids.water, new Point2(0, -3), Liquids.slag, new Point2(-3, -1));
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputLiquids = LiquidStack.with(Liquids.water, 6f / 60f, Liquids.slag, 6f / 60f);
        craftTime = 60f;
        size = 4;
        hasPower = true;
        hasLiquids = true;
        drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
        ambientSound = Sounds.smelter;
        ambientSoundVolume = 0.07f;
        consumeLiquids(LiquidStack.with(Liquids.cryofluid, 6f / 60f, CDLiquids.ClF3, 7f / 60f))
        ;
        rotate = true;
    }};

    public static ComponentCrafter smallCrafter = new ComponentCrafter("thanatus-small-crafter"){{
        addComp(
        new MainMultiComponent(){{
            dataOf(portIn, -1, 1, Blocks.copperWall, 1, -1, Blocks.copperWall, 1, 1, Blocks.copperWall, -1, -1);
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputLiquid = LiquidStack.with(Liquids.water, 6f/60f)[0];
        craftTime = 60f;
        size = 1;
        hasPower = true;
        hasLiquids = false;
        drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
        ambientSound = Sounds.smelter;
        ambientSoundVolume = 0.07f;
        consumeItem(CDItems.ice);
        rotate = true;
    }};
    public static ComponentCrafter normalCrafter = new ComponentCrafter("thanatus-normal-crafter"){{
        addComp(
        new MainMultiComponent(){{
            /*00000000
             * 00006000
             * 00110000
             * 00110000
             * 00008000
             * 00000000
             * 00000000
             * 00000000*/
            //left lower?
            dataOf(portOut, 2, 2, Blocks.copperWallLarge, 2, -2);
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputItem = new ItemStack(CDItems.ice, 1);
        craftTime = 60f;
        size = 2;
        hasPower = true;
        hasLiquids = false;
        drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
        ambientSound = Sounds.smelter;
        ambientSoundVolume = 0.07f;
        consumeLiquid(Liquids.water, 0.1f);
        rotate = true;
    }};
    public static ComponentCrafter largeCrafter = new ComponentCrafter("thanatus-large-crafter"){{
        addComp(
        new MainMultiComponent(){{
            /* 00000600
             * 00111000
             * 00111000
             * 00111000
             * 00000000
             * 00000800
             * 00000000
             * 00000000*/
            //left lower?
            dataOf(Blocks.router, 2, 2, portIn, 2, -3);
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputItem = new ItemStack(CDItems.ice, 1);
        craftTime = 60f;
        size = 3;
        hasPower = true;
        hasLiquids = false;
        drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
        ambientSound = Sounds.smelter;
        ambientSoundVolume = 0.07f;
        consumeLiquid(Liquids.water, 0.1f);
        rotate = true;
    }};
    public static ComponentCrafter hugeCrafter = new ComponentCrafter("thanatus-huge-crafter"){{
        addComp(
        new MainMultiComponent(){{
            dataOf(portIn, -1, 1, portOut, 1, -1, portOut, 1, 1, Blocks.copperWall, -1, -1);
            liquidDirectionOf(Liquids.water, new Point2(1, -1), CDLiquids.H2O2, new Point2(1, 1));
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputLiquids = LiquidStack.with(Liquids.water, 6f / 60f, CDLiquids.H2O2, 6f / 60f);
        craftTime = 60f;
        size = 1;
        hasPower = true;
        hasLiquids = false;
        drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
        ambientSound = Sounds.smelter;
        ambientSoundVolume = 0.07f;
        consumeLiquids(LiquidStack.with(Liquids.cryofluid, 6f / 60f, CDLiquids.ClF3, 7f / 60f))
        ;
        rotate = true;
    }};

    public static Block superCrafter = new ComponentCrafter("thanatus-super-crafter"){{
        requirements(Category.crafting, with(Items.plastanium, 40, Items.titanium, 100, Items.silicon, 150, Items.thorium, 80));
        outputItem = new ItemStack(CDItems.basicChip, 2);
        craftTime = 120f;
        size = 1;
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
        }},
        new MainMultiComponent(){{
            dataOf(portIn, -1, 1, portOut, 1, -1, laserIn, 1, 1, pressureIn, -1, -1,
            Blocks.duo,0,1,Blocks.arc,0,-1,Blocks.hail,1,0,Blocks.scorch,-1,0);
        }});
    }};

    public static void load(){
        Seq<Item> fore = Vars.content.items();

        for(Item in : fore.copy()){
            if(in == Items.pyratite){
                g.add(new MetaDust(Items.pyratite, "ffaa5f", "D4A383", "d37f47"));
            }else{
                g.add(new MetaDust(in));
            }
        }
    }


}

