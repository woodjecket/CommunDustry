package cd;

import arc.util.*;
import cd.content.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

import static arc.util.Log.LogLevel.debug;

public class CDMod extends Mod{
    public CDMod(){
        Log.info("Loaded CDMod constructor.");
    }

    @Override
    public void loadContent(){
        Log.info("Loading content.");
        CDItems.load();
        CDLiquids.load();
        CDBlocks.load();
        CDUnitTypes.load();
        CDPlanets.load();
        configure();
    }

    public static void configure(){
        PlanetDialog.debugSelect = true;
        Log.level = debug;
    }

}
