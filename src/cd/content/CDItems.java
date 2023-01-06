package cd.content;

import arc.graphics.*;
import mindustry.type.*;

public class CDItems{
    public static Item cerium, platinum, ice;

    public void load(){
        //Color from Ce4+ 
        cerium = new Item("cerium", Color.valueOf("fad228")){{
            flammability = 0.6f;
            explosiveness = 1.0f;
        }};
        //Color from H2Cl6Pt
        platinum = new Item("platinum", Color.valueOf("9b132f")){{
            cost = 2f;
        }};
        ice = new Item("ice", Color.valueOf("80b3c4"));
    }
}