package cd.world.comp;

import cd.world.comp.recipe.AbstractRecipeManager;
import cd.world.comp.recipe.AbstractRecipeManager.AbstractRecipeManagerFactory;
import mindustry.gen.Building;
import mindustry.world.Block;

/** The following methods should be overridden to work with recipe manager:
 * <ul>
 * <li> The manager should be created by a {@link AbstractRecipeManagerFactory#newManager(Building)}
 * <li> The recipe in {@link AbstractRecipeManagerFactory#recipes} has better be initialized in {@link Block#init()}
 * <li> `recipeManager.update();` `efficiency = recipeManager.enhancer.displayEfficiency();` `recipeManager.enhancer.assistDump(this);` in {@link Building#updateTile()} are required
 * <li> `buildConfiguration()` `config()` `acceptItem()` `acceptLiquid()` `write()` `read()` must be overridden
 * </ul>*/

public interface IRecipeManager{
    AbstractRecipeManager manager();
}
