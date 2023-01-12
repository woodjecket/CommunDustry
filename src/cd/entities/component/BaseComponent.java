package cd.entities.component;

import mindustry.gen.*;
import mindustry.world.*;

public class BaseComponent{

    public BaseComponent(){
    }

    public void onUpdateTile(Building b){
    }

    public void onDestroyed(Building b){
    }

    public void onCreateExplosion(Building b){
    }

    public void onCraft(Building b){
    }

    public boolean onShouldConsume(Building b){
        return true;
    }

    public float onEfficiencyScale(Building b){
        return 1f;
    }

    public void onInit(Block b){
    }

    public void onSetStats(Block b){
    }

    public void onLoad(){
    }

    public void onPlace(Building b){

    }

    public void onDrawPlace(Block b, int x, int y, int rotation){
    }

    public void onEntityDraw(Building b){
    }

    public void onSetBars(Block b){
    }
}
