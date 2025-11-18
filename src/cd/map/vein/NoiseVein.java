package cd.map.vein;

import arc.struct.Seq;
import arc.util.noise.Simplex;
import cd.struct.vein.VeinEntity;
import cd.struct.vein.VeinTile;
import cd.struct.vein.VeinType;
import mindustry.Vars;

public class NoiseVein implements VeinGenerator {
    public Seq<VeinType> generate = VeinType.all;

    @Override
    public VeinTile get(int x, int y) {
        var worldTile = Vars.world.tile(x, y);
        float nx = x / 200f;
        float ny = y / 200f;
        var veins = new Seq<VeinEntity>();
        for (var type : generate) {
            float noise1 = Simplex.noise2d(type.id + 3900, 3, 2, 1, nx, ny);
            float noise2 = Simplex.noise2d(type.id + 7800, 3, 2, 1, nx, ny);
            float noise3 = Simplex.noise2d(type.id + 11700, 3, 2, 1, nx, ny);
            float noise4 = Simplex.noise2d(type.id + 15600, 3, 2, 1, nx, ny);
            if (noise1 < type.threshold) {
                var entity = new VeinEntity();
                entity.type = type;
                entity.amount = (int) (type.baseAmount + (noise2 - 0.5) * type.amountScale);
                entity.depth = (int) (type.baseDepth + (noise3 - 0.5) * type.depthScale);
                entity.range = (int) (type.baseRange + (noise4 - 0.5) * type.rangeScale);
                veins.add(entity);
            }
        }
        var vtile = new VeinTile();
        vtile.veins = veins;
        vtile.tile = worldTile;
        return vtile;
    }
}
