package cd.world.comp;

import arc.scene.ui.layout.Table;
import cd.entities.RecipeEntity;
import cd.struct.recipe.Recipe;
import mindustry.gen.Building;

public abstract class RecipeManager {
    public Building building;
    public Recipes recipes;
    public RecipeSlot[] slots;

    public RecipeManager(Building building, Recipes recipes){
        this.recipes = recipes;
        this.building = building;
        slots = new RecipeSlot[getParallel()];
    }

    public int getParallel() {
        return 1;
    }

    public void update() {
        refreshSlot();
        updateSlot();
    }

    public void updateSlot() {
        for(var slot : slots){
            slot.update();
        }
    }

    protected abstract void refreshSlot();

    public abstract void config(Table table);

    public class RecipeSlot {

        public RecipeEntity recipeEntity;

        public RecipeSlot(Recipe recipe) {
            recipeEntity = recipe.newEntity(RecipeManager.this);
        }

        public void update() {
            if(recipeEntity.noOutput()) return;
            var efficiency = recipeEntity.totalEfficiency() * recipeEntity.totalEfficiencyMultiplier() ;
            recipeEntity.runWhile(efficiency);
            recipeEntity.progress += building.delta() * efficiency / recipeEntity.recipe.craftTime;

            if(recipeEntity.progress >= 1f){
                recipeEntity.runOnce();
                pop();
            }
        }

        public void pop(){
            for (int i = 0; i < slots.length; i++) {
                if(slots[i] == this) slots[i]
                        = null;
            }
        }
    }

}
