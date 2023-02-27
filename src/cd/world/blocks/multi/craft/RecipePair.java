package cd.world.blocks.multi.craft;

import mindustry.content.*;
import mindustry.entities.*;

@SuppressWarnings("FieldNotUsedInToString")
public class RecipePair{
    public static final RecipePair EMPTY_RECIPE_PAIR = new RecipePair(Recipe.EMPTY_RECIPE,Recipe.EMPTY_RECIPE);
    public Recipe in, out;
    public float craftTime;

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public int[] liquidOutputDirections = {-1};

    public RecipePair(Recipe in, Recipe out){
        this.in =in;
        this.out = out;
    }

    public RecipePair(){}

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        RecipePair that = (RecipePair)obj;

        if(!in.equals(that.in)) return false;
        if(craftTime != that.craftTime) return false;
        return out.equals(that.out);
    }

    @Override
    public int hashCode(){
        int result = in.hashCode();
        result = 4 * result + out.hashCode();
        result = 24 * result + Float.floatToIntBits(craftTime);
        return result;
    }

    @Override
    public String toString(){
        return "RecipePair{" +
        "in=" + in +
        ", out=" + out +
        ", craftTime=" + craftTime +
        '}';
    }
}
