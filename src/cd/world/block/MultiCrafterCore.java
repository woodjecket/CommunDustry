package cd.world.block;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import cd.struct.recipe.Recipe;
import cd.world.comp.IHeat;
import cd.world.comp.IRecipeManager;
import cd.world.comp.recipe.AbstractRecipeManager;
import cd.world.comp.recipe.MultiRecipeManager;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.heat.HeatConsumer;
import mindustry.world.blocks.storage.CoreBlock;

/**
 * Peculiar core crafter, but multi-selected
 */
public class MultiCrafterCore extends CoreBlock {
    public AbstractRecipeManager.AbstractRecipeManagerFactory factory = new MultiRecipeManager.MultiRecipeManagerFactory();

    public MultiCrafterCore(String name) {
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        drawArrow = false;
        configurable = true;
        hasLiquids = true;
        outputsLiquid = true;
        hasPower = true;
        liquidCapacity = 100f;
        //to let power generate
        outputsPower = consumesPower = true;
        consumePowerDynamic((MultiCrafterCoreBuild b) -> b.recipeManager.enhancer.powerIn());
        factory.registerConfig(this);
    }

    @Override
    public void init() {
        super.init();
        factory.recipes.each(Recipe::init);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        //For test purpose
        return true;
    }

    public class MultiCrafterCoreBuild extends CoreBuild implements IHeat, HeatConsumer, IRecipeManager {
        public AbstractRecipeManager recipeManager = factory.newManager(this);
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
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return recipeManager.enhancer.filterLiquids().contains(liquid);
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

