package cd.map.vein;

import arc.struct.Seq;
import arc.util.Log;
import arc.util.noise.Simplex;
import cd.content.VeinTypes;
import cd.struct.vein.VeinEntity;
import cd.struct.vein.VeinTile;
import cd.struct.vein.VeinType;
import mindustry.Vars;

import java.util.Arrays;

public class NoiseVein implements VeinGenerator {
    public Seq<VeinType> generate = VeinType.all;

    @Override
    public VeinTile get(int x, int y) {
        var worldTile = Vars.world.tile(x, y);
        float nx = x / 100f;
        float ny = y / 100f;
        var veins = new Seq<VeinEntity>();
        for (var type : generate) {
            float noise1 = Simplex.noise2d(type.id + 3900, 3, 2, 1, nx, ny);
            float noise2 = Simplex.noise2d(type.id + 393900, 3, 2, 1, nx, ny);
            float noise3 = Simplex.noise2d(type.id + 39393900, 3, 2, 1, nx, ny);
            if (noise1 < type.threshold) {
                var entity = new VeinEntity();
                entity.type = type;
                entity.currentAbundance = (int) (type.baseAbundance + (noise2 - 0.5) * type.abundanceScale);
                entity.centerZ = (int) (type.baseZ + (noise3 - 0.5) * type.rangeScale);
                veins.add(entity);
            }
        }
        var vtile = new VeinTile();
        vtile.veins = veins.toArray(VeinEntity.class);
        vtile.tile = worldTile;
        return vtile;
    }
}
