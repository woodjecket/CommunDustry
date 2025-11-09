package cd.world.comp.recipe;

import arc.struct.IntSeq;
import arc.struct.Seq;
import cd.struct.recipe.Recipe;
import cd.world.comp.IRecipeManager;
import mindustry.gen.Building;
import mindustry.world.Block;

public class RecipeManagerFactory {

    public Seq<Recipe> recipes = new Seq<>();

    public AbstractRecipeManager newManager(Building build) {
        return new MultiRecipeManager(build, this);
    }

    public void registerConfig(Block block) {
        block.config(IntSeq.class, (Building build, IntSeq s) -> {
            if (build instanceof IRecipeManager manager) manager.manager().passiveConfigured(s);
        });
    }
}
