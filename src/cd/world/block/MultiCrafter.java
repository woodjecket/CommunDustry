package cd.world.block;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.util.io.Reads;
import arc.util.io.Writes;
import cd.struct.recipe.Recipe;
import cd.world.comp.IHeat;
import cd.world.comp.IRecipeManager;
import cd.world.comp.recipe.AbstractRecipeManager;
import cd.world.comp.recipe.RecipeManagerFactory;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatConsumer;
import mindustry.world.meta.BlockFlag;

public class MultiCrafter extends Block {
    public RecipeManagerFactory recipes = new RecipeManagerFactory();

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
        consumePowerDynamic((MultiCrafterBuild b) -> b.recipeManager.enhancer.powerIn());
        recipes.registerConfig(this);
    }

    @Override
    public void init() {
        super.init();
        recipes.recipes.each(Recipe::init);
    }

    public class MultiCrafterBuild extends Building implements IHeat, HeatConsumer, IRecipeManager {
        public AbstractRecipeManager recipeManager = recipes.newManager(this);
        public float heat;
        public float[] sideHeat = new float[4];

        @Override
        public void updateTile() {
            super.updateTile();
            heat = calculateHeat(sideHeat);

            recipeManager.update();

            if (recipeManager.enhancer.heatOut() > 0) {
                heat = Mathf.approachDelta(heat, recipeManager.enhancer.heatOut() * efficiency, 10 * delta());
            }

            efficiency = recipeManager.enhancer.displayEfficiency();
            recipeManager.enhancer.assistDump(this);
        }

        @Override
        public void buildConfiguration(Table table) {
            recipeManager.buildConfigure(table);
        }

        @Override
        public Object config() {
            return recipeManager.getConfig();
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

        @Override
        public byte version() {
            // Recipe: 2
            return 2;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            recipeManager.write(write);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            if (revision >= 2) recipeManager.read(read);
        }

        @Override
        public AbstractRecipeManager manager() {
            return recipeManager;
        }
    }
}
