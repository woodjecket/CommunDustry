package cd.world.comp;

import arc.struct.Seq;
import cd.struct.recipe.Recipe;
import cd.world.block.MultiCrafter;
import cd.world.comp.recipe.SingleRecipeManager;
import mindustry.gen.Building;

public class Recipes {

    public Seq<Recipe> recipes = new Seq<>();

    public RecipeManager newManager(Building build) {
        return new SingleRecipeManager(build, this);
    }
}
