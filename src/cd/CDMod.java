package cd;

import arc.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import cd.content.*;
import cd.manager.*;
import cd.ui.*;
import mindustry.*;
import mindustry.mod.*;

import static mindustry.Vars.state;


public class CDMod extends Mod{

    public WidgetGroup galGroup = new WidgetGroup();


    public CDMod(){
        super();

    }

    @Override
    public void init(){
        super.init();
        initUI();
    }

    @Override
    public void loadContent(){
        Log.info("All is Alon Master's order");
        CDBlocks.load();
    }

    private void initUI(){
        Vars.ui.hudGroup.visible(() -> state.isGame() && !GalManager.visible);
        galGroup.fillParent = true;
        galGroup.touchable = Touchable.childrenOnly;
        galGroup.visible(() -> state.isGame() && GalManager.visible);
        Core.scene.add(galGroup);
        GalFragment.build(galGroup);
    }
}