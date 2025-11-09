package cd.world.comp;

import arc.struct.IntSeq;
import arc.struct.Seq;
import cd.struct.recipe.Recipe;
import cd.world.block.MultiCrafter;
import cd.world.comp.recipe.MultiRecipeManager;
import cd.world.comp.recipe.SingleRecipeManager;
import mindustry.gen.Building;
import mindustry.world.Block;

public class Recipes {

    public Seq<Recipe> recipes = new Seq<>();

    public RecipeManager newManager(Building build) {
        return new MultiRecipeManager(build, this);
    }

    public void registerConfig(Block block) {
        block.config(IntSeq.class, (Building build, IntSeq s) -> {
            if (build instanceof IRecipeManager manager) manager.manager().passiveConfigured(s);
        });
    }
}
