package cd.io;

import cd.CDMod;
import mindustry.Vars;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FiniteOreCustomChunk implements SaveFileReader.CustomChunk {

    public FiniteOreCustomChunk(){
        SaveVersion.addCustomChunk("commundustry-finite-ore", this);
    }
    @Override
    public void write(DataOutput stream) throws IOException {
        var map = CDMod.oreUpdater.amountMap;
        // |---position---|----amount----|
        stream.writeInt(map.size);
        for(var entry: map){
            stream.writeInt(entry.key.pos());
            stream.writeInt(entry.value);
        }
    }

    @Override
    public void read(DataInput stream) throws IOException {
        var map = CDMod.oreUpdater.amountMap;
        map.clear();
        int size = stream.readInt();
        for (int i = 0; i < size; i++) {
            int pos = stream.readInt();
            int amount = stream.readInt();
            map.put(Vars.world.tile(pos),amount);
        }
    }

}
