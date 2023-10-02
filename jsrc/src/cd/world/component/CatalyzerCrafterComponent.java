package cd.world.component;

import cd.world.stat.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class CatalyzerCrafterComponent extends BaseComponent{
    /**
     * The catalyzer of the crafter. if exists, the efficiency will increase ,or it
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
    /** Whether to add or multiply scale */
    public String catalyzerCalculation = "mul";
    /** Maximum possible efficiency after catalyzer. */
    public float maxEfficiency = 4f;
    /** Chance to consume catalyzer. -1 for disabled */
    public float catalyzerChance = -1f;

    @Override
    public void onInit(Block b){
        if(catalyzer != null){
            for(ItemStack i : catalyzer){
                b.itemFilter[i.item.id] = true;
            }
        }
    }

    @Override
    public void onSetStats(Block b){
        b.stats.add(CDStats.catalyzer, StatValues.items(true, catalyzer));
    }

    @Override
    public boolean onShouldConsume(Building b){
        if(catalyzer != null && catalyzerNecessity){
            for(ItemStack i : catalyzer){
                if(!b.items.has(i.item)){
                    return false;
                }
            }
        }
        return true;
    }



    @Override
    public float onEfficiencyScale(Building b){
        if(!catalyzerNecessity || catalyzerScale != null){
            float result = baseCatalyzerScale;
            for(int i = 0; i < catalyzer.length; i++){
                ItemStack stack = catalyzer[i];
                if(b.items.get(stack.item) >= stack.amount){
                    switch(catalyzerCalculation){
                        case "mul" -> result *= catalyzerScale[i];
                        case "add" -> result += catalyzerScale[i];
                    }
                }
            }
            return Math.min(maxEfficiency, result);
        }
        return baseCatalyzerScale;
    }

}
