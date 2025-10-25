package cd;

import arc.*;
import arc.scene.Element;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import cd.manager.*;
import cd.ui.*;
import cd.world.block.*;
import mindustry.*;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.*;
import mindustry.world.blocks.power.HeaterGenerator;
import mindustry.world.blocks.power.PowerBlock;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawMulti;

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
        Block test2 = new MultiCrafter("nm"){{
            requirements(Category.crafting,new ItemStack[]{});
        }};
    }
}