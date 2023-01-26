package cd.world.stat;

import mindustry.world.meta.*;

public class CDStat{

    public final static StatCat
    industry = new StatCat("industry");

    public final static StatUnit
    perConsume = new StatUnit("perConsume");
    public final static Stat
    catalyzer = new Stat("catalyzer", StatCat.crafting),
    pressureRange = new Stat("pressure-range", industry),
    pressureOutput = new Stat("pressure-output", industry),
    pressureConsume = new Stat("pressure-consume", industry),

    maxLaser = new Stat("max-laser", industry),
    laserOutput = new Stat("laser-output", industry),
    laserConsume = new Stat("laser-consume", industry);

    CDStat(){
    }

}
