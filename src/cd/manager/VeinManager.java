package cd.manager;

import arc.Events;
import arc.struct.ObjectIntMap;
import arc.struct.ObjectMap;
import cd.map.vein.NoiseVein;
import cd.map.vein.VeinGenerator;
import cd.struct.vein.VeinTile;
import mindustry.game.EventType;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;
import mindustry.world.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static mindustry.Vars.world;

public class VeinManager {
    public VeinGenerator currentGenerator = new NoiseVein();
    public VeinChunk veinChunk;
    public ObjectMap<Tile, VeinTile> lazy = new ObjectMap<>();

    {
        veinChunk = new VeinChunk();
        Events.run(EventType.Trigger.draw, this::draw);
    }

    public VeinTile get(Tile tile) {
        return tile == null ? null: lazy.get(tile, ()->currentGenerator.get(tile.x, tile.y));
    }

    private void draw(){
        for(var vein: lazy.values()){
            vein.drawVein();
        }
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
