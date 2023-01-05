package cd.entities.component;

import mindustry.gen.Building;
import mindustry.world.Block;

public class MultiComponent extends BaseComponent {
    BaseComponent[] components;

    public MultiComponent(BaseComponent... components){
        this.components = components;
    }

    @Override
    public void onUpdateTile(Building b) {
        for(BaseComponent c: components){
            c.onUpdateTile(b);
        }
    }
    @Override
    public void onDestroyed(Building b) {
        for(BaseComponent c: components){
            c.onDestroyed(b);
        }
    }
    @Override
    public void onCreateExplosion(Building b) {
        for(BaseComponent c: components){
            c.onCreateExplosion(b);
        }
    }
    @Override
    public void onCraft(Building b) {
        for(BaseComponent c: components){
            c.onCraft(b);
        }
    }
    @Override
    public boolean onShouldConsume(Building b) { 
        boolean result = true;
        for(BaseComponent c: components){
            result &= c.onShouldConsume(b);
        }
        return result;
    }
    @Override
    public float onEfficiencyScale(Building b) { 
        float result = 1f;
        for(BaseComponent c: components){
            result *= c.onEfficiencyScale(b);
        }
        return result;
    }
    @Override
    public void onInit(Block b) {
        for(BaseComponent c: components){
            c.onInit(b);
        }
    }
    @Override
    public void onSetStats(Block b) {
        for(BaseComponent c: components){
            c.onSetStats(b);
        }
    }

}
