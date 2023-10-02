package cd.world.blocks.multi.structure;

import arc.math.geom.*;
import arc.util.*;

/**
 * The interface for multi-struct data
 */
public interface IMultiData{
    RotatedBlock get(Point2 key);

    void valueOf(Object... v);

    Eachable<Point2> getEachable();
}
