package cd.entities.component;

import cd.entities.component.pneu.*;
import mindustry.gen.*;
import mindustry.world.*;

public class MultiComponent extends BaseComponent{
    BaseComponent[] components;
    boolean hasPneu = false;

    public MultiComponent(BaseComponent... components){
        this.components = components;
        for(var c : components){
            c.parentComponent = this;
            if(c instanceof PneuComponent) hasPneu = true;
        }
    }

    public float getVisualExplodePressure(){
        if(!hasPneu) return -1f;
        for(var c : components){
            if(c instanceof PneuComponent) return c.getVisualExplodePressure();
        }
        return -1f;
    }

    public float getMaxOperatePressure(){
        if(!hasPneu) return -1f;
        for(var c : components){
            if(c instanceof PneuComponent) return c.getMaxOperatePressure();
        }
        return -1f;
    }

    public float getMinOperatePressure(){
        if(!hasPneu) return -1f;
        for(var c : components){
            if(c instanceof PneuComponent) return c.getMinOperatePressure();
        }
        return -1f;
    }

    public float getLeakPointPressure(){
        if(!hasPneu) return -1f;
        for(var c : components){
            if(c instanceof PneuComponent) return c.getLeakPointPressure();
        }
        return -1f;
    }

    @Override
    public void onUpdateTile(Building b){
        for(BaseComponent c : components){
            c.onUpdateTile(b);
        }
    }

    @Override
    public void onDestroyed(Building b){
        for(BaseComponent c : components){
            c.onDestroyed(b);
        }
    }

    @Override
    public void onCreateExplosion(Building b){
        for(BaseComponent c : components){
            c.onCreateExplosion(b);
        }
    }

    @Override
    public void onCraft(Building b){
        for(BaseComponent c : components){
            c.onCraft(b);
        }
    }

    @Override
    public boolean onShouldConsume(Building b){
        boolean result = true;
        for(BaseComponent c : components){
            result &= c.onShouldConsume(b);
        }
        return result;
    }

    @Override
    public float onEfficiencyScale(Building b){
        float result = 1f;
        for(BaseComponent c : components){
            result *= c.onEfficiencyScale(b);
        }
        return result;
    }

    @Override
    public void onInit(Block b){
        for(BaseComponent c : components){
            c.onInit(b);
        }
    }

    @Override
    public void onSetStats(Block b){
        for(BaseComponent c : components){
            c.onSetStats(b);
        }
    }


}
