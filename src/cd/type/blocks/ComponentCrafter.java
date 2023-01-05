package cd.type.blocks;

import cd.entities.component.BaseComponent;
import cd.type.blocks.pneumatic.PneuInterface;
import mindustry.world.blocks.production.*;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.graphics.Pal;
import arc.Core;

public class ComponentCrafter extends GenericCrafter {
    public float visualExplodePressure = 15f;
    public BaseComponent component;  
    public boolean hasPressure;

    public ComponentCrafter(String name) {
        super(name);

    }

    @Override
    public void init() {
        super.init();
        component.onInit(this);

    }

    @Override
    public void setBars(){
        if(hasPressure){
        addBar("pressure",
                (ComponentCrafterBuild entity) -> new Bar(
                        () -> Core.bundle.format("bar.pressureamount", entity.pressure),
                        () -> Pal.lightOrange, () -> entity.pressure / visualExplodePressure));}
    }

    public class ComponentCrafterBuild extends GenericCrafterBuild implements PneuInterface{
        //define pneumatic
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

        @Override
        public boolean shouldConsume() {
            return super.shouldConsume() && component.onShouldConsume(this);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            component.onUpdateTile(this);
        }

        @Override
        public float efficiencyScale() {
            return component.onEfficiencyScale(this);
        }

        @Override
        public void craft(){
            super.craft();
            component.onCraft(this);
        }
    }

}

