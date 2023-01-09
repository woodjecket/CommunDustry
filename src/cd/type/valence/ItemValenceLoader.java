package cd.type.valence;

import cd.content.CDItems;
import mindustry.content.Items;

import static cd.type.valence.ItemsValence.*;

public class ItemValenceLoader {
    public static void load(){
        setValence(Items.silicon, 0);
        setValence(Items.graphite, new ValenceFunc(module -> module.formulaBefore.has(Items.silicon) ? 3 : 4));
        set(Items.titanium, new ValenceFunc(m -> 3), new ValenceFunc(m -> m.itemValence / 2));
        set(CDItems.cerium, new ValenceFunc(m -> 3), new ValenceFunc(m -> m.itemValence + 3));
        setValence(Items.copper, 2);
        set(Items.thorium, new ValenceFunc(m -> -4), new ValenceFunc(m -> m.itemValence * 3 + 3));
        setValence(Items.sand, -2);
        set(Items.coal, new ValenceFunc(m -> 2), new ValenceFunc(m -> m.itemValence - 1));
    }
}
