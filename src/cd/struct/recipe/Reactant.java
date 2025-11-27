package cd.struct.recipe;

import arc.scene.Element;
import mindustry.gen.Building;

public class Reactant {
    /** Set up the Recipe, rather than Consume#init() to set up a block */
    public void init(Recipe recipe){

    }
    public void reactOnce(Building building){

    }
    public void reactWhile(Building building, float efficiency){

    }
    public float efficiency(Building building){
        return 1f;
    }
    public float efficiencyMultiplier(Building building){
        return 1f;
    }
    public Element[] icon(){
        return null;
    }
}
