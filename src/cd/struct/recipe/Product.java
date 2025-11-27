package cd.struct.recipe;

import arc.scene.Element;
import mindustry.gen.Building;

/** Indicates a product of a reaction.*/
public class Product {
    /** Set up the Recipe, rather than Consume#init() to set up a block */
    public void init(Recipe recipe){

    }
    public void produceOnce(Building building){

    }
    public void produceWhile(Building building, float efficiency){

    }
    public boolean canOutput(Building building){
        return true;
    }

    public Element[] icon(){
        return null;
    }
}
