package cd.entities.component;

import arc.math.Mathf;
import cd.entities.stat.CDStat;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.StatValues;

public class CatalyzerCrafterComponent extends BaseComponent {
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
    public float baseCatalyzerScale = 1f;
    /** Efficiency that catalyzers increases */
    public float[] catalyzerScale = {1f};
    /** Whether to add or multipily scale */
    public String catalyzerCaculation = "mul";
    /** Maximum possible efficiency after catalyzer. */
    public float maxEfficiency = 4f;
    /** Chance to consume catalyzer. -1 for disabled */
    public float catalyzerChance = -1f;

    @Override
    public void onInit(Block b){
        if (catalyzer != null) {
            for (ItemStack i : catalyzer) {
                b.itemFilter[i.item.id] = true;
            }
        }     
    }

    @Override
    public void onSetStats(Block b){
        b.stats.add(CDStat.catalyzer, StatValues.items(true, catalyzer));   
    }

    @Override
    public boolean onShouldConsume(Building b){
        if (catalyzer != null && catalyzerNecessity) {
            for (ItemStack i : catalyzer) {
                if (!b.items.has(i.item)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onUpdateTile(Building b){
        if(Mathf.chanceDelta(catalyzerChance)){
            b.items.remove(catalyzer[Mathf.random(catalyzer.length - 1)]);
        }
    }

    @Override
    public float onEfficiencyScale(Building b){
        if (!catalyzerNecessity || catalyzerScale != null) {
            float result = baseCatalyzerScale;
            for (int i = 0; i < catalyzer.length; i++) {
                ItemStack stack = catalyzer[i];
                if (b.items.get(stack.item) >= stack.amount) {
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
        return baseCatalyzerScale;
    }

}
