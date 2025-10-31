package cd.world.comp.recipe;

import arc.scene.ui.Button;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import cd.struct.recipe.Recipe;
import cd.world.comp.RecipeManager;
import cd.world.comp.Recipes;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.blocks.heat.HeatConsumer;

public class SingleRecipeManager extends RecipeManager {
    private Recipe selected;

    public SingleRecipeManager(Building building, Recipes recipes) {
        super(building, recipes);
        selected = recipes.recipes.get(0);
        enhancer = new SingleVanillaEnhancer(this);
        chosen(selected);
    }

    @Override
    protected void updateEnhancer() {
        var enhance = (SingleVanillaEnhancer) enhancer;
        float nextPower = 0, nextHeat = 0, nextEfficiency = 1;
        enhance.efficiency = 1;
        for (var slot : slots) {
            if (slot == null) continue;

            nextPower += slot.recipeEntity.recipe.power * slot.recipeEntity.totalEfficiencyMultiplier();
            nextHeat += slot.recipeEntity.recipe.heat * slot.recipeEntity.totalEfficiencyMultiplier();
            nextEfficiency *= slot.recipeEntity.totalEfficiency() * slot.recipeEntity.totalEfficiencyMultiplier();

        }

        enhance.power = nextPower;
        enhance.heat = nextHeat;
        enhance.efficiency = nextEfficiency;
    }


    @Override
    protected void refreshSlot() {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null && selected != null) {
                slots[i] = new RecipeSlot(selected);
            }
        }
    }

    @Override
    public void config(Table table) {
        table.pane(new Table(Tex.buttonEdge2 , p -> {
            for (var recipe : recipes.recipes) {
                p.button(r -> {
                    r.add(recipe.equation());
                }, Styles.defaultb, ()->{
                    Log.info(recipe);
                }).row();
            }
        }));
    }

    private void chosen(Recipe recipe) {
        var enhance = (SingleVanillaEnhancer) enhancer;
        enhance.filterItems = recipe.potentialInputItem;
        enhance.dumpItems = recipe.potentialOutputItem;
        enhance.filterLiquids = recipe.potentialInputLiquid;
        enhance.dumpLiquids = recipe.potentialOutputLiquid;
    }

    public class SingleVanillaEnhancer extends RecipeVanillaEnhancer {

        private Seq<Item> filterItems = new Seq<>(), dumpItems = new Seq<>();

        private Seq<Liquid> filterLiquids = new Seq<>(), dumpLiquids = new Seq<>();

        private float power, heat, efficiency;

        public SingleVanillaEnhancer(RecipeManager recipeManager) {
            super(recipeManager);
        }

        @Override
        public Seq<Item> filterItems() {
            return filterItems;
        }

        @Override
        public Seq<Item> dumpItems() {
            return dumpItems;
        }

        @Override
        public Seq<Liquid> filterLiquids() {
            return filterLiquids;
        }

        @Override
        public Seq<Liquid> dumpLiquids() {
            return filterLiquids;
        }

        @Override
        public float powerOut() {
            return power >= 0 ? power : 0;
        }

        @Override
        public float powerIn() {
            return power < 0 ? -power : 0;
        }

        @Override
        public float heatOut() {
            return heat >= 0 ? heat : 0;
        }

        @Override
        public float heatIn() {
            return heat < 0 ? -heat : 0;
        }


        @Override
        public float displayEfficiency() {
            return efficiency;
        }
    }
}
