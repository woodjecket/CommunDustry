package cd.struct.recipe.reactant;

import arc.scene.Element;
import arc.struct.Seq;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.ui.UIUtils;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import mindustry.type.LiquidStack;

public class ReactantLiquids extends Reactant {
    public Seq<LiquidStack> liquids;
    private Seq<Content> consume;

    public ReactantLiquids(Seq<LiquidStack> liquids) {
        this.liquids = liquids;
        consume = liquids.map(s->s.liquid);
    }

    @Override
    public void init(Recipe recipe) {
        super.init(recipe);
        recipe.potentialInputLiquids.add(liquids);
        recipe.potentialInputLiquid.add(liquids.map(s->s.liquid));
    }

    @Override
    public void reactWhile(Building building, float efficiency) {
        for(var stack : liquids){
            building.liquids.remove(stack.liquid,stack.amount * efficiency);
        }
    }
    
    @Override
    public float efficiency(Building building) {
        float min = 1;
        for(var stack : liquids){
            min = Math.min(building.liquids.get(stack.liquid) / (stack.amount), min);
        }
        return min;
    }

    @Override
    public Element[] icon() {
        var icons = new Element[liquids.size];
        for (int i = 0; i < liquids.size; i++) {
            icons[i] = UIUtils.stack(liquids.get(i).liquid.uiIcon, liquids.get(i).amount);
        }
        return icons;
    }
}
