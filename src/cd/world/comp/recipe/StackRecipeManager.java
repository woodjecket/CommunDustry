package cd.world.comp.recipe;

import arc.scene.ui.layout.Table;
import cd.world.comp.RecipeManager;
import cd.world.comp.Recipes;
import mindustry.gen.Building;

public class StackRecipeManager extends RecipeManager {
    public StackRecipeManager(Building building, Recipes recipes) {
        super(building, recipes);
    }

    @Override
    protected void refreshSlot() {

    }

    @Override
    public void buildConfigure(Table table) {

    }

    @Override
    public Object getConfig() {
        return null;
    }

    @Override
    public void passiveConfigured(Object object) {

    }

    @Override
    public int getParallel() {
        return 3;
    }

    @Override
    protected void updateEnhancer() {

    }
}
