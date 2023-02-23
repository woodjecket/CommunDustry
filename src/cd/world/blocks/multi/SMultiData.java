package cd.world.blocks.multi;

import arc.struct.*;
import arc.util.*;
import cd.world.blocks.*;
import cd.world.component.*;
import mindustry.game.*;
import mindustry.game.Schematic.*;

/**Stores the data for multi-struct with schematics. A wrapper to access data with the interface*/
@Deprecated
public class SMultiData extends OMultiData{
    private Schematic data;

    public void valueOf(Object... v){
        data= (Schematic)v[0];
        convert();
    }

    private void convert(){
        Log.info(data.tiles);
        Stile main = data.tiles.find(s -> {
            Log.info(s.block+" "+s.x+" "+s.y+" "+s.rotation);
            return s.block instanceof IComp comp && comp.getComp(MainMultiComponent.class) != null;
        });
        int baseX = main.x,baseY=main.y;
        Seq<Object> param = new Seq<>();
        for(var t : data.tiles){
            if(t == main) continue;
            param.addAll(new RotatedBlock(t.block,t.rotation), t.x -baseX, t.y -baseY);
        }
        valueOf(param);
    }
}
