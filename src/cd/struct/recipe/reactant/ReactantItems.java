package cd.struct.recipe.reactant;

import arc.scene.Element;
import arc.struct.Seq;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.ui.ItemsDisplay;

public class ReactantItems extends Reactant {
    public Seq<ItemStack> items;
    private Seq<Content> consume;

    public ReactantItems(Seq<ItemStack> items) {
        this.items = items;
        consume = items.map(s -> s.item);
    }

    @Override
    public void init(Recipe recipe) {
        super.init(recipe);
        recipe.potentialInputItems.add(items);
        recipe.potentialInputItem.add(items.map(s -> s.item));
    }

    @Override
    public void reactOnce(Building building) {
        building.items.remove(items);
    }


    @Override
    public float efficiency(Building building) {
        return building.items.has(items) ? 1f : 0f;
    }

    @Override
    public Element[] icon() {
        var icons = new Element[items.size];
        for (int i = 0; i < items.size; i++) {
            icons[i] = UIUtils.stack(items.get(i).item.uiIcon, items.get(i).amount);
        }
        return icons;
    }

}
