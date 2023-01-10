package cd.type.blocks;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.io.*;
import cd.entities.component.*;
import cd.type.blocks.laser.*;
import cd.type.blocks.pneumatic.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;

public class ComponentCrafter extends GenericCrafter{
    public BaseComponent component;
    public boolean hasPressure, hasLaser;

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
    public void load(){
        super.load();
        component.onLoad();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
        component.onDrawPlace(this, x, y, rotation, valid);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
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


    public class ComponentCrafterBuild extends GenericCrafterBuild implements PneuInterface, LaserInterface{
        //define pneumatic
        public float pressure;
        /** A laser building might have one or more parents who give laser energy. */
        public Seq<Building> laserParent = new Seq<>();
        /** But it can only have one child who can be given laser energy to. */
        public Building laserChild;
        /** The laser energy now */
        public float laserEnergy;
        /** To make the laser block connect automatically. The working way seen below. */
        public int lastChange = -2;


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
            if(buildNumber < 2) return;
            if(hasLaser) laserEnergy = read.f();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(2);
            if(hasPressure) write.f(pressure);
            if(hasLaser) write.f(laserEnergy);
        }

        @Override
        public int laserRange(){
            return component.getLaserRange();
        }


        @Override
        public void addLaserParent(Building b){
            laserParent.add(b);
        }

        @Override
        public void removeLaserParent(Building b){
            laserParent.remove(b);
        }

        @Override
        public Building getLaserChild(){
            return laserChild;
        }

        @Override
        public void setLaserChild(Building b){
            laserChild = b;
        }

        @Override
        public void changeLaserEnergy(float c){
            laserEnergy += c;
        }

        @Override
        public boolean isAcceptLaserEnergy(int bx, int by){
            return component.isAcceptLaserEnergy(this, bx, by);
        }

        @Override
        public boolean isProvideLaserEnergy(int bx, int by){
            return component.isProvideLaserEnergy(this, bx, by);
        }

        @Override
        public int getLastChange(){
            return lastChange;
        }

        @Override
        public void setLastChange(int t){
            lastChange = t;
        }

        @Override
        public float getLaserEnergy(){
            return laserEnergy;
        }
    }


}

