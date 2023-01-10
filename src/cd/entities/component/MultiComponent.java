package cd.entities.component;

import mindustry.gen.*;
import mindustry.world.*;

public class MultiComponent extends BaseComponent{
    BaseComponent[] components;

    public MultiComponent(BaseComponent... components){
        this.components = components;
        for(var c : components){
            if(c instanceof PneuComponent){
                hasPneu = true;
                break;
            }
            if(c instanceof LaserEnergyComponent){
                hasLaser = true;
                break;
            }
        }
    }

    public float getExplodePressure(){
        if(!hasPneu) return -1f;
        for(var c : components){
            if(c instanceof PneuComponent) return c.getExplodePressure();
        }
        return -1f;
    }

    public int getLaserRange(){
        if(!hasLaser) return -1;
        for(var c : components){
            if(c instanceof LaserEnergyComponent) return c.getLaserRange();
        }
        return -1;
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

    @Override
    public void onPlace(Building b){
        for(BaseComponent c : components){
            c.onPlace(b);
        }
    }

    @Override
    public void onLoad(){
        for(BaseComponent c : components){
            c.onLoad();
        }
    }

    @Override
    public void onDrawPlace(Block b, int x, int y, int rotation, boolean valid){
        for(BaseComponent c : components){
            c.onDrawPlace(b, x, y, rotation, valid);
        }
    }

    @Override
    public boolean isProvideLaserEnergy(Building b, int bx, int by){
        boolean result = false;
        for(BaseComponent c : components){
            result |= c.isProvideLaserEnergy(b, bx, by);
        }
        return result;
    }

    @Override
    public boolean isAcceptLaserEnergy(Building b, int bx, int by){
        boolean result = false;
        for(BaseComponent c : components){
            result |= c.isAcceptLaserEnergy(b, bx, by);
        }
        return result;
    }

    @Override
    public void onEntityDraw(Building b){
        for(BaseComponent c : components){
            c.onEntityDraw(b);
        }
    }
}
