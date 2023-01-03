package cd.type.blocks;

import arc.math.Mathf;
import cd.entities.stat.CDStat;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

public class CatalyzerCrafter extends GenericCrafter {

    /**
     * The catalyzer of the crafter. if exists, the efficiency will increase or it
     * can only work with catalyzer.
     */
    public ItemStack[] catalyzer;

    /**
     * Whether the crafter must work with catalyzer, if not, the catalyzers aur for
     * increasing the efficiency.
     */
    public boolean catalyzerNecessity;

    /** Base efficiency catalyzer for 100% */
    public float catalyzerRequirement = 0f;
    /** Efficiency that catalyzers increases */
    public float[] catalyzerScale = {1f};
    /** Whether to add or multipily scale */
    public String catalyzerCaculation = "mul";
    /** Maximum possible efficiency after catalyzer. */
    public float maxEfficiency = 4f;

    public CatalyzerCrafter(String name) {
        super(name);

    }

    @Override
    public void init() {
        // there makes a mistake that when I tried to write to itemFilter before
        // super.init()
        // 我这个大聪明还没调用init就要操作itemFilter
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
        // use items(boolean,...)not items(float,...)to let not display time
        stats.add(CDStat.catalyzer, StatValues.items(true, catalyzer));
    }

    public class CatalyzerCrafterBuilding extends GenericCrafterBuild {

        @Override
        public boolean shouldConsume() {
            if (catalyzer != null && catalyzerNecessity) {
                for (ItemStack i : catalyzer) {
                    if (items.get(i.item) == 0) {
                        return false;
                    }
                }
            }
            return super.shouldConsume();
        }

        @Override
        public float efficiencyScale() {
            if (!catalyzerNecessity || catalyzerScale != null) {
                float result = 1f;
                for (int i = 0; i < catalyzer.length; i++) {
                    ItemStack stack = catalyzer[i];
                    if (items.get(stack.item) >= stack.amount) {
                        switch (catalyzerCaculation) {
                            case "mul":
                                result *= catalyzerScale[i];
                                break;
                            case "add":
                                result += catalyzerScale[i];
                                break;
                        }
                    }
                }
                return Math.min(maxEfficiency, result);
            }
            return 1f;
        }
    }

}
