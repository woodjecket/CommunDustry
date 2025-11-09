package cd.entities;

import arc.util.Strings;
import cd.struct.recipe.Recipe;
import cd.world.comp.RecipeManager;

public class RecipeEntity {
    public RecipeManager manager;
    public Recipe recipe;
    public float progress;

    public float totalEfficiency(){
        float efficiency = 1f;
        for(var reactant: recipe.reactants){
            efficiency *= reactant.efficiency(manager.building);
        }
        return efficiency;
    }

    public float totalEfficiencyMultiplier(){
        float efficiencyMultiplier = 1f;
        for(var reactant: recipe.reactants){
            efficiencyMultiplier *= reactant.efficiencyMultiplier(manager.building);
        }
        return efficiencyMultiplier;
    }

    public boolean noOutput() {
        for(var product: recipe.products){
            if(!product.canOutput(manager.building)) return true;
        }
        return false;
    }

    public void runWhile(float efficiency){
        for(var reactant: recipe.reactants){
            reactant.reactWhile(manager.building,efficiency);
        }
        for(var product: recipe.products){
            product.produceWhile(manager.building,efficiency);
        }
    }

    public void runOnce() {
        for(var reactant: recipe.reactants){
            reactant.reactOnce(manager.building);
        }
        for(var product: recipe.products){
            product.produceOnce(manager.building);
        }
    }

    public String toString(){
        return recipe + ",progress=" + Strings.autoFixed(progress,3);
    }
}

