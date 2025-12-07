package cd.struct.vein;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Tmp;
import cd.content.CDItems;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.type.ItemStack;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/*Denotes a vein entity that contains an amount of ore and has its depth and depth's range.*/
public class VeinEntity {
    public static final VeinEntity infiniteStone = new VeinEntity() {
        {
            type = new VeinType() {{
                color = new Color();
                items = ItemStack.with(CDItems.stone, 1);
                generator = (x, y) -> null;
            }};
        }

        @Override
        public boolean exhausted() {
            return false;
        }

        @Override
        public String toString() {
            return "Mr. Infinite Stone";
        }
    };
    public VeinType type;
    public int amount, depth, range;

    private int seed;
    public boolean shouldWrite, detected;

    @Override
    public String toString() {
        return "{type: " + type.toString() + ", amount:" + amount + ", depth:" + depth + ", range:" + range
                + ", shouldWrite:" + shouldWrite + ", detected:" + detected + "}";
    }

    public boolean exhausted() {
        return amount <= 0;
    }

    public void drawVeinEntity(float x, float y) {
        if (!detected) return;
        if (exhausted()) return;
        Log.info(toString());
        Tmp.c1.set(type.color).a(1 - depth / -128f);
        Draw.color(Tmp.c1);
        Fill.rect(x, y, Vars.tilesize - 1, Vars.tilesize - 1);
    }

    public Item produceOre() {
        shouldWrite = true;
        amount--;

        //Copied from Separator.java
        int sum = 0;
        for (ItemStack stack : type.items) sum += stack.amount;

        int i = Mathf.randomSeed(seed++, 0, sum - 1);
        int count = 0;
        Item item = null;

        //guaranteed desync since items are random - won't be fixed and probably isn't too important
        for (ItemStack stack : type.items) {
            if (i >= count && i < count + stack.amount) {
                item = stack.item;
                break;
            }
            count += stack.amount;
        }
        return item;
    }

    public void write(DataOutput stream) throws IOException {
        stream.writeShort(type.id);
        stream.writeInt(amount);
        stream.writeInt(depth);
        stream.writeInt(range);
        stream.writeInt(seed);
        stream.writeBoolean(shouldWrite);
        stream.writeBoolean(detected);
    }

    public void read(DataInput stream) throws IOException {
        type = VeinType.all.get(stream.readShort());
        amount = stream.readInt();
        depth = stream.readInt();
        range = stream.readInt();
        seed = stream.readInt();
        shouldWrite = stream.readBoolean();
        detected = stream.readBoolean();
    }
}
