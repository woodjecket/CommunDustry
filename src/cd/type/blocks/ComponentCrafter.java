package cd.type.blocks;

import arc.*;
import arc.util.io.*;
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
        sync = true;
    }

    @Override
    public void init(){
        super.init();
        component.onInit(this);
        hasPressure = component.hasPneu;

    }

    @Override
    public void setBars(){
        if(hasPressure){
            addBar("pressure",
            (ComponentCrafterBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pressure-amount", entity.pressure),
            () -> Pal.lightOrange, () -> entity.pressure / component.getExplodePressure()));
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

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            var buildNumber = read.i();
            if(buildNumber < 0) return;
            if(hasPressure) pressure = read.f();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(1);
            if(hasPressure) write.f(pressure);
        }
    }


}

