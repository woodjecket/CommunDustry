package cd.world.blocks.multi;

import arc.math.geom.*;
import arc.util.*;

/**
 * The interface for multi-struct data
 */
public interface IMultiData{
    RotatedBlock getByOffsetPos(Point2 o);

    RotatedBlock getByPosRotation(Point2 o, int rotation);

    void valueOf(Object... v);

    Eachable<Point2> getEachable();
}
