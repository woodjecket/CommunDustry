package cd.entities;

import arc.graphics.Color;
import arc.util.Strings;
import arc.util.Tmp;
import cd.struct.recipe.Recipe;
import cd.world.comp.recipe.AbstractRecipeManager;
import mindustry.graphics.Pal;

public class RecipeSlot {
    public AbstractRecipeManager manager;
    public Recipe recipe;
    public int index;
    public float progress;

    //region slot
    public RecipeSlot(AbstractRecipeManager manager, Recipe recipe, int index) {
        this.manager = manager;
        this.recipe = recipe;
        this.index = index;
        manager.count.increment(recipe);
        recipe.potentialInputItems.each(s -> manager.items[s.item.id] += s.amount);
        progress = 0f;
    }

    public void update() {
        var efficiency = totalEfficiency() * totalEfficiencyMultiplier();
        if (!noOutput()) {
            runWhile(efficiency);
        }
        progress += manager.building.delta() * efficiency / recipe.craftTime;
        if (progress >= 1f) {
            if (noOutput()) {
                progress -= manager.building.delta() * efficiency / recipe.craftTime;
            } else {
                runOnce();
                passivePop();
            }

        }
    }

    public void passivePop() {
        manager.slots[index] = null;
        manager.count.increment(recipe, -1);
        recipe.potentialInputItems.each(s -> manager.items[s.item.id] -= s.amount);
    }

    public void activePop() {
        manager.building.configure(Float.valueOf(index));
    }
    //endregion
    
    //region entity
    public float totalEfficiency() {
        float efficiency = 1f;
        for (var reactant : recipe.reactants) {
            efficiency *= reactant.efficiency(manager.building);
        }
        return efficiency;
    }

    public float totalEfficiencyMultiplier() {
        float efficiencyMultiplier = 1f;
        for (var reactant : recipe.reactants) {
            efficiencyMultiplier *= reactant.efficiencyMultiplier(manager.building);
        }
        return efficiencyMultiplier;
    }

    public boolean noOutput() {
        for (var product : recipe.products) {
            if (!product.canOutput(manager.building)) return true;
        }
        return false;
    }

    public void runWhile(float efficiency) {
        for (var reactant : recipe.reactants) {
            reactant.reactWhile(manager.building, efficiency);
        }
        for (var product : recipe.products) {
            product.produceWhile(manager.building, efficiency);
        }
    }

    public void runOnce() {
        for (var reactant : recipe.reactants) {
            reactant.reactOnce(manager.building);
        }
        for (var product : recipe.products) {
            product.produceOnce(manager.building);
        }
    }

    public String toString() {
        return recipe + ",progress=" + Strings.autoFixed(progress, 3);
    }

    public Color getColor() {
        if (noOutput()) return Pal.techBlue;
        return Tmp.c1.set(Color.scarlet).lerp(Pal.accent, Math.min(totalEfficiency() * totalEfficiencyMultiplier(), 1f));
    }
    //endregion
}
