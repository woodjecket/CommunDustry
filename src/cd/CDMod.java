package cd;

import arc.*;
import arc.util.*;
import cd.content.CDBlocks;
import cd.content.CDItems;
import cd.content.CDLiquids;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

public class CDMod extends Mod{
    public static CDBlocks modBlocks;
    public static CDItems modItems;
    public static CDLiquids modLiquids;
    public CDMod(){
        Log.info("Loaded CDMod constructor.");

        //listen for game load event
        Events.on(ClientLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("behold").row();
                //mod sprites are prefixed with the mod name (this mod is called 'example-java-mod' in its config)
                dialog.cont.image(Core.atlas.find("commumdustry-frog")).pad(20f).row();
                dialog.cont.button("I see", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
        //listen for game load event
        Events.on(SaveLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("You have just loaded the save.").row();
                //mod sprites are prefixed with the mod name (this mod is called 'example-java-mod' in its config)
                dialog.cont.image(Core.atlas.find("commumdustry-cerium")).pad(20f).row();
                dialog.cont.button("No", dialog::hide).size(100f, 50f);
                dialog.cont.button("Yes", dialog::show).size(50f, 50f);
                dialog.show();
            });
        });

    }

    @Override
    public void loadContent(){
        Log.info("Loading some example content.");
        //why not ues static? Yes
        modItems = new CDItems();
        modItems.load();
        modLiquids = new CDLiquids();
        modLiquids.load();
        modBlocks = new CDBlocks();
        modBlocks.load();
    }

}
