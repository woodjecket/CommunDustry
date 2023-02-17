package cd;

import arc.Core;
import arc.util.Log;
import cd.content.*;
import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.ui.dialogs.PlanetDialog;

import static arc.util.Log.LogLevel.debug;

public class CDMod extends Mod{
    public CDMod(){
        Log.info("Loaded CDMod constructor.");
    }

    public void configure() {
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
    }

}
