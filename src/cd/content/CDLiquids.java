package cd.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;

import static mindustry.content.Liquids.water;

public class CDLiquids{
    public static Liquid fluorine, H2O2, chlorine, ClF3,
    petrol,kerosene,diesel,lubricatingOil;

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

        petrol = new Liquid("petrol"){{
            viscosity = 0.2f;
            explosiveness = 1.7f;
            flammability = 1.7f;
            boilPoint = 0.8f;
            canStayOn.add(water);
        }};
        kerosene = new Liquid("kerosene"){{
            viscosity = 0.3f;
            explosiveness = 0.1f;
            flammability = 2.0f;
            boilPoint = 1f;
            canStayOn.add(water);
        }};
        diesel = new Liquid("diesel-oil"){{
            viscosity = 1.2f;
            explosiveness = 1.6f;
            flammability = 2.1f;
            boilPoint = 1.2f;
            canStayOn.add(water);
        }};
        lubricatingOil = new Liquid("lubricating-oil"){{
            viscosity = 0f;
            explosiveness = 0f;
            flammability = 0f;
            boilPoint = 2f;
            canStayOn.add(water);
        }};
    }
}