package cd.world.blocks.multi;

import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

public class MultiStructPort extends Block{
    public MultiStructPort(String name){
        super(name);
        update = true;
    }

    @Override
    public void init(){
        super.init();
        removeBar("liquid");
    }

    public class MultiStructPortBuild extends Building{
        public Building connectParent;

        @Override
        public boolean acceptItem(Building source, Item item){
            return connectParent != null && connectParent.block.consumesItem(item) && items.get(item) + connectParent.items.get(item) < getMaximumAccepted(item) + connectParent.getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return connectParent != null && !connectParent.dead && connectParent.acceptLiquid(source, liquid);
        }



        @Override
        public void updateTile(){
            super.updateTile();
            if(connectParent != null){
                for(var liquid : Vars.content.liquids()){
                    if(!connectParent.block.liquidFilter[liquid.id]) continue;
                    float thisAmount = liquids.get(liquid);
                    float parentAmount = connectParent.liquids.get(liquid);
                    float thisRemains = block.liquidCapacity - thisAmount;
                    float parentRemains = connectParent.block.liquidCapacity - parentAmount;
                    float arrangeRemains = (thisRemains + parentRemains) / 2f;
                    float thisTobeChange = thisRemains - arrangeRemains;
                    float parentToBeChange = parentRemains - arrangeRemains;
                    //Vars.ui.showLabel(Strings.format("tA:@,pA:@,tR:@,pR:@,aR:@,tB:@,pB:@",thisAmount,parentAmount,thisRemains,parentRemains,arrangeRemains,thisTobeChange,parentToBeChange),1f/60f,x,y);
                    liquids.remove(liquid, -thisTobeChange);
                    connectParent.handleLiquid(this, liquid, parentToBeChange);
                }
                for(var item:Vars.content.items()){
                    if(!connectParent.block.itemFilter[item.id]) continue;
                    if(connectParent.items.get(item) > connectParent.block.itemCapacity) continue;
                    items.remove(item,1);
                    connectParent.handleItem(this,item);
                }
            }
        }
    }
}
