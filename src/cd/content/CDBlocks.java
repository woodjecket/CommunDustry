package cd.content;

import cd.world.block.environment.*;
import mindustry.content.*;
import mindustry.world.*;

public class CDBlocks{
    public static FiniteOre finiteCopper;

    public static void load(){
        finiteCopper = new FiniteOre(Items.copper);
    }
}
