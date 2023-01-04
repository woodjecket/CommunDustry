package cd.content;

import arc.graphics.Color;
import mindustry.content.StatusEffects;
import mindustry.type.Liquid;

public class CDLiquids {
    public static Liquid fluorine, hydrogenPeroxide;

    public void load(){
        fluorine = new Liquid("fluorine",Color.valueOf("806532")){{
            gas = true;
            flammability = 1.0f;
            gasColor = Color.valueOf("ccff33");
        }};
        hydrogenPeroxide = new Liquid("hydrogen-peroxid",Color.valueOf("a1caf1")){{
            barColor = Color.valueOf("7666c6");
            viscosity = 0.7f;
            coolant = false;
            effect = StatusEffects.sapped;
            particleEffect = CDFx.iceCraft;
        
        }};
        }
}