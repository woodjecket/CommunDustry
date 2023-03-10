package cd.type.valence;

import arc.struct.*;
import mindustry.type.*;

//TODO 2dMap
public class ResultMap{
    public Seq<ItemStack> mapKeys;
    public boolean hasNegative;
    public Seq<ItemStack> negativeMapKeys;
    public int max;

    public ResultMap(int max){
        this(max, false);
    }

    public ResultMap(int max, boolean hasNegative){
        this.max = max;
        mapKeys = new Seq<>(max + 1);
        mapKeys.setSize(max + 1);
        this.hasNegative = hasNegative;
        if(hasNegative){
            negativeMapKeys = new Seq<>(max + 1);
            negativeMapKeys.setSize(max + 1);
        }
    }

    public void putResult(Item item, int[] range){
        for(int i : range){
            if(i > 0) mapKeys.set(i, new ItemStack(item, 1));
            else if(i < 0 && hasNegative) negativeMapKeys.set(Math.abs(i), new ItemStack(item, 1));
        }
    }

    public void putResult(Item item, int[] range, int[] amount){
        int index = 0;
        for(int i : range){
            if(i > 0) mapKeys.set(i, new ItemStack(item, amount[index]));
            else if(i < 0 && hasNegative) negativeMapKeys.set(Math.abs(i), new ItemStack(item, amount[index]));
            index++;
        }
    }

    public ItemStack getResult(int index){
        ItemStack result = null;
        if(index > 0 && index < mapKeys.size){
            result = mapKeys.get(index);
        }else if(index < 0 && hasNegative && index < negativeMapKeys.size){
            result = negativeMapKeys.get(Math.abs(index));
        }
        return result;
    }

}
