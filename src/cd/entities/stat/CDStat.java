package cd.entities.stat;

import mindustry.world.meta.*;

public class CDStat extends Stat {

    public final static Stat
    catalyzer = new Stat("catalyzer", StatCat.crafting);

    /** unnecessary */
    public CDStat(String name, StatCat category) {
        super(name, category);
    }
    
}
