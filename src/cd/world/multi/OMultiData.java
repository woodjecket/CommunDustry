package cd.world.multi;

import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.world.*;

/** Stores the data for multi-struct. A wrapper to access data with the interface */
public class OMultiData implements IMultiData{
    private ObjectMap<Point2, Block> data = new ObjectMap<>();

    @Override
    public Block getByOffsetPos(int ox, int oy){
        return data.get(new Point2(ox, oy));
    }

    @Override
    public Block getByOffsetPos(Point2 o){
        return data.get(new Point2(o.x, o.y));
    }

    @Override
    public void valueOf(Object... v){
        for(int i = 0; i < v.length - 1; i = i + 3){
            data.put(
            new Point2((Integer)v[i + 1], (Integer)v[i + 2]),
            (Block)v[i]);
        }
    }

    @Override
    public Eachable<Point2> getEachable(){
        return data.keys().toSeq();
    }

}
