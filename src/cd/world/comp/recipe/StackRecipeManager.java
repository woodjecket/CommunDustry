package cd.world.comp.recipe;

import arc.scene.ui.layout.Table;
import mindustry.gen.Building;

public class StackRecipeManager extends AbstractRecipeManager {
    public StackRecipeManager(Building building, RecipeManagerFactory recipes) {
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
