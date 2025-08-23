package cd.content;

import cd.world.block.*;
import cd.world.block.environment.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class CDBlocks{
    public static FiniteOre finiteCopper;
    public static Airport airport;

    public static void load(){
        finiteCopper = new FiniteOre(Items.copper);
        airport = new Airport("airport"){{
            requirements(Category.crafting,ItemStack.with());
        }};
    }

}
