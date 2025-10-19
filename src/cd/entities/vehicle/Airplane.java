package cd.entities.vehicle;

import arc.util.*;
import cd.ai.*;
import cd.content.*;
import cd.entities.*;
import cd.world.block.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.modules.*;

public class Airplane implements GlobalEntity{
    public Sector from, to;
    public float x, y, z;
    public float flightTime = 60f;
    public float timer = 0f;
    public Team team = Team.sharded;
    public UnitType type = CDUnitTYpe.unitType;
    public ItemModule items;

    @Override
    public void update(){
        timer++;
        Log.info("You");
        if(timer >= flightTime) enterSector();
    }

    private void enterSector(){
        if(Vars.state.getSector() == to){
            Log.info("You get it");
            comvertToSector();
        }else{
            Log.info("You miss it");
        }
        remove();
    }

    private void remove(){
        GlobalEntities.entities.remove(this);
    }

    private void comvertToSector(){
        var air = AirPlaneUnit.create();
        air.x = 0;
        air.y = 0;
        air.team = team;
        air.elevation = 1;
        air.heal();
        air.airPlaneState = AirPlaneState.landing;
        air.destB = getDestination();
        air.controller().unit(air);
        air.itemModule = items;
        air.add();
    }

    private Building getDestination(){
        Log.info(Vars.indexer.findTile(team, 0, 0, 1000, building -> building.block instanceof Airport));
        return Vars.indexer.findTile(team, 0, 0, 1000, building -> building.block instanceof Airport);
    }

    public static enum AirPlaneState{
        takeoff, land, landing, global
    }

    public static class AirPlaneUnit extends UnitEntity{
        public AirPlaneState airPlaneState = AirPlaneState.land;
        @Nullable
        public Building destB;
        public Sector destS;
        public ItemModule itemModule;

        {
            controller = new AirPlaneAI();
        }

        private Airplane convertToGlobal(){
            var plane = new Airplane();
            itemModule.add(stack.item,stack.amount);
            plane.items = itemModule;
            return plane;
        }

        public static AirPlaneUnit create(){
            return new AirPlaneUnit();
        }

        @Override
        public void update(){
            super.update();
            if(dst(0, 0) < 2 * hitSize) enterPlanet();
        }

        private void enterPlanet(){
            var sector = Vars.state.getSector();
            GlobalEntities.entities.add(convertToGlobal());
            remove();
        }
    }
}
