package cd.world.comp.recipe;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import cd.struct.recipe.Recipe;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;

public class SingleRecipeManager extends AbstractRecipeManager {
    private static final Seq<Item> emptyItem = new Seq<>();
    private static final Seq<Liquid> emptyLiquid = new Seq<>();
    private Recipe selected;

    public SingleRecipeManager(Building building, RecipeManagerFactory recipes) {
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
            if (slots[i] == null && selected != null && selected.sufficient(building,items)) {
                slots[i] = new RecipeSlot(selected);
            }
        }
    }

    @Override
    public void buildConfigure(Table table) {
        table.table(Tex.buttonEdge1 , outer->{
            outer.pane(new Table( p -> {
                for (var recipe : recipes.recipes) {
                    p.button(rb -> {
                        rb.add(recipe.equation()).grow();
                    }, Styles.togglet, ()->{
                        if(selected == recipe){
                            unchosen(recipe);
                        }else{
                            chosen(recipe);
                        }
                    }).checked(rb -> selected == recipe).margin(10f).grow().row();
                }
            }));
        });
    }

    @Override
    public void write(Writes write) {
        super.write(write);
        if(selected!= null){
            write.i(selected.id);
        }else {
            write.i(-1);
        }

    }

    @Override
    public void read(Reads read) {
        super.read(read);
        var id = read.i();
        selected = id == -1 ? null : Recipe.all.get(id);
        if(!recipes.recipes.contains(selected)) selected = null;
    }

    @Override
    public Object getConfig() {
        return selected;
    }

    @Override
    public void passiveConfigured(Object object) {
        if(object instanceof Integer i){
            chosen(Recipe.all.get(i));
        }
    }

    private void chosen(Recipe recipe) {
        selected = recipe;
        var enhance = (SingleVanillaEnhancer) enhancer;
        enhance.filterItems = recipe.potentialInputItem;
        enhance.dumpItems = recipe.potentialOutputItem;
        enhance.filterLiquids = recipe.potentialInputLiquid;
        enhance.dumpLiquids = recipe.potentialOutputLiquid;
    }

    private void unchosen(Recipe recipe){
        selected = null;
        var enhance = (SingleVanillaEnhancer) enhancer;
        enhance.filterItems = emptyItem;
        enhance.dumpItems = emptyItem;
        enhance.filterLiquids = emptyLiquid;
        enhance.dumpLiquids = emptyLiquid;
    }

    public class SingleVanillaEnhancer extends RecipeVanillaEnhancer {

        private Seq<Item> filterItems = new Seq<>(), dumpItems = new Seq<>();

        private Seq<Liquid> filterLiquids = new Seq<>(), dumpLiquids = new Seq<>();

        private float power, heat, efficiency;

        public SingleVanillaEnhancer(AbstractRecipeManager recipeManager) {
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
