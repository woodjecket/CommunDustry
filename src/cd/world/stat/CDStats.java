package cd.world.stat;

import mindustry.world.meta.*;

public final class CDStats{

    public static final StatCat
    industry = new StatCat("industry");

    public static final StatUnit
    perConsume = new StatUnit("perConsume");
    public static final Stat
    catalyzer = new Stat("catalyzer", StatCat.crafting),
    pressureRange = new Stat("pressure-range", industry),
    pressureOutput = new Stat("pressure-output", industry),
    pressureConsume = new Stat("pressure-consume", industry),

    maxLaser = new Stat("max-laser", industry),
    laserOutput = new Stat("laser-output", industry),
    laserConsume = new Stat("laser-consume", industry),

    recipes = new Stat("recipe", industry);


}
