package cd.content;

import arc.graphics.Color;
import cd.struct.vein.VeinType;
import mindustry.content.Items;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;

public class VeinTypes {
    public static VeinType pyrite, galena, rutite;
    public static void load(){
        pyrite = new VeinType(){{
            items = ItemStack.with(Items.copper,5,CDItems.iron,5,Items.pyratite,2);
            color = Pal.accent.cpy();
        }};
        galena = new VeinType(){{
            items = ItemStack.with(Items.lead,5,Items.pyratite,2);
            color = Pal.spore.cpy();
        }};
        rutite = new VeinType(){{
            items = ItemStack.with(Items.titanium,5,CDItems.chromium,1);
            color = Color.scarlet.cpy();
        }};

    }
}
