package cd.struct.recipe.product;

import arc.struct.Seq;
import cd.struct.recipe.Product;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import mindustry.type.ItemStack;

public class ProductItems extends Product {
    public Seq<ItemStack> items;

    public ProductItems(Seq<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void produceOnce(Building building) {
        building.items.add(items);
    }
}
