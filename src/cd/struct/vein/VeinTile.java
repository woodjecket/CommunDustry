package cd.struct.vein;

import arc.struct.Seq;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Structs;
import mindustry.Vars;
import mindustry.world.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class VeinTile {
    public Seq<VeinEntity> veins;
    public Tile tile;

    public @Nullable VeinEntity getEntity(int depth) {
        return veins.find(ve -> Math.abs(ve.depth - depth) < ve.range);
    }

    public boolean shouldWrite() {
        return veins.contains(ve -> ve.shouldWrite);
    }

    @Override
    public String toString() {
        return "{veins:" + veins + ", tile: (" + tile.x + "," + tile.y + ")}";
    }

    public void write(DataOutput stream) throws IOException {
        stream.writeInt(tile.pos());

        int count = veins.count(ve -> ve.shouldWrite);
        stream.writeInt(count);

        //No FP, for exception cannot be caught
        for (var vein : veins) {
            if (vein.shouldWrite) vein.write(stream);
        }
    }

    public void read(DataInput stream) throws IOException {
        int pos = stream.readInt();
        tile = Vars.world.tile(pos);
        int count = stream.readInt();
        veins.clear();
        for (int i = 0; i < count; i++) {
            var ve = new VeinEntity();
            ve.read(stream);
            veins.add(ve);
        }
    }
}
