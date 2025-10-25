package cd.world.comp.recipe;

import arc.scene.ui.layout.Table;
import cd.struct.recipe.Recipe;
import cd.world.comp.RecipeManager;
import cd.world.comp.Recipes;
import mindustry.gen.Building;

public class SingleRecipeManager extends RecipeManager {
    private Recipe selected;

    public SingleRecipeManager(Building building, Recipes recipes) {
        super(building, recipes);
        selected = recipes.recipes.get(0);
    }


    @Override
    protected void refreshSlot() {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null && selected != null){
                slots[i] = new RecipeSlot(selected);
            }
        }
    }

    @Override
    public void config(Table table) {

    }
}
