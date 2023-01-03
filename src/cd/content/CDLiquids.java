package cd.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class CDLiquids {
    public static Liquid fluorine;

    public void load(){
        fluorine = new Liquid("fluorine"){{
            gas = true;
            flammability = 1.0f;
            color = Color.valueOf("806532");
            gasColor = Color.valueOf("ccff33");
        }};
    }
}