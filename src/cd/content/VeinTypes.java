package cd.content;

import cd.struct.vein.VeinType;
import mindustry.content.Items;
import mindustry.type.ItemStack;

public class VeinTypes {
    public static VeinType pyrite;
    public static void load(){
        pyrite = new VeinType(){{
            items = ItemStack.with(Items.copper,5,CDItems.iron,5,Items.pyratite,2);
        }};
    }
}
