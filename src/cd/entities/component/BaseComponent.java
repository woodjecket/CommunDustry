package cd.entities.component;

import mindustry.gen.*;
import mindustry.world.*;

public abstract class BaseComponent{
    public MultiComponent parentComponent;
    public float visualExplodePressure, maxOperatePressure, minOperatePressure, leakPointPressure;

    public BaseComponent(){
    }

    public float getVisualExplodePressure(){
        return -1f;
    }

    public float getMaxOperatePressure(){
        return -1f;
    }

    public float getMinOperatePressure(){
        return -1f;
    }

    public float getLeakPointPressure(){
        return -1f;
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

}
