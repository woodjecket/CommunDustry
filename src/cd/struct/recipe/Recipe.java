package cd.struct.recipe;

import arc.struct.Seq;
import cd.entities.RecipeEntity;
import cd.world.comp.RecipeManager;

public class Recipe {
    public Seq<Product> products = new Seq<>();
    public Seq<Reactant> reactants = new Seq<>();
    public int craftTime = 60;

    public RecipeEntity newEntity(RecipeManager manager){
        var entity = new RecipeEntity();
        entity.recipe = this;
        entity.manager = manager;
        entity.progress = 0f;
        return entity;
    }
}
