package cd.type.blocks;

import arc.*;
import cd.entities.component.*;
import cd.type.blocks.pneumatic.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;

public class ComponentCrafter extends GenericCrafter{
    public BaseComponent component;
    public boolean hasPressure;

    public ComponentCrafter(String name){
        super(name);

    }

    @Override
    public void init(){
        super.init();
        component.onInit(this);

    }

    @Override
    public void setBars(){
        if(hasPressure){
            addBar("pressure",
            (ComponentCrafterBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pressure-amount", entity.pressure),
            () -> Pal.lightOrange, () -> entity.pressure / component.getVisualExplodePressure()));
        }
    }

    @Override
    public void setStats(){
        super.setStats();
        component.onSetStats(this);
    }

    public class ComponentCrafterBuild extends GenericCrafterBuild implements PneuInterface{
        //define pneumatic
        public float pressure;

        public float pressure(){
            return pressure;
        }

        public float getPressure(){
            return pressure;
        }

        public void setPressure(float p){
            this.pressure = p;
        }

        @Override
        public boolean shouldConsume(){
            return super.shouldConsume() && component.onShouldConsume(this);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            component.onUpdateTile(this);
        }

        @Override
        public float efficiencyScale(){
            return component.onEfficiencyScale(this);
        }

        @Override
        public void craft(){
            super.craft();
            component.onCraft(this);
        }
    }

}

