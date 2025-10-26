package cd;

import arc.*;
import arc.graphics.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import cd.content.*;
import cd.manager.*;
import cd.map.planets.*;
import cd.ui.*;
import cd.world.block.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.maps.generators.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.state;


public class CDMod extends Mod{

    public WidgetGroup galGroup = new WidgetGroup();
    //注册事件
    public FiniteOreManager fom = new FiniteOreManager();

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
        CDUnitTYpe.load();
        Planets.tantros.generator = new GenPlanetGenerator();
        PlanetDialog.debugSelect = true;
        Block test = new TestGalBlock("test"){{
            requirements(Category.crafting,new ItemStack[]{});
        }};
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