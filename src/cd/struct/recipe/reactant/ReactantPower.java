package cd.struct.recipe.reactant;

import arc.scene.Element;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import mindustry.gen.Building;
import mindustry.gen.Icon;

public class ReactantPower extends Reactant {

    public float power;

    public ReactantPower(float power) {
        this.power = power;
    }


    @Override
    public void init(Recipe recipe) {
        recipe.power -= power;
    }

    @Override
    public float efficiency(Building building) {
        return building.power.status;
    }

    @Override
    public Element[] icon() {
        var icons = new Element[1];
        icons[0] = UIUtils.stack(Icon.power.getRegion(), power);
        return icons;
    }
}
