package cd.content.test;

import arc.graphics.*;
import cd.content.*;
import cd.world.blocks.*;
import cd.world.blocks.multi.*;
import cd.world.component.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

import static mindustry.type.ItemStack.with;

public class TestBlocks{
    public static Block d = new MultiStructPort("2.2.3-PRE-ALPHA-MULTI-PORT"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        hasLiquids = true;
        hasItems = true;
        size = 1;
    }};
    public static ComponentCrafter a = new ComponentCrafter("2.2.3-PRE-ALPHA-MULTI-BLOCK"){{
        addComp(
        new MainMultiComponent(){{
            dataOf(d,-1,1,Blocks.copperWall,1,-1,Blocks.copperWall,1,1,Blocks.copperWall,-1,-1);
        }}
        );
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        craftEffect = CDFx.iceCraft;
        outputLiquid = LiquidStack.with(Liquids.water,6f)[0];
        craftTime = 60f;
        size = 1;
        hasPower = true;
        hasLiquids = false;
        drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
        ambientSound = Sounds.smelter;
        ambientSoundVolume = 0.07f;
        consumeItem(CDItems.ice);
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
            dataOf(d,2,2,Blocks.distributor,2,-2);
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
            dataOf(Blocks.router,2,2,d,2,-3);
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
    }};
}

