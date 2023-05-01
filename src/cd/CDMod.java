package cd;

import arc.*;
import arc.util.*;
import cd.content.*;
import cd.content.test.*;
import mindustry.*;
import mindustry.mod.*;

public class CDMod extends Mod{
    public boolean test = true;

    public CDMod(){
        Log.info("Loaded CDMod constructor.");
    }

    @Override
    public void loadContent(){
        configure();
        Log.debug("Start loading content.");
        CDItems.load();
        Log.debug("Loaded items.");
        CDLiquids.load();
        Log.debug("Loaded liquids.");
        CDBlocks.load();
        Log.debug("Loaded blocks.");
        CDUnitTypes.load();
        Log.debug("Loaded units.");
        CDPlanets.load();
        Log.debug("Loaded planets.");
        if(test) TestContent.load();

    }

    public static void configure(){
        ///PlanetDialog.debugSelect = true;
        //Log.level = LogLevel.debug;
        var meta = Vars.mods.getMod(cd.CDMod.class).meta;
        meta.name = Core.bundle.get("mod.commumdustry.displayName");
        meta.description = Core.bundle.get("mod.commumdustry.description");
        meta.author = Core.bundle.get("mod.commumdustry.author");
    }

}
