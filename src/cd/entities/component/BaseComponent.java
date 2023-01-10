package cd.entities.component;

import mindustry.gen.*;
import mindustry.world.*;

public class BaseComponent{
    public boolean hasPneu = false;
    public boolean hasLaser = false;

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

    public void onPlace(Building b){

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

    public float getExplodePressure(){
        return -1f;
    }

    public int getLaserRange(){
        return -1;
    }

    public void onLoad(){

    }

    public void onDrawPlace(Block b, int x, int y, int rotation, boolean valid){

    }

    public boolean isProvideLaserEnergy(Building b, int bx, int by){
        return false;
    }

    public boolean isAcceptLaserEnergy(Building b, int bx, int by){
        return false;
    }

    public void onEntityDraw(Building b){
    }

}
