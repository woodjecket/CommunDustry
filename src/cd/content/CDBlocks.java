package cd.content;

import arc.graphics.*;
import cd.world.block.*;
import cd.world.block.environment.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.power.Battery;
import mindustry.world.blocks.power.SolarGenerator;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

import static mindustry.type.ItemStack.with;

public class CDBlocks{
    public static FiniteOre finiteCopper;

    public static Airport airport;

    public static Block ashWall, ashFloor, ashBoulder, deadSapling, enrichedSandFloor, enrichedSandWall, enrichedSandBoulder,
    graniteFloor, graniteWall, graniteBoulder,
    permafrostFloor, permafrostWall, vine;

    public static CoreBlock industryCentrum;

    public static SolarGenerator ev;

    public static Drill smallDrill;

    public static Battery hvWire, smallBattery;

    public static Conveyor cdConveyor;

    public static MultiCrafter smallFurnace;

    public static void load(){
        finiteCopper = new FiniteOre(Items.copper);
        airport = new Airport("airport"){{
            requirements(Category.crafting,ItemStack.with());
        }};
        ashWall = new StaticWall("ash-wall");
        enrichedSandWall = new StaticWall("enriched-sand-wall");
        graniteWall = new StaticWall("granite-wall");
        permafrostWall = new StaticWall("permafrost-wall");

        ashFloor = new Floor("ash-floor");
        enrichedSandFloor = new Floor("enriched-sand-floor");
        graniteFloor = new Floor("granite-floor");
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
            alwaysReplace = false;
            instantDeconstruct = false;
        }};
    }

}
