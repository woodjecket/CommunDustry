package cd.world.block.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import cd.manager.*;
import mindustry.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class FiniteOre extends OreBlock{

    public TextureRegion[][] stages;

    public Floor exhauseted;

    /** How many stages according to the capacity*/
    public int stage = 3;
    public FiniteOre(Item ore){
        super("finite-ore-" + ore.name, ore);
        exhauseted = new Floor("exhausted-finite-ore-"+ ore.name);
    }

    @Override
    public void load(){
        super.load();
        stages = new TextureRegion[variants][stage];
        for(int i = 0; i < variants; i++){
            for(int j = 0; j < stage; j++){
                stages[i][j] = Core.atlas.find(Vars.content.transformName(name) + "-" + i + "-" + j);
            }
        }
    }

    @Override
    public void drawBase(Tile tile){
        //Float division
        float slack = tile.extraData;
        float capacity = FiniteOreManager.getTileCapacity(tile);

        int currentStage = Mathf.ceil(slack / capacity * (stage - 1));

        Draw.rect(stages[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))][currentStage],
        tile.worldx(), tile.worldy());
    }

    //update owe to FiniteOreManager
}
