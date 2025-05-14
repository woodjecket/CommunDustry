package cd;

import arc.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import cd.manager.*;
import cd.ui.*;
import cd.world.block.*;
import mindustry.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.state;


public class CDMod extends Mod{
    public WidgetGroup galGroup = new WidgetGroup();


    public CDMod(){
        super();

    }

    @Override
    public void init(){
        super.init();
        Vars.ui.hudGroup.visible(() -> state.isGame() && !GalManager.visible);
        galGroup.fillParent = true;
        galGroup.touchable = Touchable.childrenOnly;
        galGroup.visible(() ->state.isGame() && GalManager.visible);
        Core.scene.add(galGroup);
        GalFragment.build(galGroup);
    }

    @Override
    public void loadContent(){
        Log.info("Cosmos insight benefits well-being");
        Block test = new TestGalBlock("test"){{
            requirements(Category.crafting,new ItemStack[]{});
        }};
    }
}