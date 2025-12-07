package cd.struct.recipe.product;

import arc.math.Rand;
import arc.scene.Element;
import arc.struct.Seq;
import cd.struct.recipe.Product;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;

public class ProductChanceItems extends Product {
    //TODO have no idea about how it can be synced. But since it is average, nobody would notice
    //TODO The items keeps floating
    public Rand rand = new Rand();
    public Seq<ItemStack> results;

    public ProductChanceItems(Seq<ItemStack> items) {
        this.results = items;
    }

    @Override
    public void init(Recipe recipe) {
        super.init(recipe);
        recipe.potentialOutputItems.add(results.map(i -> new ItemStack(i.item, 1)));
        recipe.potentialOutputItem.add(results.map(s -> s.item));
    }

    @Override
    public void produceOnce(Building building) {
        int sum = 0;
        for (ItemStack stack : results) sum += stack.amount;

        int i = rand.random(0, sum - 1);
        int count = 0;
        Item item = null;

        //guaranteed desync since items are random - won't be fixed and probably isn't too important
        for (ItemStack stack : results) {
            if (i >= count && i < count + stack.amount) {
                item = stack.item;
                break;
            }
            count += stack.amount;
        }

        var capacity = building instanceof CoreBlock.CoreBuild c ? c.storageCapacity : building.block.itemCapacity;

        if (item != null && building.items.get(item) < capacity) {
            building.items.add(item, 1);
        }
    }

    @Override
    public Element[] icon() {
        var icons = new Element[results.size];
        for (int i = 0; i < results.size; i++) {
            icons[i] = UIUtils.stack(results.get(i).item.uiIcon, results.get(i).amount);
        }
        return icons;
    }

    @Override
    public boolean canOutput(Building building) {
        for(var item: results){
            var capacity = building instanceof CoreBlock.CoreBuild c ? c.storageCapacity : building.block.itemCapacity;
            if(building.items.get(item.item) + item.amount > capacity) return false;
        }
        return true;
    }
}
