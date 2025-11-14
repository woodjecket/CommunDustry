package cd.map.vein;

import arc.util.Log;
import arc.util.noise.Simplex;
import cd.content.VeinTypes;
import cd.struct.vein.VeinEntity;
import cd.struct.vein.VeinTile;
import cd.struct.vein.VeinType;
import mindustry.Vars;

import java.util.Arrays;

public class NoiseVein implements VeinGenerator{
    @Override
    public VeinTile get(int x, int y) {
        var tileA = Vars.world.tile(x,y);
        VeinEntity[] vein;
        var noise = Simplex.noise2d(114,1,1,1,x,y);
        if(noise > 0.6){
            vein = new VeinEntity[]{};
        }else {
        vein = new VeinEntity[]{new VeinEntity(){{
            type = VeinTypes.pyrite;
        }}};
        }
        Log.infoList(tileA, Arrays.toString(vein));
        return new VeinTile(){{
            tile = tileA;
            veins = vein;
        }};
    }
}
