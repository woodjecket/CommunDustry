package cd.struct.recipe.reactant;

import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import mindustry.gen.Building;

public class ReactantPower extends Reactant {

    public float power;

    public ReactantPower(float power) {
        this.power = power;
    }


    @Override
    public void init(Recipe recipe) {
        recipe.power -= power;
    }

    @Override
    public float efficiency(Building building) {
        return building.power.status;
    }
}
