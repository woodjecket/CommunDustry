package cd.type.blocks.pneumatic;

import arc.Core;
import cd.type.blocks.ComponentBlock;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;

public class PneuConduit extends ComponentBlock {

    public float visualExplodePressure;
    public float visualMaxOperatePressure;
    public float visualMinOperatePressure;
    public float leakPointPressure;


    public PneuConduit(String name) {
        super(name);
        update = true;
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
    public void load(){
        super.load();
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("pressure",
                (PneuBlockBuild entity) -> new Bar(
                        () -> Core.bundle.format("bar.pressureamount", entity.pressure),
                        () -> Pal.lightOrange, () -> entity.pressure / visualExplodePressure));
    }

    public class PneuBlockBuild extends ComponentBuild implements PneuInterface {

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
