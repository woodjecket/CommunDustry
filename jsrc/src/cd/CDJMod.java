package cd;

import cd.content.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

@SuppressWarnings("unused")
public class CDJMod extends Mod{
    @Override
    public void loadContent(){
        super.loadContent();
        PlanetDialog.debugSelect = true;
        CDItems.load();
        CDLiquids.load();
        CDBlocks.load();
        CDUnitTypes.load();
        CDPlanets.load();



    }
}
