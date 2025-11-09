package cd.world.comp.recipe;

import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import cd.struct.recipe.Recipe;
import cd.world.comp.RecipeManager;
import cd.world.comp.Recipes;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;

public class MultiRecipeManager extends RecipeManager {
    private final Seq<Recipe> selects;


    public MultiRecipeManager(Building building, Recipes recipes) {
        super(building, recipes);
        enhancer = new MultiVanillaEnhancer(this);
        selects = recipes.recipes.copy();
        unchosen(null);
    }

    @Override
    public int getParallel() {
        return 3;
    }

    @Override
    protected void updateEnhancer() {

        var enhance = (MultiVanillaEnhancer) enhancer;
        float nextPower = 0, nextHeat = 0, nextEfficiency = 1;

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
            if (slots[i] == null) {
                for(var selected: selects){
                    if((!selected.sufficient(building, items) || count.get(selected,0) >= selected.maxParallel)) continue;
                    slots[i] = new RecipeSlot(selected);
                    break;
                }
            }
        }
    }

    @Override
    public void config(Table table) {
        table.table(Tex.buttonEdge1 , outer->{
            outer.pane(new Table( p -> {
                for (var recipe : recipes.recipes) {
                    p.button(rb -> {
                        rb.add(recipe.equation()).grow();
                    }, Styles.togglet, ()->{
                        if(selects.contains(recipe)){
                            unchosen(recipe);
                        }else {
                            chosen(recipe,false);
                        }
                    }).checked(rb -> selects.contains(recipe)).margin(10f).grow().row();
                }
            }));

            outer.pane(new Table( p -> {
                for (int i = 0; i < slots.length; i++) {
                    int finalI = i;
                    p.table(Tex.buttonEdge3, s->{
                        s.add("").width(100f).update(l->{
                            if(slots[finalI] == null) {
                                l.setText("empty");
                            }else {
                                l.setText(slots[finalI].recipeEntity.toString());
                            }
                        }).self(l->{
                            l.get().setWrap(true);
                            l.get().setFontScale(0.7f);
                        });
                    }).row();
                }
            }));

        });
    }

    private void unchosen(Recipe recipe) {
        selects.remove(recipe);

        var enhance = (MultiVanillaEnhancer) enhancer;
        enhance.filterItems.clear();
        enhance.filterLiquids.clear();

        for(var selected : selects){
            chosen(selected,true);
        }
    }

    private void chosen(Recipe recipe, boolean silent) {
        if(!silent) selects.add(recipe);
        var enhance = (MultiVanillaEnhancer) enhancer;

        enhance.filterItems.add(recipe.potentialInputItem);
        enhance.dumpItems.add(recipe.potentialOutputItem);
        enhance.filterLiquids.add(recipe.potentialInputLiquid);
        enhance.dumpLiquids.add(recipe.potentialOutputLiquid);

        enhance.dumpItems.removeAll(enhance.filterItems);
        enhance.dumpLiquids.removeAll(enhance.filterLiquids);
    }


    public class MultiVanillaEnhancer extends RecipeVanillaEnhancer {

        private final Seq<Item> filterItems = new Seq<>(), dumpItems = new Seq<>();

        private final Seq<Liquid> filterLiquids = new Seq<>(), dumpLiquids = new Seq<>();

        private float power, heat, efficiency;

        public MultiVanillaEnhancer(RecipeManager recipeManager) {
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
            return dumpLiquids;
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

        @Override
        public void assistDump(Building building) {
            dumpItems.each(building::dump);
            dumpLiquids.each(building::dumpLiquid);
        }
    }
}
