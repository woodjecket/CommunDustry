package cd.world.comp.recipe;

import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import cd.CDMod;
import cd.entities.RecipeEntity;
import cd.struct.recipe.Recipe;
import cd.world.comp.IRecipeManager;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Block;

public abstract class AbstractRecipeManager {
    public final Building building;
    public final AbstractRecipeManagerFactory recipes;
    public transient AbstractRecipeVanillaEnhancer enhancer;
    public RecipeSlot[] slots;
    protected transient final ObjectIntMap<Recipe> count;
    protected transient final int[] items = new int[Vars.content.items().size];

    public AbstractRecipeManager(Building building, AbstractRecipeManagerFactory recipes) {
        this.recipes = recipes;
        this.building = building;
        slots = new RecipeSlot[this.recipes.getParallel()];
        count = new ObjectIntMap<>(recipes.recipes.size);
    }

    public void update() {
        refreshSlot();
        updateSlot();
        updateEnhancer();
    }

    protected abstract void updateEnhancer();

    public void updateSlot() {
        for (var slot : slots) {
            if (slot != null) slot.update();
        }
    }

    protected abstract void refreshSlot();

    public abstract void buildConfigure(Table table);

    public void write(Writes write) {
        int length = 0;
        for (RecipeSlot slot : slots) {
            if (slot != null) length++;
        }
        write.i(length);
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                write.s(i);
                write.i(slots[i].recipeEntity.recipe.id);
                write.f(slots[i].recipeEntity.progress);
            }
        }
    }

    public void read(Reads read) {
        var length = read.i();
        for (int i = 0; i < length; i++) {
            var index = read.s();
            var recipeID = read.i();
            var progress = read.f();
            if (slots[index] != null) {
                slots[index].passivePop();
            }
            slots[index] = new RecipeSlot(CDMod.xcontent.recipes().get(recipeID));
            slots[index].recipeEntity.progress = progress;
        }
    }

    public abstract Object getConfig();

    public abstract void passiveConfigured(Object object);

    public class RecipeSlot {

        public RecipeEntity recipeEntity;

        public RecipeSlot(Recipe recipe) {
            recipeEntity = recipe.newEntity(AbstractRecipeManager.this);
            count.increment(recipe);
            recipe.potentialInputItems.each(s -> items[s.item.id] += s.amount);
        }

        public void update() {
            var efficiency = recipeEntity.totalEfficiency() * recipeEntity.totalEfficiencyMultiplier();
            if (!recipeEntity.noOutput()) {
                recipeEntity.runWhile(efficiency);
            }
            recipeEntity.progress += building.delta() * efficiency / recipeEntity.recipe.craftTime;
            if (recipeEntity.progress >= 1f) {
                if (recipeEntity.noOutput()) {
                    recipeEntity.progress -= building.delta() * efficiency / recipeEntity.recipe.craftTime;
                } else {
                    recipeEntity.runOnce();
                    passivePop();
                }

            }
        }

        public void passivePop() {
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] == this) {
                    slots[i] = null;
                    count.increment(recipeEntity.recipe, -1);
                    recipeEntity.recipe.potentialInputItems.each(s -> items[s.item.id] -= s.amount);
                }
            }
        }

        public void activePop() {
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] == this) {
                    recipeEntity.manager.building.configure(Float.valueOf(i));
                }
            }

        }
    }

    public abstract static class AbstractRecipeManagerFactory {

        public Seq<Recipe> recipes = new Seq<>();

        public AbstractRecipeManager newManager(Building build) {
            return new MultiRecipeManager(build, this);
        }

        public void registerConfig(Block block) {
            block.config(IntSeq.class, (Building build, IntSeq s) -> {
                if (build instanceof IRecipeManager manager) manager.manager().passiveConfigured(s);
            });
            block.config(Float.class, (Building build, Float s) -> {
                if (build instanceof IRecipeManager manager) manager.manager().passiveConfigured(s);
            });
        }

        public int getParallel() {
            return 1;
        }
    }

    public abstract class AbstractRecipeVanillaEnhancer {
        public AbstractRecipeManager recipeManager;

        public AbstractRecipeVanillaEnhancer(AbstractRecipeManager recipeManager) {
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

        public abstract void assistDump(Building building);

    }

}
