package cd.world.comp.recipe;

import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.Seq;
import cd.struct.recipe.Recipe;
import cd.ui.TableBar;
import cd.world.comp.IRecipeManager;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;
import mindustry.world.Block;

//TODO Unused by Commundustry
public class StackRecipeManager extends AbstractRecipeManager {
    private Recipe extendRecipe;
    public final Seq<BaseStackTask> tasks = new Seq<>();

    public StackRecipeManager(Building building, RecipeManagerFactory recipes) {
        super(building, recipes);
        enhancer = new StackVanillaEnhancer(this);
    }

    @Override
    protected void refreshSlot() {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                for (var task : tasks) {
                    if ((!task.recipe.sufficient(building, items) || count.get(task.recipe, 0) >= task.maxParallel || !task.running))
                        continue;
                    slots[i] = new RecipeSlot(task.recipe);
                    task.consume();
                    break;
                }
            }
        }
    }

    @Override
    public void buildConfigure(Table table) {
        table.clear();
        table.table(Tex.buttonEdge1, outer -> {
            outer.pane(new Table(p -> {
                for (var recipe : recipes.recipes) {
                    p.button(rb -> rb.add(recipe.equation()).grow(), Styles.togglet, () -> {
                        extendRecipe = recipe;
                        buildConfigure(table);
                    }).margin(10f).grow().row();
                    if (extendRecipe == recipe) {
                        p.table(e -> {
                            e.button("Purchase", () -> {
                                tasks.add(new DoTimesTask(recipe, 10));
                                rebuildFilter();
                                activeConfigure();
                            }).width(80f);
                        }).row();
                    }
                }
            }));

            Seq<BaseStackTask> oldTasks = new Seq<>();
            Table ta = new Table(Tex.buttonEdge3);
            ta.update(() -> {
                if (!tasks.equals(oldTasks)) {
                    ta.clear();
                    oldTasks.set(tasks);
                    for (var task : tasks) {
                        ta.table(Tex.buttonEdge3, i -> i.add(task.toString()).width(140f).
                                self(l -> {
                                    l.get().setWrap(true);
                                    l.get().setFontScale(0.7f);
                                })).width(140f).row();
                    }
                }
            });
            outer.pane(ta).grow().width(150f);


            outer.pane(new Table(p -> {
                for (int i = 0; i < slots.length; i++) {
                    int finalI = i;
                    p.table(Tex.buttonEdge3, s -> {
                        s.defaults().growX().height(30f).width(200f).pad(4);
                        s.add(new TableBar(() -> {
                            if (slots[finalI] != null) {
                                return slots[finalI].recipeEntity.progress;
                            }
                            return 0f;
                        }, () -> {
                            if (slots[finalI] == null) return null;
                            return slots[finalI].recipeEntity.getColor();
                        })).get().addChild(new Table(t -> {
                            t.update(() -> {
                                t.clear();
                                if (slots[finalI] != null) {
                                    t.add(slots[finalI].recipeEntity.recipe.equation()).grow();
                                }
                            });
                        }
                        ));
                    }).grow().row();
                }
            }));

        });
    }

    private void rebuildFilter() {
        var enhance = (StackVanillaEnhancer) enhancer;
        enhance.filterItems.clear();
        enhance.filterLiquids.clear();
        enhance.dumpLiquids.clear();
        enhance.dumpItems.clear();

        for (var task : tasks) {
            updateFilter(task);
        }
    }

    private void updateFilter(BaseStackTask task) {
        var enhance = (StackVanillaEnhancer) enhancer;

        enhance.filterItems.add(task.recipe.potentialInputItem);
        enhance.dumpItems.add(task.recipe.potentialOutputItem);
        enhance.filterLiquids.add(task.recipe.potentialInputLiquid);
        enhance.dumpLiquids.add(task.recipe.potentialOutputLiquid);

        enhance.dumpItems.removeAll(enhance.filterItems);
        enhance.dumpLiquids.removeAll(enhance.filterLiquids);
    }

    private void updateFilter(Recipe recipe) {
        var enhance = (StackVanillaEnhancer) enhancer;

        enhance.filterItems.add(recipe.potentialInputItem);
        enhance.dumpItems.add(recipe.potentialOutputItem);
        enhance.filterLiquids.add(recipe.potentialInputLiquid);
        enhance.dumpLiquids.add(recipe.potentialOutputLiquid);

        enhance.dumpItems.removeAll(enhance.filterItems);
        enhance.dumpLiquids.removeAll(enhance.filterLiquids);
    }


    private void activeConfigure() {
        var seq = new IntSeq();
        for (var task : tasks) {
            seq.add(task.id());
            seq.add(task.recipe.id);
            seq.add(task.special());
        }
        building.configure(seq);
    }

    @Override
    public Object getConfig() {
        return tasks;
    }

    @Override
    public void passiveConfigured(Object object) {

        if (object instanceof IntSeq seq) {
            tasks.clear();
            for (int i = 0; i < seq.size; i += 3) {
                var task = switch (seq.get(i)) {
                    case 0 -> new DoTimesTask(Recipe.all.get(seq.get(i + 1)), seq.get(i + 2));
                    case 1 -> new ReachAmountTask(Recipe.all.get(seq.get(i + 1)), building, seq.get(i + 2));
                    case 2 -> new LoopTask(Recipe.all.get(seq.get(i + 1)));
                    default -> throw new IllegalArgumentException();
                };
                tasks.add(task);
            }
            rebuildFilter();
        }

        if(object instanceof Float f && slots[f.intValue()] != null){
            slots[f.intValue()].passivePop();
        }

    }

    public int getParallel() {
        return 5;
    }

    @Override
    protected void updateEnhancer() {
        var enhance = (StackVanillaEnhancer) enhancer;
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

    public static class StackRecipeManagerFactory extends RecipeManagerFactory {

        public AbstractRecipeManager newManager(Building build) {
            return new StackRecipeManager(build, this) {
            };
        }

        public void registerConfig(Block block) {
            block.config(IntSeq.class, (Building build, IntSeq s) -> {
                if (build instanceof IRecipeManager manager) manager.manager().passiveConfigured(s);
            });
        }
        public int getParallel() {
            return 5;
        }
    }

    public class StackVanillaEnhancer extends RecipeVanillaEnhancer {

        private final Seq<Item> filterItems = new Seq<>(), dumpItems = new Seq<>();

        private final Seq<Liquid> filterLiquids = new Seq<>(), dumpLiquids = new Seq<>();

        private float power, heat, efficiency;

        public StackVanillaEnhancer(AbstractRecipeManager recipeManager) {
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

    public abstract class BaseStackTask {
        public Recipe recipe;
        public int maxParallel = 6;
        public boolean running = true;

        public abstract int id();

        public abstract int special();

        public abstract void consume();

        protected void pop() {
            tasks.remove(this);
            rebuildFilter();
        }
    }

    public class DoTimesTask extends BaseStackTask {
        public int times;

        public DoTimesTask(Recipe recipe, int times) {
            this.recipe = recipe;
            this.times = times;
        }

        @Override
        public int id() {
            return 0;
        }

        @Override
        public int special() {
            return times;
        }

        @Override
        public void consume() {
            times--;
            if (times <= 0) pop();
        }
    }

    public class ReachAmountTask extends BaseStackTask {
        public Building building;
        public int amount;

        public ReachAmountTask(Recipe recipe, Building building, int amount) {
            this.recipe = recipe;
            this.building = building;
            this.amount = amount;
        }

        @Override
        public int id() {
            return 1;
        }

        @Override
        public int special() {
            return amount;
        }

        @Override
        public void consume() {
            if (building.items.get(recipe.potentialOutputItem.first()) >= amount) running = false;
        }

    }

    public class LoopTask extends BaseStackTask {

        public LoopTask(Recipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public int id() {
            return 2;
        }

        @Override
        public int special() {
            return 0;
        }

        @Override
        public void consume() {

        }
    }
}
