package cd;

import arc.math.*;
import arc.util.*;
import cd.ui.*;
import mindustry.*;
import mindustry.mod.*;


public class CDMod extends Mod{


    public CDMod(){
        super();

    }

    @Override
    public void loadContent(){
        Log.info("Cosmos insight benefits well-being");
        var rand = new Rand();
    }

    @Override
    public void init(){
        super.init();
        GalFragment.build(Vars.ui.hudGroup);
    }
}