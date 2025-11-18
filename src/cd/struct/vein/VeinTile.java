package cd.struct.vein;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Structs;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.world.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
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
        return Structs.contains(veins, veinEntity -> veinEntity.touched);
    }

    @Override
    public String toString() {
        return "veins:" + Arrays.toString(veins) + " tile:" + tile + "detected:" + detected;
    }

    public void write(DataOutput stream) throws IOException {
        stream.writeInt(tile.pos());
        int count = 0;
        for(var vein: veins){
            if(vein.touched) count++;
        }
        stream.writeInt(count);
        for(var vein: veins){
            Log.info("Has written vein: @", vein);
            if(vein.touched) vein.write(stream);
        }
        stream.writeBoolean(detected);
    }

    public void read(DataInput stream) throws IOException {
        int pos = stream.readInt();
        tile = Vars.world.tile(pos);

        int count = stream.readInt();
        Seq<VeinEntity> builder = new Seq<>();
        for (int i = 0; i < count; i++) {

            var ve = new VeinEntity();
            ve.read(stream);
            Log.info("Has read vein: @", ve);
            builder.add(ve);
        }
        veins = builder.toArray(VeinEntity.class);

        detected = stream.readBoolean();
    }
}
