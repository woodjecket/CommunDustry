package cd.manager;

import arc.struct.ObjectIntMap;
import arc.struct.ObjectMap;
import cd.map.vein.VeinGenerator;
import cd.struct.vein.VeinTile;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;
import mindustry.world.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static mindustry.Vars.world;

public class VeinManager {
    public VeinGenerator currentGenerator;
    public VeinChunk veinChunk = new VeinChunk();
    public ObjectMap<Tile, VeinTile> lazy = new ObjectMap<>();

    public VeinTile get(Tile tile) {
        return lazy.get(tile, currentGenerator.get(tile.x, tile.y));
    }

    public class VeinChunk implements SaveFileReader.CustomChunk {
        {
            SaveVersion.addCustomChunk("commundustry-v5-vein", this);
        }

        @Override
        public void write(DataOutput stream) throws IOException {

        }

        @Override
        public void read(DataInput stream) throws IOException {

        }
    }
}
