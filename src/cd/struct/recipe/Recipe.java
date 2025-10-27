package cd.struct.recipe;

import arc.struct.Seq;
import cd.entities.RecipeEntity;
import cd.world.comp.RecipeManager;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;

public class Recipe {
    public Seq<Product> products = new Seq<>();
    public Seq<Reactant> reactants = new Seq<>();
    public int craftTime = 60;

    public final Seq<ItemStack> potentialInputItems = new Seq<>(), potentialOutputItems = new Seq<>();
    public final Seq<LiquidStack> potentialInputLiquids = new Seq<>(), potentialOutputLiquids = new Seq<>();
    public final Seq<Item> potentialInputItem = new Seq<>(), potentialOutputItem = new Seq<>();
    public final Seq<Liquid> potentialInputLiquid = new Seq<>(), potentialOutputLiquid = new Seq<>();

    public float power, heat;

    public void init() {
        products.each(product -> {
            product.init(this);
        });
        reactants.each(reactant -> reactant.init(this));
    }

    public RecipeEntity newEntity(RecipeManager manager) {
        var entity = new RecipeEntity();
        entity.recipe = this;
        entity.manager = manager;
        entity.progress = 0f;
        return entity;
    }

}
