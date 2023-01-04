package cd.type.valence;

import arc.struct.Seq;
import mindustry.type.Item;

//TODO 2dMap
public class ResultMap {
    public Seq<Item> mapKeys;
    public boolean hasNegative;
    private Seq<Item> negativeMapKeys;
    public ResultMap(int max){
        this(max,false);
    }
    public ResultMap(int max,boolean hasNegative){
        mapKeys = new Seq<>(max + 1);
        mapKeys.setSize(max+1);
        this.hasNegative = hasNegative;
        if(hasNegative){
            negativeMapKeys = new Seq<>(max + 1);
            negativeMapKeys.setSize(max+1);
        }
    }
    public void putResult(Item item,int[] range){
        for (int i : range){
            if(i > 0) mapKeys.set(i,item);
            else if (i < 0&&hasNegative) negativeMapKeys.set(Math.abs(i),item);
        }
    }
    public Item getResult(int index){
        Item result = null;
        if (index > 0 && index < mapKeys.size){
            result = mapKeys.get(index);
        } else if (index < 0 && hasNegative && index < negativeMapKeys.size){
            result =negativeMapKeys.get(Math.abs(index));
        }
        return result;
    }
}
