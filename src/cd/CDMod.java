package cd;

import arc.Core;
import arc.util.Log;
import cd.entities.FiniteOreUpdater;
import cd.map.SingleMapDeepVeinGenerator;
import cd.world.block.environment.FiniteOre;
import cd.world.block.production.VeinMiner;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.mod.Mod;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.environment.OverlayFloor;
import mindustry.world.meta.BuildVisibility;

public class CDMod extends Mod{
    public static FiniteOreUpdater oreUpdater = new FiniteOreUpdater();

    public CDMod() {
        super();
        Log.info("Commundustry constructor is loaded");
        if(Core.scene!=null){
            BaseDialog cont = new BaseDialog("Commundustry");
            cont.image(Core.atlas.find(Vars.content.transformName("frog"))).center();
            cont.addCloseButton();
        }
        oreUpdater.init();
    }

    @Override
    public void loadContent() {
        Log.info("[red]Commundustry[]-[blue]v4[] is coming back!!!");
        OverlayFloor ore1e = new OverlayFloor("blast-exhuasted");
        FiniteOre ore1 = new FiniteOre(Items.blastCompound){{
            exhausted = ore1e;
        }};
        VeinMiner miner = new VeinMiner("1"){{
            buildVisibility = BuildVisibility.shown;
        }};
        SingleMapDeepVeinGenerator s = new SingleMapDeepVeinGenerator();
    }
}