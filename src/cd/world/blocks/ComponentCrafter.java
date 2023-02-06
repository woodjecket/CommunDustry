package cd.world.blocks;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.io.*;
import cd.world.component.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.*;

@SuppressWarnings("unchecked")
public class ComponentCrafter extends GenericCrafter implements ComponentInterface{
    public boolean hasPressure, hasLaser;
    private ObjectMap<Class<? extends BaseComponent>, BaseComponent> comps = new ObjectMap<>();

    public ComponentCrafter(String name){
        super(name);
        sync = true;
    }

    @Override
    public void init(){
        super.init();
        executeAllComps(c -> c.onInit(this));
        hasPressure = (getComp(PneuComponent.class) != null);
        hasLaser = (getComp(LaserEnergyComponent.class) != null);

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

    public <C extends BaseComponent> C getComp(Class<C> type){
        if(!comps.containsKey(type)) return null;
        return (comps.get(type) != null) ? (C)comps.get(type) : null;
    }

    public void addComp(BaseComponent... c){
        for(var sc : c){
            var type = sc.getClass();
            if(type.isAnonymousClass()){
                type = (Class<? extends BaseComponent>)type.getSuperclass();
            }
            comps.put(type, sc);
        }
    }

    public <T extends BaseComponent> void removeComp(Class<T> type){
        comps.remove(type);
    }

    public Iterable<BaseComponent> listComps(){
        return comps.values();
    }

    public void executeAllComps(Cons<? super BaseComponent> operator){
        for(var i : listComps()){
            operator.get(i);
        }
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

        public int laserRange(){
            return getComp(LaserEnergyComponent.class).getLaserRange();
        }


        public boolean isAcceptLaserEnergy(){
            return getComp(LaserEnergyComponent.class) != null && getComp(LaserEnergyComponent.class).isAcceptLaserEnergy();
        }

        public boolean isProvideLaserEnergy(int bx, int by){
            return getComp(LaserEnergyComponent.class) != null && getComp(LaserEnergyComponent.class).isProvideLaserEnergy(this, bx, by);
        }

        public int getLastChange(){
            return lastChange;
        }

        public void setLastChange(int t){
            lastChange = t;
        }

        public void addLaserParent(Building b){
            laserParent.add(b);
        }

        public void removeLaserParent(Building b){
            laserParent.remove(b);
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
    }


}

