package cd;

import arc.util.*;
import cd.content.*;
import mindustry.mod.*;

public class CDMod extends Mod{
    public static CDBlocks modBlocks;
    public static CDItems modItems;
    public static CDLiquids modLiquids;

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
    }

}
