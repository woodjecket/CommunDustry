package cd.world.component;

import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter.*;

/** Basic components with no operation */
public abstract class BaseComponent{
    /** Calls when building {@code updateTile} */
    public void onUpdateTile(Building b){
    }

    /** Calls when building {@code onDestroyed} */
    public void onDestroyed(Building b){
    }

    /** Calls when building {@code createExplosion} */
    public void onCreateExplosion(Building b){
    }

    /** Calls when building {@code craft} */
    public void onCraft(GenericCrafterBuild b){
    }

    /** Calls when building {@code shouldConsume}, "and" relation */
    public boolean onShouldConsume(Building b){
        return true;
    }

    /** Calls when building {@code efficiencyScale}, "multiply" relation */
    public float onEfficiencyScale(Building b){
        return 1f;
    }

    /** Calls when building {@code init} */
    public void onInit(Block b){
    }

    /** Calls when building {@code setStats} */
    public void onSetStats(Block b){
    }

    /** Calls when building {@code load} */
    public void onLoad(){
    }

    /** Calls when building {@code place} */
    public void onPlace(Building b){

    }

    /** Calls when building {@code drawPlace} */
    public void onDrawPlace(Block b, int x, int y, int rotation){
    }

    /** Calls when building {@code Building.draw} */
    public void onEntityDraw(Building b){
    }

    /** Calls when building {@code setBars} */
    public void onSetBars(Block b){
    }

}
