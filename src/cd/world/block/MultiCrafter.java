package cd.world.block;

import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.Seq;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.struct.recipe.product.ProductItems;
import cd.struct.recipe.reactant.ReactantItems;
import cd.struct.recipe.reactant.ReactantLiquids;
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
import mindustry.world.meta.BlockFlag;

public class MultiCrafter extends Block {
    public Recipes recipes = new Recipes(){{
        recipes.add(new Recipe(){{
            reactants.add(new ReactantItems(new Seq<>(ItemStack.with(Items.copper,1))),
                    new ReactantLiquids(new Seq<>(LiquidStack.with(Liquids.water,0.1f)))
            );
            products.add(new ProductItems(new Seq<>(ItemStack.with(Items.lead,1))));
        }});
    }};
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
    }
    public class MultiCrafterBuild extends Building{
        public RecipeManager recipeManager = recipes.newManager(this);
        @Override
        public void updateTile() {
            super.updateTile();
            recipeManager.update();
            dump(Items.lead);
        }

        @Override
        public void buildConfiguration(Table table) {
            recipeManager.config(table);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return true;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return true;
        }
    }
}
