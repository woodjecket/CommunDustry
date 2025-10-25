package cd.struct.recipe;

import arc.struct.Seq;
import mindustry.ctype.Content;
import mindustry.gen.Building;

public class Reactant {
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
    public Seq<Content> potentialReactant(){
        return null;
    }
}
