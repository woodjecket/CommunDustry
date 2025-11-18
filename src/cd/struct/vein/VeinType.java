package cd.struct.vein;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.type.ItemStack;

import java.util.Arrays;


/** Denotes the flyweight of a kind of vein.*/
public class VeinType {
    private static short idCount;
    public static Seq<VeinType> all = new Seq<>();

    public short id;
    public ItemStack[] items;
    public Color color;

    public float threshold = 0.16f;
    public int baseAmount = 5;
    public int baseDepth = -75;
    public int baseRange = 20;
    public int amountScale = 3;
    public int depthScale = 15;
    public int rangeScale = 3;


    public VeinType() {
        id = idCount;
        idCount++;
        all.add(this);
    }

    @Override
    public String toString() {
        return "{ VeinType: " + id + ",items: " + Arrays.toString(items) + "}";
    }
}
