package cd.content;

import cd.entities.component.*;

import static mindustry.type.ItemStack.with;

public class CDComponent{

    public static final BaseComponent
    basicDirectlyH2O2CrafterCatalyzerComponent = new CatalyzerCrafterComponent(){{
        catalyzerNecessity = true;
        catalyzer = with(CDItems.platinum, 2);
        catalyzerScale = new float[]{1f};
        catalyzerChance = 0.0001f;
    }};

}
