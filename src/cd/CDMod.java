package cd;

import arc.Core;
import arc.util.Log;
import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.ui.dialogs.BaseDialog;

public class CDMod extends Mod{

    public CDMod() {
        super();
        Log.info("Commundustry constructor is loaded");
        if(Core.scene!=null){
            BaseDialog cont = new BaseDialog("Commundustry");
            cont.image(Core.atlas.find(Vars.content.transformName("frog"))).center();
            cont.addCloseButton();
        }
    }

    @Override
    public void loadContent() {
        Log.info("[red]Commundustry[]-[blue]v4[] is coming back!!!");
    }
}