package cd.world.block;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.Seq;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.struct.recipe.product.ProductItems;
import cd.struct.recipe.reactant.ReactantHeat;
import cd.struct.recipe.reactant.ReactantItems;
import cd.struct.recipe.reactant.ReactantLiquids;
import cd.struct.recipe.reactant.ReactantPower;
import cd.world.comp.RecipeManager;
import cd.world.comp.Recipes;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.blocks.heat.HeatConsumer;
import mindustry.world.meta.BlockFlag;

public class MultiCrafter extends Block {
    public Recipes recipes = new Recipes() {{
        recipes.add(new Recipe() {{
            reactants.add(new ReactantItems(new Seq<>(ItemStack.with(Items.copper, 1))),
                    new ReactantLiquids(new Seq<>(LiquidStack.with(Liquids.slag, 0.1f))),
                    new ReactantPower(6f),
                    new ReactantHeat(1f)
            );
            products.add(new ProductItems(new Seq<>(ItemStack.with(Items.lead, 1))));
        }});
    }};

    public float heatCapacity = 10;

    public MultiCrafter(String name) {
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        ambientSound = Sounds.machine;
        sync = true;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
        drawArrow = false;
        configurable = true;
        hasLiquids = true;
        hasPower = true;
        liquidCapacity = 100f;
        consumePowerDynamic((MultiCrafterBuild b)-> b.recipeManager.enhancer.powerIn());
    }

    @Override
    public void init() {
        super.init();
        recipes.recipes.each(Recipe::init);
    }

    public class MultiCrafterBuild extends Building implements HeatBlock, HeatConsumer {
        public RecipeManager recipeManager = recipes.newManager(this);
        public float heat;
        public float[] sideHeat = new float[4];

        @Override
        public void updateTile() {
            super.updateTile();
            recipeManager.update();
            if (recipeManager.enhancer.heatOut() > 0) {
                heat = Mathf.approachDelta(heat, recipeManager.enhancer.heatOut() * efficiency, 10 * delta());
            }
            heat = calculateHeat(sideHeat);

            efficiency = recipeManager.enhancer.displayEfficiency();
            dump(Items.lead);
        }

        @Override
        public void buildConfiguration(Table table) {
            recipeManager.config(table);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return recipeManager.enhancer.filterItems().contains(item) && items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return recipeManager.enhancer.filterLiquids().contains(liquid);
        }

        @Override
        public float heat() {
            return heat;
        }

        @Override
        public float heatFrac() {
            return heat / heatCapacity;
        }

        @Override
        public float[] sideHeat() {
            return sideHeat;
        }

        @Override
        public float heatRequirement() {
            return recipeManager.enhancer.heatIn();
        }

        @Override
        public float getPowerProduction() {
            return recipeManager.enhancer.powerOut();
        }
    }
}
