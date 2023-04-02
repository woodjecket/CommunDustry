package cd;

import arc.*;
import arc.struct.*;
import arc.util.*;
import cd.content.*;
import cd.content.test.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;

public class CDMod extends Mod{
    public boolean test = true;

    public CDMod(){
        Log.info("Loaded CDMod constructor.");
    }

    public static void configure(){
        ///PlanetDialog.debugSelect = true;
        //Log.level = LogLevel.debug;
        var meta = Vars.mods.getMod(cd.CDMod.class).meta;
        meta.name = Core.bundle.get("mod.commumdustry.displayName");
        meta.description = Core.bundle.get("mod.commumdustry.description");
        meta.author = Core.bundle.get("mod.commumdustry.author");

        Events.on(ClientLoadEvent.class,e->{
            var set = new IntSet();
            var dustBase = Core.atlas.getPixmap(Items.pyratite.fullIcon);
            for(int x = 0; x < dustBase.width; x++){
                for(int y = 0; y < dustBase.height; y++){
                    int rawColor = dustBase.get(x, y);
                    set.add(rawColor);
                }
            }
            Log.info(set.size);
        });


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

}
