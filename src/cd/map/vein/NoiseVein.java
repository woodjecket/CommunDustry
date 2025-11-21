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
        var veins = new Seq<VeinEntity>();
        for (var type : generate) {
            veins.addUnique(type.generator.get(x,y));
        }
        veins.remove((VeinEntity) null);
        var vtile = new VeinTile();
        vtile.veins = veins;
        vtile.tile = worldTile;
        return vtile;
    }
}
