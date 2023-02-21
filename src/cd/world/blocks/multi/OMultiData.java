package cd.world.blocks.multi;

import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.world.*;

/** Stores the data for multi-struct. A wrapper to access data with the interface */
public class OMultiData implements IMultiData{
    private ObjectMap<Point2, RotatedBlock> data = new ObjectMap<>();


    @Override
    public RotatedBlock getByOffsetPos(Point2 o){
        return data.get(new Point2(o.x, o.y));
    }

    @Override
    public RotatedBlock getByPosRotation(Point2 o, int rotation){
        Point2 rotated = o.cpy().rotate(-rotation);
        Log.info("tried to access (@,@), full is @",rotated.x,rotated.y,data.toString());
        return getByOffsetPos(rotated);
    }

    @Override
    public void valueOf(Object... v){
        for(int i = 0; i < v.length - 1; i = i + 3){
            if(v[i] instanceof RotatedBlock){
                data.put(new Point2((Integer)v[i + 1], (Integer)v[i + 2]), (RotatedBlock)v[i]);
            }else if(v[i] instanceof Block){
                data.put(new Point2((Integer)v[i + 1], (Integer)v[i + 2]), new RotatedBlock((Block)v[i]));
            }
        }
    }

    @Override
    public Eachable<Point2> getEachable(){
        return data.keys().toSeq();
    }

}
