package cd.type.blocks;

import mindustry.type.ItemStack;
import mindustry.world.blocks.production.GenericCrafter;

public class CatalyzerCrafter extends GenericCrafter {

    /**
     * The catalyzer of the crafter. if exists, the efficiency will increase or it
     * can only work with catalyzer.
     */
    public ItemStack[] catalyzer;

    /** Whether the crafter must work with catalyzer */
    public boolean catalyzerNecessity;

    public CatalyzerCrafter(String name) {
        super(name);

    }

    @Override
    public void init() {
        //there makes a mistake that when I tried to write to itemFilter before super.init()
        //我这个大聪明还没调用init就要操作itemFilter
        super.init();
        // To let catalyzer can enter the block
        if (catalyzer != null) {
            for (ItemStack i : catalyzer) {
                itemFilter[i.item.id] = true;
            }
        }

    }

    @Override
    public void setStats() {
        super.setStats();
        // TODO for catalyzer
    }

    public class CatalyzerCrafterBuilding extends GenericCrafterBuild {

        @Override
        public boolean shouldConsume() {
            if (catalyzer != null) {
                if (catalyzerNecessity) {
                    for (ItemStack i : catalyzer) {
                        if (items.get(i.item) == 0) {
                            return false;
                        }
                    }
                }
            }
            return super.shouldConsume();
        }
    }

}
