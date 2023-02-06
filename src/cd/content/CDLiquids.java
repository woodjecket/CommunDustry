package cd.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;

public class CDLiquids{
    public static Liquid fluorine, H2O2, chlorine, ClF3;

    public static void load(){
        fluorine = new Liquid("fluorine", Color.valueOf("9ec42b")){{
            gas = true;
            flammability = 1.0f;
            barColor = gasColor = Color.valueOf("ccff33");
        }};
        chlorine = new Liquid("chlorine", Color.valueOf("9acd32")){{
            gas = true;
            flammability = 1.0f;
            barColor = gasColor = Color.valueOf("87ff2a");
        }};
        H2O2 = new Liquid("h2o2", Color.valueOf("a1caf1")){{
            barColor = Color.valueOf("7666c6");
            viscosity = 0.7f;
            coolant = false;
            effect = StatusEffects.sapped;
            particleEffect = CDFx.iceCraft;
        }};
        ClF3 = new Liquid("clf3", Color.valueOf("39ff14")){{
            gas = true;
            barColor = gasColor = Color.valueOf("ffeeee88");
            explosiveness = 0.7f;
        }};
    }
}