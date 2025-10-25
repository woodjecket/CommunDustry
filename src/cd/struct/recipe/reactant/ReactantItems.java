package cd.struct.recipe.reactant;

import arc.struct.Seq;
import cd.struct.recipe.Reactant;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import mindustry.type.ItemStack;

public class ReactantItems extends Reactant {
    public Seq<ItemStack> items;
    private Seq<Content> consume;

    public ReactantItems(Seq<ItemStack> items) {
        this.items = items;
        consume = items.map(s->s.item);
    }


    @Override
    public void reactOnce(Building building) {
        building.items.remove(items);
    }

    @Override
    public Seq<Content> potentialReactant() {
        return consume;
    }

    @Override
    public float efficiency(Building building) {
        return building.items.has(items)? 1f: 0f;
    }
}
