package cd.struct.recipe.reactant;

import arc.scene.Element;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import cd.world.comp.IHeat;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.world.blocks.heat.HeatConsumer;

public class ReactantHeat extends Reactant {
    public float heat;

    public ReactantHeat(float heat) {
        this.heat = heat;
    }


    @Override
    public void init(Recipe recipe) {
        recipe.heat -= heat;
    }

    @Override
    public float efficiency(Building building) {
        if(building instanceof IHeat heatBlock && building instanceof HeatConsumer heatConsumer){
            return Math.min(heatBlock.heat() / (heatConsumer.heatRequirement() + 0.0001f), 1f);
        }else {
            return 1f;
        }
    }

    @Override
    public Element[] icon() {
        var icons = new Element[1];
        icons[0] = UIUtils.stack(Icon.waves.getRegion(), ((int) (heat)),32f);
        return icons;
    }
}
