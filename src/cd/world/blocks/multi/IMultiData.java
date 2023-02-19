package cd.world.blocks.multi;

import arc.math.geom.*;
import arc.util.*;
import mindustry.world.*;

/**
 * The interface for multi-struct data
 */
public interface IMultiData{
    Block getByOffsetPos(int ox, int oy);

    Block getByOffsetPos(Point2 o);

    void valueOf(Object... v);

    Eachable<Point2> getEachable();
}
