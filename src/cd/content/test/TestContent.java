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

public class TestContent{
    public static Seq<MetaDust> g = new Seq<>();
    public static Block d = new MultiStructPort("2.2.3-PRE-ALPHA-MULTI-PORT"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        size = 1;
    }};
    public static Block h = new MultiStructPort("2.2.3-PRE-ALPHA-MULTI-OUT-PORT"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isOutputLiquid = isOutputItem = true;
        isInputItem = isInputLiquid = false;
        size = 1;
    }};

    public static Block j = new MultiStructPort("2.2.3-PRE-ALPHA-MULTI-MULTI-OUT-PORT"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isOutputLiquid = isOutputItem = true;
        isInputItem = isInputLiquid = false;
        size = 2;
    }};

    public static Block k = new MultiStructPort("2.2.3-PRE-ALPHA-MULTI-MULTI-IN-PORT"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        isOutputLiquid = isOutputItem = false;
        isInputItem = isInputLiquid = true;
        size = 3;
    }};

    public static ComponentCrafter l = new ComponentCrafter("2.2.3-PRE-ALPHA-MULTI-FIVE-BLOCK"){{
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
            dataOf(k, 4, 0, k, 2, 3, j, 0, -3, j, -3, -1,
            Blocks.titaniumConveyor,3,0,Blocks.conveyor,3,1,Blocks.armoredConveyor,3,2,
            new RotatedBlock(Blocks.diode,1),-1,3,new RotatedBlock(Blocks.plastaniumConveyor,1),0,3,new RotatedBlock(CDBlocks.basicCO2Laser,1),1,3);

            directionOf(Liquids.water, new Point2(0, -3), CDLiquids.H2O2, new Point2(-3, -1));
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputLiquids = LiquidStack.with(Liquids.water, 6f / 60f, CDLiquids.H2O2, 6f / 60f);
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

    public static ComponentCrafter a = new ComponentCrafter("2.2.3-PRE-ALPHA-MULTI-BLOCK"){{
        addComp(
        new MainMultiComponent(){{
            dataOf(d, -1, 1, Blocks.copperWall, 1, -1, Blocks.copperWall, 1, 1, Blocks.copperWall, -1, -1);
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputLiquid = LiquidStack.with(Liquids.water, 6f)[0];
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
    public static ComponentCrafter b = new ComponentCrafter("2.2.3-PRE-ALPHA-MULTI-MULTI-BLOCK"){{
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
            dataOf(h, 2, 2, Blocks.distributor, 2, -2);
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
    public static ComponentCrafter c = new ComponentCrafter("2.2.3-PRE-ALPHA-MULTI-MULTI3-BLOCK"){{
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
            dataOf(Blocks.router, 2, 2, d, 2, -3);
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
    public static ComponentCrafter i = new ComponentCrafter("2.2.3-PRE-ALPHA-MULTI-FOUR-BLOCK"){{
        addComp(
        new MainMultiComponent(){{
            dataOf(d, -1, 1, h, 1, -1, h, 1, 1, Blocks.copperWall, -1, -1);
            directionOf(Liquids.water, new Point2(1, -1), CDLiquids.H2O2, new Point2(1, 1));
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

    public static void load(){
        Seq<Item> fore = Vars.content.items();

        for(Item i : fore.copy()){
            if(i == Items.pyratite){
                g.add(new MetaDust(Items.pyratite, "ffaa5f", "D4A383", "d37f47"));
            }else{
                g.add(new MetaDust(i));
            }
        }
    }


}

