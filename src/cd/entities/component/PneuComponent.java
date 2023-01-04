package cd.entities.component;

import arc.audio.Sound;
import cd.content.CDFx;
import cd.type.blocks.pneumatic.PneuInterface;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import static mindustry.Vars.*;

public class PneuComponent extends BaseComponent {

    public float visualExplodePressure = 15f;
    public float visualMaxOperatePressure = 10f;
    public float visualMinOperatePressure = 2f;
    public float leakPointPressure = 1f;

    public int explosionRadius = 3;
    public int explosionDamage = 50;
    public Effect explodeEffect = CDFx.pneuSmoke;
    public Sound explodeSound = Sounds.explosion;

    public float explosionShake = 6f, explosionShakeDuration = 16f;

    public PneuComponent() {
    }

    public void calculatePressure(Building b) {
        PneuInterface bPneu = (PneuInterface) b;
        for (Building other : b.proximity) {
            if (other.block != b.block)
                continue;
            PneuInterface otherPneu = (PneuInterface) other;
            float thisP = bPneu.getPressure();
            float otherP = otherPneu.getPressure();
            float arrangeP = (thisP + otherP) / 2f;
            if (thisP > otherP && thisP > leakPointPressure && otherP > leakPointPressure) {
                bPneu.setPressure(arrangeP);
                otherPneu.setPressure(arrangeP);
            }
        }
        if (bPneu.getPressure() < leakPointPressure) {
            bPneu.setPressure((bPneu.getPressure() + leakPointPressure) / 2f + leakPointPressure / 10f);
        }

        if (bPneu.getPressure() > visualExplodePressure) {
            b.kill();
        }
    }

    @Override
    public void onCreateExplosion(Building b){
        PneuInterface bPneu = (PneuInterface) b;
        if (bPneu.getPressure() > visualExplodePressure) {
            if (explosionDamage > 0) {
                Damage.damage(b.x, b.y, explosionRadius * tilesize, explosionDamage);
            }

            explodeEffect.at(b);
            explodeSound.at(b);

            if (explosionShake > 0) {
                Effect.shake(explosionShake, explosionShakeDuration, b);
            }
        }
    }

    @Override
    public void onDestroyed(Building b){
        //Why?
        if (state.rules.reactorExplosions) {
            onCreateExplosion(b);
        }
    }
}