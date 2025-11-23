package cd.struct.recipe.product;

import arc.scene.Element;
import arc.struct.Seq;
import cd.struct.recipe.Product;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import mindustry.type.LiquidStack;

public class ProductLiquids extends Product {
    public Seq<LiquidStack> liquids;

    public ProductLiquids(Seq<LiquidStack> liquids) {
        this.liquids = liquids;
    }

    @Override
    public void init(Recipe recipe) {
        super.init(recipe);
        recipe.potentialOutputLiquids.add(liquids);
        recipe.potentialOutputLiquid.add(liquids.map(s->s.liquid));
    }

    @Override
    public void produceWhile(Building building, float efficiency) {
        for(var stack : liquids){
            building.liquids.add(stack.liquid,stack.amount * efficiency);
        }
    }

    @Override
    public Element[] icon() {
        var icons = new Element[liquids.size];
        for (int i = 0; i < liquids.size; i++) {
            icons[i] = UIUtils.stack(liquids.get(i).liquid.uiIcon, ((int) (liquids.get(i).amount * 60f)));
        }
        return icons;
    }

    @Override
    public boolean canOutput(Building building) {
        for(var liquid: liquids){
            if(building.liquids.get(liquid.liquid) + liquid.amount > building.block.liquidCapacity) return false;
        }
        return true;
    }
}
