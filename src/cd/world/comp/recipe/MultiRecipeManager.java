package cd.world.comp.recipe;

import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import cd.struct.recipe.Recipe;
import cd.world.comp.RecipeManager;
import cd.world.comp.Recipes;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;

public class MultiRecipeManager extends RecipeManager {
    private final Seq<Recipe> selects = new Seq<>();

    public MultiRecipeManager(Building building, Recipes recipes) {
        super(building, recipes);
        enhancer = new MultiVanillaEnhancer(this);
        rebuildFilter();
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
                for (var selected : selects) {
                    if ((!selected.sufficient(building, items) || count.get(selected, 0) >= selected.maxParallel))
                        continue;
                    slots[i] = new RecipeSlot(selected);
                    break;
                }
            }
        }
    }

    @Override
    public void buildConfigure(Table table) {
        table.table(Tex.buttonEdge1, outer -> {
            outer.pane(new Table(p -> {
                for (var recipe : recipes.recipes) {
                    p.button(rb -> rb.add(recipe.equation()).grow(), Styles.togglet, () -> {
                        if (selects.contains(recipe)) {
                            selects.remove(recipe);
                            activeConfigure();
                            rebuildFilter();
                        } else {
                            selects.add(recipe);
                            activeConfigure();
                            updateFilter(recipe);
                        }
                    }).checked(rb -> selects.contains(recipe)).margin(10f).grow().row();
                }
            }));

            outer.pane(new Table(p -> {
                for (int i = 0; i < slots.length; i++) {
                    int finalI = i;
                    p.table(Tex.buttonEdge3, s -> {
                        s.add("").width(100f).update(l -> {
                            if (slots[finalI] == null) {
                                l.setText("empty");
                            } else {
                                l.setText(slots[finalI].recipeEntity.toString());
                            }
                        }).self(l -> {
                            l.get().setWrap(true);
                            l.get().setFontScale(0.7f);
                        });
                    }).row();
                }
            }));

        });
    }

    @Override
    public void write(Writes write) {
        super.write(write);
        write.i(selects.size);
        for (var selected : selects) {
            write.i(selected.id);
        }
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        selects.clear();
        var length = read.i();
        for (int i = 0; i < length; i++) {
            var id = read.i();
            selects.add(Recipe.all.get(id));
        }
        selects.removeAll(r -> !recipes.recipes.contains(r));
        rebuildFilter();
    }

    @Override
    public Object getConfig() {
        return selects;
    }

    @Override
    public void passiveConfigured(Object object) {
        selects.clear();
        if(object instanceof IntSeq seq){
            for (int i: seq.items){
                selects.add(Recipe.all.get(i));
            }
        }
        rebuildFilter();
    }

    private void activeConfigure() {
        var ints = new int[selects.size];
        for (int i = 0; i < selects.size; i++) {
            ints[i] = selects.get(i).id;
        }
        building.configure(new IntSeq(ints));
    }

    private void rebuildFilter() {
        var enhance = (MultiVanillaEnhancer) enhancer;
        enhance.filterItems.clear();
        enhance.filterLiquids.clear();
        enhance.dumpLiquids.clear();
        enhance.dumpItems.clear();

        for (var selected : selects) {
            updateFilter(selected);
        }
    }

    private void updateFilter(Recipe recipe) {
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
