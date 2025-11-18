package cd.struct.vein;

import arc.math.Mathf;
import arc.util.Log;
import mindustry.type.Item;
import mindustry.type.ItemStack;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class VeinEntity {
    public VeinType type;
    public int currentAbundance = 5;
    public int centerZ;
    private int seed;
    public boolean touched = false;

    @Override
    public String toString() {
        return type.toString() + "amount:" + currentAbundance + "z:"+centerZ;
    }

    public boolean exhausted() {
        return currentAbundance < 0;
    }

    public void consume() {
        touched = true;
        currentAbundance -= 1;
    }

    public Item offload() {

        int sum = 0;
        for(ItemStack stack : type.items) sum += stack.amount;

        int i = Mathf.randomSeed(seed++, 0, sum - 1);
        int count = 0;
        Item item = null;

        //guaranteed desync since items are random - won't be fixed and probably isn't too important
        for(ItemStack stack : type.items){
            if(i >= count && i < count + stack.amount){
                item = stack.item;
                break;
            }
            count += stack.amount;
        }
        return item;
    }

    public void write(DataOutput stream) throws IOException {
        stream.writeShort(type.id);
        stream.writeInt(currentAbundance);
        stream.writeInt(centerZ);
        stream.writeInt(seed);
        stream.writeBoolean(touched);
    }

    public void read(DataInput stream) throws IOException {
        type = VeinType.all.get(stream.readShort());
        currentAbundance = stream.readInt();
        centerZ = stream.readInt();
        seed = stream.readInt();
        touched = stream.readBoolean();
    }
}
