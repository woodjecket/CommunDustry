package cd;

import arc.*;
import arc.util.*;
import cd.content.*;
import cd.content.test.*;
import mindustry.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

import static arc.util.Log.LogLevel.debug;

public class CDMod extends Mod{
    public boolean test = true;
    public CDMod(){
        Log.info("Loaded CDMod constructor.");
    }

    public static void configure() {
        PlanetDialog.debugSelect = true;
        Log.level = debug;
        var meta = Vars.mods.getMod(cd.CDMod.class).meta;
        meta.name = Core.bundle.get("mod.commumdustry.displayName");
        meta.description = Core.bundle.get("mod.commumdustry.description");
        meta.author = Core.bundle.get("mod.commumdustry.author");
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
        if(test) new TestBlocks();

    }

}
