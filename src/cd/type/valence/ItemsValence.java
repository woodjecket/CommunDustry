package cd.type.valence;

import arc.func.*;
import arc.struct.*;
import mindustry.type.*;
import mindustry.world.modules.*;

public class ItemsValence{
    private static final ObjectMap<Item, Func<ItemModule, Integer>> valenceMap = new ObjectMap<>();

    public static void setValence(Item item, Func<ItemModule, Integer> valence){
        valenceMap.put(item, valence);
    }

    public static void setValence(Item item, int valence){
        setValence(item, (a) -> valence);
    }

    public static int getValence(Item item, ItemModule itemModule){
        return hasValence(item) ? valenceMap.get(item).get(itemModule) : 0;
    }

    public static boolean hasValence(Item item){
        return valenceMap.containsKey(item);
    }
}
