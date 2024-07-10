package cd.map;

import arc.Events;
import arc.util.noise.Simplex;
import cd.io.DeepVeinCustomChunk;
import cd.map.dv.data.DeepVeinPosition;
import cd.type.OreInfo;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.world.Tiles;

public class SingleMapDeepVeinGenerator implements IDeepVeinGenerator {
    public int seed;
    public DeepVeinPosition walkTile(int x, int y){
        var noise1 =  Simplex.noise2d(seed * 2,8,1,1,x,y);
        var noise2 =  Simplex.noise2d(seed * 3,8,1,1,x,y);
        var noise3 =  Simplex.noise2d(seed * 4,8,1,1,x,y);
        OreInfo o1 = null,o2 = null,o3 = null;
       if(noise1 < 0.15f) o1 = OreInfo.t1;
       if(noise2 <0.5f && noise2>0.3f) o2 =OreInfo.t2;
       if(noise3 > 0.7f) o3 =OreInfo.t3;
       return new DeepVeinPosition(x,y,o1,o2,o3);
    }

    public void generate(Tiles tiles){
        for(var tile:tiles){
            DeepVeinCustomChunk.infos.put(tile.pos(),walkTile(tile.x, tile.y));
        }
    }

    static{
        Events.on(EventType.WorldLoadEvent.class ,e->{
            if(DeepVeinCustomChunk.infos.isEmpty()){
                new SingleMapDeepVeinGenerator().generate(Vars.world.tiles);
            }
        });
    }
}
