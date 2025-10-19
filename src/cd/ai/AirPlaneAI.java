package cd.ai;

import cd.entities.vehicle.Airplane.*;
import mindustry.*;
import mindustry.entities.units.*;

public class AirPlaneAI extends AIController{
    @Override
    public void updateMovement(){
        super.updateMovement();
        if(unit instanceof AirPlaneUnit aero){
            switch(aero.airPlaneState){
                case land, global -> {
                }
                case takeoff -> moveTo(Vars.world.tile(0,0),unit.hitSize);
                case landing -> moveTo(aero.destB, 0);
            }
        }
    }

    @Override
    public void updateUnit(){
        super.updateUnit();
    }

}
