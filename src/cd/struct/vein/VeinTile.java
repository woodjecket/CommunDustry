package cd.struct.vein;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Structs;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.world.Tile;

import java.util.Arrays;

public class VeinTile {
    public VeinEntity[] veins;
    public Tile tile;
    public boolean detected;

    public void drawVein() {
        if (veins.length > 0 && detected && Structs.contains(veins, v -> !v.exhausted())) {
            for (var ve : veins) {
                Tmp.c1.set(ve.type.color).a(1 - ve.centerZ / -128f);
                Draw.color(Tmp.c1);
                Fill.rect(tile.worldx(), tile.worldy(), Vars.tilesize - 1, Vars.tilesize - 1);
            }
        }

    }

    public boolean exhausted(int z, VeinType vt) {
        var vein = Structs.find(veins, v->v.type == vt);
        return vein == null || Math.abs(z - vein.centerZ) > vein.type.rangeScale || vein.exhausted();
    }

    public boolean shouldWrite() {
        return Structs.contains(veins, veinEntity -> !veinEntity.untouched);
    }

    @Override
    public String toString() {
        return "veins:" + Arrays.toString(veins) + " tile:" + tile + "detected:" + detected;
    }
}
