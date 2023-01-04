package cd.type.blocks.pneumatic;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.util.*;
import cd.content.CDFx;
import cd.type.blocks.ComponentBlock;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.draw.*;

public class PneuBlock extends ComponentBlock {

    public float visualExplodePressure;
    public float visualMaxOperatePressure;
    public float visualMinOperatePressure;
    public float leakPointPressure;

    public DrawBlock drawer = new DrawDefault();

    public int explosionRadius = 3;
    public int explosionDamage = 50;
    public Effect explodeEffect = CDFx.pneuSmoke;
    public Sound explodeSound = Sounds.explosion;

    public float explosionShake = 6f, explosionShakeDuration = 16f;

    public PneuBlock(String name) {
        super(name);
        update = true;
    }

    @Override
    public void init() {
        super.init();
        visualExplodePressure = component.visualExplodePressure;
        visualMaxOperatePressure = component.visualMaxOperatePressure;
        visualMinOperatePressure = component.visualMinOperatePressure;
        leakPointPressure = component.leakPointPressure;
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("pressure",
                (PneuBlockBuild entity) -> new Bar(
                        () -> Core.bundle.format("bar.pressureamount", (int) entity.pressure),
                        () -> Pal.lightOrange, () -> entity.pressure / visualExplodePressure));
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    public class PneuBlockBuild extends ComponentBuild implements PneuInterface {
        public float pressure;

        public float pressure() {
            return pressure;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

    }

}
