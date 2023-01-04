package cd.content;

import cd.entities.component.BaseComponent;
import cd.entities.component.CatalyzerComponent;
import static mindustry.type.ItemStack.*;

public class CDComponent {

    public static final BaseComponent 
    basicDirectlyH2O2CrafterCatalyzerComponent = new CatalyzerComponent(){{
        catalyzerNecessity = true;
        catalyzer = with(CDItems.platinum,2);
        catalyzerScale = new float[]{1f};
        catalyzerChance = 0.0001f;
    }};
    
}
