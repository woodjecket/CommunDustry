package cd.entities.component.pneu;

import arc.*;
import arc.audio.*;
import cd.content.*;
import cd.entities.component.*;
import cd.entities.stat.*;
import cd.type.blocks.pneumatic.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class PneuComponent extends BaseComponent{

    public float explodePressure = 15f;
    public float leakPointPressure = 1f;
    public float maxOperatePressure = 10f;
    public float minOperatePressure = 2f;

    public int explosionRadius = 3;
    public int explosionDamage = 50;
    public Effect explodeEffect = CDFx.pneuSmoke;
    public Sound explodeSound = Sounds.explosion;

    public float explosionShake = 6f, explosionShakeDuration = 16f;

    public PneuComponent(){
    }

    public float getVisualExplodePressure(){
        return explodePressure;
    }

    public float getMaxOperatePressure(){
        return maxOperatePressure;
    }

    public float getMinOperatePressure(){
        return minOperatePressure;
    }

    public float getLeakPointPressure(){
        return leakPointPressure;
    }

    @Override
    public void onUpdateTile(Building b){
        calculatePressure(b);

    }

    public void calculatePressure(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        for(Building other : b.proximity){
            if(!(other instanceof PneuInterface otherPneu))
                continue;
            float thisP = bPneu.getPressure();
            float otherP = otherPneu.getPressure();
            float arrangeP = (thisP + otherP) / 2f;
            if(thisP > otherP && thisP > leakPointPressure && otherP > leakPointPressure){
                bPneu.setPressure(arrangeP);
                otherPneu.setPressure(arrangeP);
            }
        }
        if(bPneu.getPressure() < leakPointPressure){
            bPneu.setPressure((bPneu.getPressure() + leakPointPressure) / 2f + leakPointPressure / 10f);
        }

        if(bPneu.getPressure() > explodePressure){
            b.kill();
        }
    }

    @Override
    public void onCreateExplosion(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        if(bPneu.getPressure() > explodePressure){
            if(explosionDamage > 0){
                Damage.damage(b.x, b.y, explosionRadius * tilesize, explosionDamage);
            }

            explodeEffect.at(b);
            explodeSound.at(b);

            if(explosionShake > 0){
                Effect.shake(explosionShake, explosionShakeDuration, b);
            }
        }
    }

    @Override
    public void onDestroyed(Building b){
        //Why?
        if(state.rules.reactorExplosions){
            onCreateExplosion(b);
        }
    }

    @Override
    public void onSetStats(Block b){
        b.stats.add(CDStat.pressureRange, Core.bundle.get("stat.pressure-range-format"), minOperatePressure, maxOperatePressure, explodePressure);
    }
}
