package cd.world.blocks;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.io.*;
import cd.entities.building.*;
import cd.world.blocks.multi.structure.MultiStructPort.*;
import cd.world.component.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.*;

public class ComponentCrafter extends GenericCrafter implements IComp{
    public boolean hasPressure, hasLaser;
    private ObjectMap<Class<? extends BaseComponent>, BaseComponent> comps = new ObjectMap<>();

    public ComponentCrafter(String name){
        super(name);
        sync = true;
    }

    @Override
    public ObjectMap<Class<? extends BaseComponent>, BaseComponent> components(){
        return comps;
    }

    @Override
    public void init(){
        super.init();
        executeAllComps(c -> c.onInit(this));
        hasPressure = (getComp(PneuComponent.class) != null);
        hasLaser = (getComp(LaserComponent.class) != null);

    }

    @Override
    public void load(){
        super.load();
        executeAllComps(BaseComponent::onLoad);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
        executeAllComps(c -> c.onDrawPlace(this, x, y, rotation));
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
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


    public class ComponentCrafterBuild extends GenericCrafterBuild implements IPneu, ILaser, IMulti{
        //define pneumatic
        public float pressure;
        /** But it can only have one child who can be given laser energy to. */
        public Building laserChild;
        /** The laser energy now */
        public float laserEnergy;
        private Seq<MultiStructPortBuild> ports = new Seq<>();


        public float getPressure(){
            return pressure;
        }

        public void setPressure(float pressure){
            this.pressure = pressure;
        }

        @Override
        public boolean shouldConsume(){
            var result = super.shouldConsume();
            for(var c : listComps()){
                result = result && c.onShouldConsume(this);
            }
            return result;
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
        public void draw(){
            drawer.draw(this);
            executeAllComps(c -> c.onEntityDraw(this));
        }

        @Override
        public float efficiencyScale(){
            float result = 1f;
            for(var c : listComps()){
                result *= c.onEfficiencyScale(this);
            }
            return result;
        }

        @Override
        public void craft(){
            super.craft();
            executeAllComps(c -> c.onCraft(this));
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
                //Log.info("@ is @",b,p);
            }
        }

        @Override
        public Seq<MultiStructPortBuild> getPorts(){
            return ports;
        }
    }


}

