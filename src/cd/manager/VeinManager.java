package cd.manager;

import arc.Events;
import arc.struct.ObjectMap;
import arc.util.Log;
import cd.map.vein.NoiseVein;
import cd.map.vein.VeinGenerator;
import cd.struct.vein.VeinTile;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;
import mindustry.world.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class VeinManager {
    public VeinGenerator currentGenerator = new NoiseVein();
    public VeinChunk veinChunk = new VeinChunk();
    public ObjectMap<Tile, VeinTile> lazy = new ObjectMap<>();

    {
        Events.on(EventType.ResetEvent.class, r->{
            lazy.clear();
        });
    }

    public VeinTile get(Tile tile) {
        return tile == null ? null : lazy.get(tile, () -> currentGenerator.get(tile.x, tile.y));
    }

    public VeinTile get(int x, int y) {
        return get(Vars.world.tile(x, y));
    }

    public void draw() {
        for (var vein : lazy.values()) {
            vein.veins.each(ve -> ve.drawVeinEntity(vein.tile.worldx(), vein.tile.worldy()));
        }
    }

    public class VeinChunk implements SaveFileReader.CustomChunk {
        {
            SaveVersion.addCustomChunk("commundustry-v5-vein", this);
        }

        @Override
        public void write(DataOutput stream) throws IOException {

            int count = 0;
            Log.info(lazy);
            for (var v : lazy.values()) {
                if (v.shouldWrite()) count++;
            }
            Log.info("write total:@", count);
            stream.writeInt(count);

            for (var v : lazy.values()) {
                if (v.shouldWrite()) {
                    Log.info("Has written veinTile: @", v);
                    v.write(stream);
                }
            }
        }

        @Override
        public void read(DataInput stream) throws IOException {
            int count = stream.readInt();
            Log.info("read total:@", count);
            lazy.clear();
            for (int i = 0; i < count; i++) {
                var vt = new VeinTile();
                vt.read(stream);
                Log.info("Has read veinTile: @", vt);
                lazy.put(vt.tile, vt);
            }
        }
    }
}
