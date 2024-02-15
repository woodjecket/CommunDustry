package cd.entities.unit;

import arc.struct.*;
import cd.type.unit.*;
import mindustry.gen.*;
import mindustry.world.*;

public class BlockOnUnit extends TankUnit{
    public boolean toStop;
    public boolean stopped;
    private int size = ((BlockOnUnitType)type).size;
    public Tiles tiles = new Tiles(size, size + 1);
    public Seq<Building> builds = new Seq<>(size * (size + 1));

    protected BlockOnUnit(){
    }

    @Override
    public void update(){
        super.update();
        builds.each(Building::update);
    }
}
