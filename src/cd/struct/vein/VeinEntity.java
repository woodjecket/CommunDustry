package cd.struct.vein;

import arc.math.Mathf;
import mindustry.type.Item;
import mindustry.type.ItemStack;

public class VeinEntity {
    public VeinType type;
    public int currentAbundance = 5;
    public int centerZ;
    private int seed;
    public boolean untouched;

    @Override
    public String toString() {
        return type.toString() + "amount:" + currentAbundance + "z:"+centerZ;
    }

    public boolean exhausted() {
        return currentAbundance < 0;
    }

    public void consume() {
        untouched = false;
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
}
