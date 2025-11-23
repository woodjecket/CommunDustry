package cd.struct.recipe.reactant;

import arc.scene.Element;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;

public class ReactantAnyItem extends Reactant {
    @Override
    public void reactOnce(Building building) {
        //Incinerator just handle item into void. I cannot. Just clear it
        building.items.clear();
    }

    @Override
    public void init(Recipe recipe) {
        super.init(recipe);
        recipe.potentialInputItem.add(Vars.content.items().copy());
    }

    @Override
    public Element[] icon() {
        var icons = new Element[1];
        icons[0] = UIUtils.stack(Icon.trash.getRegion(), 0,32f);
        return icons;
    }
}
