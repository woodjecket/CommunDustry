package cd.content;


import arc.graphics.*;
import cd.type.blocks.CatalyzerCrafter;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import static mindustry.type.ItemStack.*;

public class CDBlocks {
    public static Block test1;

    public void load() {
        test1 = new CatalyzerCrafter("test") {
            {
                requirements(Category.crafting, with(Items.copper, 30, Items.lead, 25));
                craftEffect = Fx.smeltsmoke;
                outputItem = new ItemStack(Items.silicon, 1);
                craftTime = 40f;
                size = 2;
                hasPower = true;
                hasLiquids = false;
                drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
                ambientSound = Sounds.smelter;
                ambientSoundVolume = 0.07f;

                consumeItems(with(Items.coal, 1, Items.sand, 2));
                consumePower(0.50f);
                catalyzer = with(Items.copper,1);
                catalyzerNecessity = true;
            }
        };
    }
}
