package cd.type.blocks.pneumatic;

import cd.type.blocks.ComponentCrafter;
import mindustry.ui.Bar;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import arc.Core;
import mindustry.graphics.Pal;

public class PneuCrafter extends ComponentCrafter {

    public float visualExplodePressure;
    public float visualMaxOperatePressure;
    public float visualMinOperatePressure;
    public float leakPointPressure;
    public DrawBlock drawer = new DrawDefault();

    public PneuCrafter(String name) {
        super(name);
    }


    @Override
    public void init() {
        super.init();
        visualExplodePressure = component.visualExplodePressure;
        visualMaxOperatePressure = component.maxOperatePressure;
        visualMinOperatePressure = component.minOperatePressure;
        leakPointPressure = component.leakPointPressure;
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("pressure",
                (PneuCrafterBuilding entity) -> new Bar(
                        () -> Core.bundle.format("bar.pressureamount", entity.pressure),
                        () -> Pal.lightOrange, () -> entity.pressure / visualExplodePressure));
    }


    public class PneuCrafterBuilding extends ComponentCrafterBuilding implements PneuInterface {
        public float pressure;

        public float pressure() {
            return pressure;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float p) {
            this.pressure = p;
        }
    }
    
}
