package cd.content;

import cd.entities.vehicle.Airplane.*;
import mindustry.type.*;

public class CDUnitTYpe{
    public static UnitType unitType = new UnitType("airplane"){{
        constructor = AirPlaneUnit::create;
        speed = 10f;
    }};

    public static void load(){

    }
}
