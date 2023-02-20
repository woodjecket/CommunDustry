package cd.entities.building;

import arc.struct.*;
import cd.world.blocks.multi.MultiStructPort.*;

public interface IMulti{
    /**
     * add a port,do not be more than one
     */
    void addPorts(MultiStructPortBuild b);

    Seq<MultiStructPortBuild> getPorts();
}
