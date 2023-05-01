package cd.world.blocks.multi.craft;

import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.consumers.*;

import java.util.*;

/** Stores data of recipe.*/
public class Recipe{
    public static final Recipe EMPTY_RECIPE = new Recipe();

    public Seq<ItemStack> items = new Seq<>();
    public Seq<LiquidStack> liquids = new Seq<>();
    public float power;
    public float heat;

    public Seq<Consume> consumers = new Seq<>();

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

    public void buildTable(Table table){
        boolean afterFirst = false;
        for(var item: items){
            if (afterFirst) table.add("+").fillX().pad(4);
            table.table(c -> {
                c.defaults().padLeft(3).fill();
                table.add(new ItemImage(item));
            }).fill();
            afterFirst = true;
        }
        for(var liquid: liquids){
            if (afterFirst) table.add("+").fillX().pad(4);
            table.table(c -> {
                c.defaults().padLeft(3).fill();
                table.add(new ItemImage(liquid.liquid.uiIcon,0));
            }).fill();
            afterFirst = true;
        }
        if(power!=0f)table.add(new ItemImage(Icon.power.getRegion(), (int)(power*60f)));
        if(heat!=0f)table.add(new ItemImage(Icon.waves.getRegion(), (int)(heat)));
    }
}
