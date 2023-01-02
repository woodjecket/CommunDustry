package cd.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class CDItems {
    public static Item cerium;

    public void load(){
        cerium = new Item("cerium",Color.valueOf("fad228")){{
            flammability = 0.6f;
            explosiveness = 1.0f;
        }};
    }
}
