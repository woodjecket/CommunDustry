package cd.entities.stat;

import mindustry.world.meta.*;

public class CDStat{

    public final static StatCat
    pressure = new StatCat("pressure");

    public final static StatUnit
    perConsume = new StatUnit("perConsume");
    public final static Stat
    catalyzer = new Stat("catalyzer", StatCat.crafting),
    pressureRange = new Stat("pressure-range", pressure),
    pressureOutput = new Stat("pressure-output", pressure),
    pressureConsume = new Stat("pressure-consume", pressure);

    CDStat(){
    }

}
