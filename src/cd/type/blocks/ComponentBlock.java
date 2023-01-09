package cd.type.blocks;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import arc.util.io.*;
import cd.entities.component.*;
import cd.type.blocks.pneumatic.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class ComponentBlock extends Block{
    public BaseComponent component = new BaseComponent();
    public boolean hasPressure;
    public DrawBlock drawer = new DrawDefault();

    public ComponentBlock(String name){
        super(name);
        update = true;
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
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
            (ComponentBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pressure-amount", entity.pressure),
            () -> Pal.lightOrange, () -> entity.pressure / component.getExplodePressure()));
        }
    }

    @Override
    public void setStats(){
        super.setStats();
        component.onSetStats(this);
    }

    public class ComponentBuild extends Building implements PneuInterface{
        //define pneumatic
        public float pressure;

        public float getPressure(){
            return pressure;
        }

        public void setPressure(float p){
            this.pressure = p;
        }

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            component.onUpdateTile(this);
        }


        @Override
        public void onDestroyed(){
            super.onDestroyed();
            component.onDestroyed(this);
        }

        public void createExplosion(){
            component.onCreateExplosion(this);
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
