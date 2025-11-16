package cd.struct.vein;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.type.ItemStack;

import java.util.Arrays;

public class VeinType {
    private static short idCount;
    public static Seq<VeinType> all = new Seq<>();

    public short id;
    public ItemStack[] items;
    public Color color;
    public int baseAbundance = 5;
    public int baseZ = -75;

    public float threshold = 0.16f;
    public int rangeScale = 15;
    public int abundanceScale = 3;

    public VeinType() {
        id = idCount;
        idCount++;
        all.add(this);
    }

    @Override
    public String toString() {
        return "VeinType:" + id + Arrays.toString(items);
    }
}
