package cd.struct.vein;

import mindustry.type.ItemStack;

import java.util.Arrays;

public class VeinType {
    public ItemStack[] items;
    public int baseAbundance = 100;
    public int baseZThreshold = 75;

    @Override
    public String toString() {
        return "VeinType:" + Arrays.toString(items);
    }
}
