package cd.world.blocks.multi;

import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.world.*;

/** Stores the data for multi-struct. A wrapper to access data with the interface */
public class OMultiData implements IMultiData{
    private ObjectMap<Point2, RotatedBlock> data = new ObjectMap<>();
    private Eachable<Point2> eachable;


    @Override
    public RotatedBlock get(Point2 key){
        return data.get(key);
    }


    @Override
    public void valueOf(Object... v){
        for(int i = 0; i < v.length - 1; i = i + 3){
            if(v[i] instanceof RotatedBlock){
                data.put(new Point2((Integer)v[i + 1], (Integer)v[i + 2]).cpy(), (RotatedBlock)v[i]);
            }else if(v[i] instanceof Block){
                data.put(new Point2((Integer)v[i + 1], (Integer)v[i + 2]).cpy(), new RotatedBlock((Block)v[i]));
            }
        }
        eachable = data.keys().toSeq();
    }

    @Override
    public Eachable<Point2> getEachable(){
        return eachable;
    }

}
