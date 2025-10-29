package cd.struct.recipe.reactant;

import arc.util.Log;
import cd.struct.recipe.Reactant;
import cd.struct.recipe.Recipe;
import cd.world.block.IHeat;
import mindustry.gen.Building;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.blocks.heat.HeatConsumer;

public class ReactantHeat extends Reactant {
    public float heat;

    public ReactantHeat(float heat) {
        this.heat = heat;
    }


    @Override
    public void init(Recipe recipe) {
        recipe.heat -= heat;
    }

    @Override
    public float efficiency(Building building) {
        if(building instanceof IHeat heatBlock && building instanceof HeatConsumer heatConsumer){
            return Math.min(heatBlock.heat() / (heatConsumer.heatRequirement() + 0.0001f), 1f);
        }else {
            return 1f;
        }
    }
}
