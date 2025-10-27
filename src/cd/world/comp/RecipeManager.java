package cd.world.comp;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import cd.entities.RecipeEntity;
import cd.struct.recipe.Recipe;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.blocks.heat.HeatBlock;

public abstract class RecipeManager {
    public Building building;
    public Recipes recipes;
    public RecipeVanillaEnhancer enhancer;
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
        updateEnhancer();
    }

    protected abstract void updateEnhancer();

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

    public abstract class RecipeVanillaEnhancer{
        public RecipeManager recipeManager;


        public RecipeVanillaEnhancer(RecipeManager recipeManager) {
            this.recipeManager = recipeManager;
        }

        public abstract Seq<Item> filterItems();

        public abstract Seq<Item> dumpItems();

        public abstract Seq<Liquid> filterLiquids();

        public abstract Seq<Liquid> dumpLiquids();

        public abstract float powerOut();

        public abstract float powerIn();

        public abstract float heatOut();

        public abstract float heatIn();

        public abstract float displayEfficiency();


    }

}
