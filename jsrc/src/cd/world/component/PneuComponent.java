package cd.world.component;

import arc.*;
import arc.audio.*;
import arc.math.geom.*;
import cd.content.*;
import cd.entities.building.*;
import cd.world.stat.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter.*;

import static mindustry.Vars.*;

public class PneuComponent extends BaseComponent{
    // For crafter

    /** The default pressure when building */
    public static final float standardPressure = 1f;
    /** Whether the building can provide pressure. */
    public boolean canProvidePressure;
    /** Pressure output per consume. */
    public float outputPressure = 1f;
    /** Whether the building can consume pressure. */
    public boolean canConsumePressure;
    /** Pressure consume per consume */
    public float pressureConsume = 0.1f;


    //for all block
    /** If pressure is lower than this point, the crafter will nor work. */
    public float minOperatePressure = 2f;
    /** When the pressure approach this point it will explode. */
    public float explodePressure = 15f;
    /** If pressure is over this point, the building will not work. */
    public float maxOperatePressure = 10f;

    //For explosion

    public int explosionRadius = 3;
    public int explosionDamage = 50;
    public Effect explodeEffect = CDFx.pneuSmoke;
    public Sound explodeSound = Sounds.explosion;
    public float explosionShake = 6f, explosionShakeDuration = 16f;

    public float getExplodePressure(){
        return explodePressure;
    }

    @Override
    public boolean onShouldConsume(Building b){
        IPneu bPneu = (IPneu)b;
        return bPneu.getPressure() < maxOperatePressure &&
        (canProvidePressure || bPneu.getPressure() > minOperatePressure) &&
        (!canConsumePressure || bPneu.getPressure() > pressureConsume)
        ;
    }

    @Override
    public void onCraft(GenericCrafterBuild b){
        IPneu bPneu = (IPneu)b;
        if(canProvidePressure) bPneu.setPressure(bPneu.getPressure() + outputPressure);
        if(canConsumePressure) bPneu.setPressure(bPneu.getPressure() - pressureConsume);
    }

    @Override
    public void onPlace(Building b){
        IPneu bPneu = (IPneu)b;
        bPneu.setPressure(standardPressure);
    }

    @Override
    public void onUpdateTile(Building b){
        calculatePressure(b);
    }


    public void calculatePressure(Building b){
        IPneu bPneu = (IPneu)b;
        if(b.block.rotate){
            var other = b.proximity.retainAll(o -> Geometry.d4[b.rotation].x == b.tileX() - o.tileX() &&
            Geometry.d4[b.rotation].y == b.tileY() - o.tileY());
            if(!(other instanceof IPneu otherPneu)) return;
            float thisP = bPneu.getPressure();
            float otherP = otherPneu.getPressure();
            float arrangeP = (thisP + otherP) / 2f;
            if(thisP > otherP && thisP >= standardPressure && otherP >= 0){
                bPneu.setPressure(arrangeP);
                otherPneu.setPressure(arrangeP);
            }
        }else{
            for(Building other : b.proximity){
                if(!(other instanceof IPneu otherPneu)) continue;
                float thisP = bPneu.getPressure();
                float otherP = otherPneu.getPressure();
                float arrangeP = (thisP + otherP) / 2f;
                if(thisP > otherP && thisP >= standardPressure && otherP >= 0){
                    bPneu.setPressure(arrangeP);
                    otherPneu.setPressure(arrangeP);
                }
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
    public void onDestroyed(Building b){
        //Why?
        if(state.rules.reactorExplosions){
            onCreateExplosion(b);
        }
    }

    @Override
    public void onCreateExplosion(Building b){
        IPneu bPneu = (IPneu)b;
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
    public void onSetStats(Block b){
        b.stats.add(CDStats.pressureRange, Core.bundle.get("stat.pressure-range-format"),
        minOperatePressure, maxOperatePressure, explodePressure);
        if(canProvidePressure) b.stats.add(CDStats.pressureOutput, outputPressure, CDStats.perConsume);
        if(canConsumePressure) b.stats.add(CDStats.pressureConsume, pressureConsume, CDStats.perConsume);
    }

    @Override
    public void onSetBars(Block b){
        b.addBar("pressure",
        (entity) -> new Bar(
        () -> Core.bundle.format("bar.pressure-amount", ((IPneu)entity).getPressure()),
        () -> Pal.lightOrange, () -> ((IPneu)entity).getPressure() / explodePressure));
    }
}
