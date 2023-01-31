package cd;

import arc.util.*;
import cd.content.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

public class CDMod extends Mod{
    public static CDBlocks modBlocks;
    public static CDItems modItems;
    public static CDLiquids modLiquids;
    public static CDUnitTypes modUnitTypes;
    public static CDPlanets modPlanets;

    public CDMod(){
        Log.info("Loaded CDMod constructor.");
    }

    @Override
    public void loadContent(){
        Log.info("Loading some example content.");
        // why not ues static? Yes
        modItems = new CDItems();
        modItems.load();
        modLiquids = new CDLiquids();
        modLiquids.load();
        modBlocks = new CDBlocks();
        modBlocks.load();
        modUnitTypes = new CDUnitTypes();
        modUnitTypes.load();
        modPlanets = new CDPlanets();
        modPlanets.load();
        configure();
    }

    public void configure(){
        PlanetDialog.debugSelect = true;
    }

}
