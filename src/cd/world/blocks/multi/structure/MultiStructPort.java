package cd.world.blocks.multi.structure;

import arc.*;
import arc.math.geom.*;
import arc.util.io.*;
import cd.entities.building.*;
import cd.world.blocks.*;
import cd.world.component.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;

import static cd.world.component.PneuComponent.standardPressure;

public class MultiStructPort extends Block{


    public boolean isInputItem = true;
    public boolean isInputLiquid = true;
    public boolean isOutputItem;
    public boolean isOutputLiquid;
    public boolean isInputPressure, isInputLaser;
    public float maxLaserEnergy = 10f;

    public MultiStructPort(String name){
        super(name);
        update = true;
        hasItems = hasLiquids = true;
    }

    @Override
    public void init(){
        super.init();
        removeBar("liquid");
        addBar("pressure",
        (entity) -> new Bar(
        () -> Core.bundle.format("bar.pressure-amount", ((IPneu)entity).getPressure()),
        () -> Pal.lightOrange, () -> ((IPneu)entity).getPressure() / 20f));

    }

    public class MultiStructPortBuild extends Building implements ILaser, IPneu{
        public Building connectParent;
        public Point2 offsetPos;
        public float pressure;
        public float laserEnergy;

        public boolean isInputItem(){
            return isInputItem;
        }

        public boolean isInputLiquid(){
            return isInputLiquid;
        }

        public boolean isOutputLiquid(){
            return isOutputLiquid;
        }

        /**
         *
         */
        public boolean canOutputItem(Item item){
            return isOutputItem() && items.get(item) < block.itemCapacity;
        }

        public boolean isOutputItem(){
            return isOutputItem;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return isInputItem && connectParent != null && connectParent.block.consumesItem(item) &&
            items.get(item) + connectParent.items.get(item) < getMaximumAccepted(item) + connectParent.getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return isInputLiquid && connectParent != null && !connectParent.dead && connectParent.acceptLiquid(source, liquid);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            //liquids.each((l,i)-> Log.info(this +"\""+l.toString()+ i));
            if(connectParent != null){
                if(isInputLiquid){
                    for(var liquid : Vars.content.liquids()){
                        if(!connectParent.block.liquidFilter[liquid.id]) continue;
                        float thisAmount = liquids.get(liquid);
                        float parentAmount = connectParent.liquids.get(liquid);
                        float thisRemains = block.liquidCapacity - thisAmount;
                        float parentRemains = connectParent.block.liquidCapacity - parentAmount;
                        float arrangeRemains = (thisRemains + parentRemains) / 2f;
                        float thisTobeChange = thisRemains - arrangeRemains;
                        float parentToBeChange = parentRemains - arrangeRemains;
                        if(liquids.get(liquid) + thisTobeChange >= 0){
                            liquids.remove(liquid, -thisTobeChange);
                            connectParent.handleLiquid(this, liquid, parentToBeChange);
                        }else{
                            connectParent.handleLiquid(this, liquid, parentToBeChange - thisTobeChange);
                        }

                    }
                }
                if(isInputItem){
                    for(var item : Vars.content.items()){
                        if(!connectParent.block.itemFilter[item.id]) continue;
                        if(connectParent.items.get(item) >= connectParent.block.itemCapacity) continue;
                        if(items.has(item)){
                            //Log.info("Before: "+item+" "+items.get(item));
                            items.remove(item, 1);
                            //Log.info("After "+item+" "+items.get(item));
                            connectParent.handleItem(this, item);
                        }
                    }
                }
                if(isOutputItem){
                    for(var item : Vars.content.items()){
                        if(items.has(item)) dump(item);
                    }
                }
                if(isOutputLiquid){
                    for(var liquid : Vars.content.liquids()){
                        if(liquids.get(liquid) > 0) dumpLiquid(liquid);
                    }
                }
                if(isInputPressure && connectParent instanceof IPneu pneu){
                    if(connectParent.block instanceof IComp comp){
                        float min = Math.min(pressure - standardPressure,
                        comp.getComp(PneuComponent.class).maxOperatePressure - pneu.getPressure() - 0.1f);
                        if(pneu.getPressure() < comp.getComp(PneuComponent.class).maxOperatePressure){
                            pneu.setPressure(min + pneu.getPressure());
                            pressure -= min;
                        }
                    }
                }
                if(isInputLaser && connectParent instanceof ILaser laser){
                    if(connectParent.block instanceof IComp comp){
                        if(laser.getLaserEnergy() < comp.getComp(LaserComponent.class).maxLaserEnergy){
                            float min = Math.min(laserEnergy,
                            comp.getComp(LaserComponent.class).maxLaserEnergy - laser.getLaserEnergy());
                            laser.changeLaserEnergy(min);
                            laserEnergy -= min;
                        }
                    }
                }
            }
        }

        public boolean canOutputLiquid(Liquid liquid){
            return isOutputLiquid && liquids.get(liquid) < block.liquidCapacity;
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            int ignored = read.i();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(1);
        }

        @Override
        public int getLaserRange(){
            return 0;
        }

        @Override
        public Building getLaserChild(){
            return null;
        }

        @Override
        public void setLaserChild(Building b){
        }

        @Override
        public void changeLaserEnergy(float c){
            laserEnergy += c;
        }

        @Override
        public boolean isAcceptLaserEnergy(){
            return isInputLaser;
        }

        @Override
        public boolean isProvideLaserEnergy(int bx, int by){
            return false;
        }

        @Override
        public float getLaserEnergy(){
            return laserEnergy;
        }

        @Override
        public void setLaserEnergy(float energy){
            laserEnergy = energy;
        }

        @Override
        public float getPressure(){
            return pressure;
        }

        @Override
        public void setPressure(float pressure){
            this.pressure = pressure;
        }
    }


}
