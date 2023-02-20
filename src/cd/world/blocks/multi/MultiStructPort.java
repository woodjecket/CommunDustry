package cd.world.blocks.multi;

import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

public class MultiStructPort extends Block{

    public boolean isInputItem =true;
    public boolean isInputLiquid=true;
    public boolean isOutputItem=false;
    public boolean isOutputLiquid = false;
    public MultiStructPort(String name){
        super(name);
        update = true;
        hasItems=hasLiquids=true;
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
            return isInputItem && connectParent != null && connectParent.block.consumesItem(item) && items.get(item) + connectParent.items.get(item) < getMaximumAccepted(item) + connectParent.getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return isInputLiquid && connectParent != null && !connectParent.dead && connectParent.acceptLiquid(source, liquid);
        }

        @Override
        public void updateTile(){
            super.updateTile();
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
                        Vars.ui.showLabel(Strings.format("tA:@,pA:@,tR:@,pR:@,aR:@,tB:@,pB:@", thisAmount, parentAmount, thisRemains, parentRemains, arrangeRemains, thisTobeChange, parentToBeChange), 1, x, y);
                        if(liquids.get(liquid) - thisTobeChange >= 0){
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
                            items.remove(item, 1);
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
                        if(liquids.get(liquid)>0) dumpLiquid(liquid);
                    }
                }
            }
        }
    }
}
