package cd.world.blocks.multi.craft;

import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;

import java.util.concurrent.atomic.*;

import static mindustry.Vars.state;

@SuppressWarnings("FieldNotUsedInToString")
public class RecipePair{
    public static final RecipePair EMPTY_RECIPE_PAIR = new RecipePair(Recipe.EMPTY_RECIPE, Recipe.EMPTY_RECIPE);
    public Recipe in, out;
    public float craftTime;

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public int[] liquidOutputDirections = {-1};
    private UnlockableContent iconItem;

    public RecipePair(Recipe in, Recipe out){
        this.in = in;
        this.out = out;
    }

    public RecipePair(){
    }

    public UnlockableContent iconItem(){
        if(iconItem == null){
            if(out.items.any())iconItem = out.items.get(0).item;
            if(in.items.any())iconItem = in.items.get(0).item;
        }
        return iconItem;
    }

    public boolean isAllUnlocked(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        in.items.each(i -> !i.item.unlockedNow() &&
        !state.rules.hiddenBuildItems.contains(i.item) &&
        !i.item.isHidden(), i -> atomicBoolean.set(true));
        out.items.each(i -> !i.item.unlockedNow() &&
        !state.rules.hiddenBuildItems.contains(i.item) &&
        !i.item.isHidden(),i -> atomicBoolean.set(true));
        return atomicBoolean.get();
    }

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
