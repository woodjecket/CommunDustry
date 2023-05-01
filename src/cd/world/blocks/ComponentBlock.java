package cd.world.blocks;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import cd.entities.building.*;
import cd.world.blocks.multi.structure.MultiStructPort.*;
import cd.world.component.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class ComponentBlock extends Block implements IComp{
    public boolean hasPressure, hasLaser;
    public DrawBlock drawer = new DrawDefault();
    private ObjectMap<Class<? extends BaseComponent>, BaseComponent> comps = new ObjectMap<>();

    public ComponentBlock(String name){
        super(name);
        update = true;
    }

    @Override
    public ObjectMap<Class<? extends BaseComponent>, BaseComponent> components(){
        return comps;
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
        executeAllComps(BaseComponent::onLoad);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
        executeAllComps(c -> c.onDrawPlace(this, x, y, rotation));
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
        executeAllComps(c -> c.onInit(this));
        hasPressure = (getComp(PneuComponent.class) != null);
        hasLaser = (getComp(LaserComponent.class) != null);
    }

    @Override
    public void setBars(){
        super.setBars();
        executeAllComps(c -> c.onSetBars(this));
    }

    @Override
    public void setStats(){
        super.setStats();
        executeAllComps(c -> c.onSetStats(this));
    }


    public class ComponentBuild extends Building implements IPneu, ILaser, IMulti{
        //define pneumatic
        public float pressure;
        /** It can only have one child who can be given laser energy to. */
        public Building laserChild;
        /*Multi-port*/
        public Seq<MultiStructPortBuild> ports;
        /** The laser energy now */
        public float laserEnergy;

        public float getPressure(){
            return pressure;
        }

        public void setPressure(float pressure){
            this.pressure = pressure;
        }

        @Override
        public void draw(){
            drawer.draw(this);
            executeAllComps(c -> c.onEntityDraw(this));
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            executeAllComps(c -> c.onUpdateTile(this));
        }

        @Override
        public void placed(){
            super.placed();
            executeAllComps(c -> c.onPlace(this));
        }

        @Override
        public void onDestroyed(){
            super.onDestroyed();
            executeAllComps(c -> c.onDestroyed(this));
        }

        public void createExplosion(){
            executeAllComps(c -> c.onCreateExplosion(this));
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

        public int getLaserRange(){
            return getComp(LaserComponent.class).getLaserRange();
        }


        public boolean isAcceptLaserEnergy(){
            return getComp(LaserComponent.class) != null && getComp(LaserComponent.class).isAcceptLaserEnergy();
        }

        public boolean isProvideLaserEnergy(int bx, int by){
            return getComp(LaserComponent.class) != null && getComp(LaserComponent.class).isProvideLaserEnergy(this, bx, by);
        }

        public Building getLaserChild(){
            return laserChild;
        }

        public void setLaserChild(Building b){
            laserChild = b;
        }

        public void changeLaserEnergy(float c){
            laserEnergy += c;
        }

        public float getLaserEnergy(){
            return laserEnergy;
        }

        @Override
        public void setLaserEnergy(float energy){
            laserEnergy = energy;
        }

        @Override
        public void addPorts(MultiStructPortBuild b, Point2 p){
            if(!ports.contains(b)){
                ports.add(b);
                b.offsetPos = p;
            }
        }

        @Override
        public Seq<MultiStructPortBuild> getPorts(){
            return ports;
        }
    }

}
