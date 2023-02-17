package cd.type.valence;

import arc.struct.ObjectMap;
import mindustry.type.Item;

public class ItemValenceMap {
    private ObjectMap<Item, ItemValence> map = new ObjectMap<>();

    public ItemValence get(Item item) {
        return map.get(item);
    }
}
