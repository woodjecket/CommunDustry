package cd.entities.building;

import arc.math.geom.*;
import arc.struct.*;
import cd.world.blocks.multi.MultiStructPort.*;

public interface IMulti{
    /**
     * add a port,do not be more than one
     */
    void addPorts(MultiStructPortBuild b, Point2 p);

    Seq<MultiStructPortBuild> getPorts();
}
