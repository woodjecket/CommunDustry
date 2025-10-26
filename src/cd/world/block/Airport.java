package cd.world.block;

import cd.content.*;
import cd.entities.vehicle.Airplane.*;
import mindustry.gen.*;
import mindustry.world.*;

public class Airport extends Block{
    public Airport(String name){
        super(name);
        update = true;
        alwaysUnlocked = true;
    }

    public class AirportBuilidng extends Building{
        public AirPlaneUnit unit;

        @Override
        public void tapped(){
            super.tapped();
            if(unit != null){
                unit.airPlaneState = AirPlaneState.takeoff;
            }
        }

        @Override
        public void placed(){
            super.placed();
            unit = (AirPlaneUnit)CDUnitTYpe.unitType.spawn(this);
            unit.controller().unit(unit);
            unit.airPlaneState = AirPlaneState.land;
        }
    }

}
