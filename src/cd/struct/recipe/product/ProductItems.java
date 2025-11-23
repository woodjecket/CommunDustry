package cd.struct.recipe.product;

import arc.scene.Element;
import arc.struct.Seq;
import cd.struct.recipe.Product;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;

public class ProductItems extends Product {
    public Seq<ItemStack> items;

    public ProductItems(Seq<ItemStack> items) {
        this.items = items;
    }
    @Override
    public void init(Recipe recipe) {
        super.init(recipe);
        recipe.potentialOutputItems.add(items);
        recipe.potentialOutputItem.add(items.map(s->s.item));
    }
    @Override
    public void produceOnce(Building building) {
        building.items.add(items);
    }

    @Override
    public Element[] icon() {
        var icons = new Element[items.size];
        for (int i = 0; i < items.size; i++) {
            icons[i] = UIUtils.stack(items.get(i).item.uiIcon, items.get(i).amount);
        }
        return icons;
    }

    @Override
    public boolean canOutput(Building building) {
        for(var item: items){
            var capacity = building instanceof CoreBlock.CoreBuild c ? c.storageCapacity : building.block.itemCapacity;
            if(building.items.get(item.item) + item.amount > capacity) return false;
        }
        return true;
    }
}
