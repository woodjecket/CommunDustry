package cd.struct.recipe;

import arc.scene.Element;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import cd.ctype.ExtendContent;
import cd.ctype.ExtendContentType;
import cd.entities.RecipeEntity;
import cd.struct.recipe.product.ProductChanceItems;
import cd.struct.recipe.product.ProductItems;
import cd.struct.recipe.product.ProductLiquids;
import cd.struct.recipe.reactant.ReactantHeat;
import cd.struct.recipe.reactant.ReactantItems;
import cd.struct.recipe.reactant.ReactantLiquids;
import cd.struct.recipe.reactant.ReactantPower;
import cd.world.comp.recipe.AbstractRecipeManager;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.meta.Stats;

public class Recipe extends ExtendContent {
    public Seq<Product> products = new Seq<>();
    public Seq<Reactant> reactants = new Seq<>();
    public int craftTime = 60;
    public int maxParallel = 1;

    /** The potential input ItemStacks is used in recipe assigning*/
    public final Seq<ItemStack> potentialInputItems = new Seq<>(), potentialOutputItems = new Seq<>();
    public final Seq<LiquidStack> potentialInputLiquids = new Seq<>(), potentialOutputLiquids = new Seq<>();
    /** The potential input things is used to set up filterItems and filterLiquid.*/
    public final Seq<Item> potentialInputItem = new Seq<>(), potentialOutputItem = new Seq<>();
    public final Seq<Liquid> potentialInputLiquid = new Seq<>(), potentialOutputLiquid = new Seq<>();

    public float power, heat;

    @Override
    public ExtendContentType getExtendContentType() {
        return ExtendContentType.recipe;
    }

    public Recipe(String name) {
        super(name);
    }

    public void init() {
        products.each(product -> product.init(this));
        reactants.each(reactant -> reactant.init(this));
    }

    public RecipeEntity newEntity(AbstractRecipeManager manager) {
        var entity = new RecipeEntity();
        entity.recipe = this;
        entity.manager = manager;
        entity.progress = 0f;
        return entity;
    }

    public Element equation() {
        var equation = new Table();
        reactants.each(r -> equation.add(r.icon()));
        equation.image(Icon.right).size(28f);
        products.each(p -> equation.add(p.icon()));
        equation.getCells().each(c->c.margin(15f));
        equation.align(Align.center);
        return equation;
    }

    public boolean sufficient(Building building, int[] items) {
        var sufficient = building.items.has(potentialInputItems);
        for( var is: potentialInputItems){
            sufficient &= building.items.get(is.item) >= is.amount + items[is.item.id];
        }
        for( var ls: potentialInputLiquids){
            sufficient &= building.liquids.get(ls.liquid) >= ls.amount;
        }
        return sufficient;
    }

    public void setStats(Stats stats) {

    }

    public static class RecipeBuilder{
        private Seq<Product> products = new Seq<>();
        private Seq<Reactant> reactants = new Seq<>();
        private int craftTime = 60;
        private int maxParallel = 1;
        //TODO Only builder method exists, unused in code
        private Effect craftEffect, updateEffect;
        private float updateEffectChance, updateEffectSpread;
        private int[] liquidOutputDirections;

        public RecipeBuilder addProduct(Product product){
            products.add(product);
            return this;
        }
        public RecipeBuilder addReactants(Reactant reactant){
            reactants.add(reactant);
            return this;
        }

        public RecipeBuilder time(int craftTime){
            this.craftTime = craftTime;
            return this;
        }

        public RecipeBuilder parallel(int maxParallel){
            this.maxParallel = maxParallel;
            return this;
        }

        public RecipeBuilder craftEffect(Effect effect){
            this.craftEffect = effect;
            return this;
        }
        public RecipeBuilder updateEffect(Effect updateEffect){
            this.updateEffect = updateEffect;
            return this;
        }

        public RecipeBuilder updateEffectChance(float updateEffectChance){
            this.updateEffectChance = updateEffectChance;
            return this;
        }

        public RecipeBuilder updateEffectSpread(float updateEffectSpread){
            this.updateEffectSpread = updateEffectSpread;
            return this;
        }

        public RecipeBuilder liquidOutputDirections(int... liquidOutputDirections){
            this.liquidOutputDirections = liquidOutputDirections;
            return this;
        }

        public Recipe build(String name){
            var value = new Recipe(name);
            value.products = products;
            value.reactants = reactants;
            value.craftTime = craftTime;
            value.maxParallel = maxParallel;
            return value;
        }

        public RecipeBuilder itemsIn(Object... items){
            var stacks = new Seq<ItemStack>(items.length / 2);
            for(int i = 0; i < items.length; i += 2){
                stacks.add(new ItemStack((Item)items[i], ((Number)items[i + 1]).intValue()));
            }
            reactants.add(new ReactantItems(stacks));
            return this;
        }

        public RecipeBuilder liquidsIn(Object... items){
            var stacks = new Seq<LiquidStack>(items.length / 2);
            for(int i = 0; i < items.length; i += 2){
                stacks.add(new LiquidStack((Liquid)items[i], ((Number)items[i + 1]).floatValue()));
            }
            reactants.add(new ReactantLiquids(stacks));
            return this;
        }

        public RecipeBuilder itemsOut(Object... items){
            var stacks = new Seq<ItemStack>(items.length / 2);
            for(int i = 0; i < items.length; i += 2){
                stacks.add(new ItemStack((Item)items[i], ((Number)items[i + 1]).intValue()));
            }
            products.add(new ProductItems(stacks));
            return this;
        }

        public RecipeBuilder liquidsOut(Object... items){
            var stacks = new Seq<LiquidStack>(items.length / 2);
            for(int i = 0; i < items.length; i += 2){
                stacks.add(new LiquidStack((Liquid)items[i], ((Number)items[i + 1]).floatValue()));
            }
            products.add(new ProductLiquids(stacks));
            return this;
        }

        public RecipeBuilder powerIn(float power){
            reactants.add(new ReactantPower(power));
            return this;
        }

        public RecipeBuilder heatIn(float heat){
            reactants.add(new ReactantHeat(heat));
            return this;
        }

        public RecipeBuilder chanceItemOut(Object... items){
            var stacks = new Seq<ItemStack>(items.length / 2);
            for(int i = 0; i < items.length; i += 2){
                stacks.add(new ItemStack((Item)items[i], ((Number)items[i + 1]).intValue()));
            }
            products.add(new ProductChanceItems(stacks));
            return this;
        }
    }
}
