package cd.entities.component;

import arc.*;
import arc.audio.*;
import cd.content.*;
import cd.entities.stat.*;
import cd.type.blocks.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class PneuComponent extends BaseComponent{
    // For crafter

    /** Whether the building can provide pressure. */
    public boolean canProvidePressure;

    /** Pressure output per consume. */
    public float outputPressure = 1f;

    /** Whether the building can consume pressure. */
    public boolean canConsumePressure;

    /** Pressure consume per consume */
    public float pressureConsume = 0.1f;

    /** If pressure is lower than this point, the crafter will nor work. */
    public float minOperatePressure = 2f;


    //for all block

    /** When the pressure approach this point it will explode. */
    public float explodePressure = 15f;

    /** The default pressure when building */
    public float standardPressure = 1f;

    /** If pressure is over this point, the building will not work. */
    public float maxOperatePressure = 10f;

    //For explosion

    public int explosionRadius = 3;
    public int explosionDamage = 50;
    public Effect explodeEffect = CDFx.pneuSmoke;
    public Sound explodeSound = Sounds.explosion;
    public float explosionShake = 6f, explosionShakeDuration = 16f;


    public PneuComponent(){
    }

    public float getExplodePressure(){
        return explodePressure;
    }

    @Override
    public boolean onShouldConsume(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        return bPneu.getPressure() < maxOperatePressure && (canProvidePressure || bPneu.getPressure() > minOperatePressure);
    }

    @Override
    public void onCraft(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        if(canProvidePressure) bPneu.setPressure(bPneu.getPressure() + outputPressure);
        if(canConsumePressure) bPneu.setPressure(bPneu.getPressure() - pressureConsume);
    }

    @Override
    public void onPlace(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        bPneu.setPressure(standardPressure);
    }

    @Override
    public void onUpdateTile(Building b){
        calculatePressure(b);
    }

    public void calculatePressure(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        for(Building other : b.proximity){
            if(!(other instanceof PneuInterface otherPneu)) continue;
            float thisP = bPneu.getPressure();
            float otherP = otherPneu.getPressure();
            float arrangeP = (thisP + otherP) / 2f;
            if(thisP > otherP && thisP >= standardPressure && otherP >= standardPressure){
                bPneu.setPressure(arrangeP);
                otherPneu.setPressure(arrangeP);
            }
        }
        if(bPneu.getPressure() < standardPressure){
            bPneu.setPressure((bPneu.getPressure() + standardPressure) / 2f + standardPressure / 10f);
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
        if(canProvidePressure) b.stats.add(CDStat.pressureOutput, outputPressure, CDStat.perConsume);
        if(canConsumePressure) b.stats.add(CDStat.pressureConsume, pressureConsume, CDStat.perConsume);
    }

    @Override
    public void onSetBars(Block b){
        b.addBar("pressure",
        (entity) -> new Bar(
        () -> Core.bundle.format("bar.pressure-amount", ((PneuInterface)entity).getPressure()),
        () -> Pal.lightOrange, () -> ((PneuInterface)entity).getPressure() / explodePressure));
    }
}
