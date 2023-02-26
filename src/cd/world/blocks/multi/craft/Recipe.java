package cd.world.blocks.multi.craft;

import arc.struct.*;
import mindustry.type.*;
import mindustry.world.consumers.*;

import java.util.*;

/** Stores data of recipe.*/
public class Recipe{
    public static final Recipe EMPTY_RECIPE = new Recipe();
    @SuppressWarnings("FieldNotUsedInToString")
    public RecipePair parent;

    public Seq<ItemStack> items = new Seq<>();
    public Seq<LiquidStack> liquids = new Seq<>();
    public float power;
    public float heat;

    public Seq<Consume> consumers;

    //For Commundustry

    public float pressure;
    public float laser;

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Recipe recipe = (Recipe)obj;

        if(Float.compare(recipe.power, power) != 0) return false;
        if(Float.compare(recipe.heat, heat) != 0) return false;
        if(Float.compare(recipe.pressure, pressure) != 0) return false;
        if(Float.compare(recipe.laser, laser) != 0) return false;
        if(!Objects.equals(items, recipe.items)) return false;
        if(!Objects.equals(consumers, recipe.consumers)) return false;
        return Objects.equals(liquids, recipe.liquids);
    }

    @Override
    public int hashCode(){
        int result = items != null ? items.hashCode() : 0;
        result = 11 * result + (liquids != null ? liquids.hashCode() : 0);
        result = 45 * result + (power == 0.0f ? 0 : Float.floatToIntBits(power));
        result = 14 * result + (heat == 0.0f ? 0 : Float.floatToIntBits(heat));
        result = 19 * result + (pressure == 0.0f ? 0 : Float.floatToIntBits(pressure));
        result = 19 * result + (laser == 0.0f ? 0 : Float.floatToIntBits(laser));
        result = 910 * result + (consumers != null ? consumers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Recipe{" +
        "items=" + items +
        ", liquids=" + liquids +
        ", consumer=" + consumers +
        ", power=" + power +
        ", heat=" + heat +
        ", pressure=" + pressure +
        ", laser=" + laser +
        ", object=" + super.toString() +
        '}';
    }
}
