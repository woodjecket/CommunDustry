package cd.world.blocks.multi;

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
        public boolean full = false;

        @Override
        public boolean acceptItem(Building source, Item item){
            return connectParent != null && connectParent.block.consumesItem(item) && items.get(item) + connectParent.items.get(item) < getMaximumAccepted(item) + connectParent.getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return connectParent != null && !connectParent.dead && connectParent.acceptLiquid(source, liquid);
        }


        @Override
        public void handleItem(Building source, Item item){
            if((connectParent != null && connectParent.items.get(item) < connectParent.block.itemCapacity) || items.get(item) >= block.itemCapacity){
                connectParent.handleItem(source, item);
            }else{
                super.handleItem(source, item);
            }
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount){
            if(connectParent == null){
                super.handleLiquid(source, liquid, amount);
                return;
            }

            float thisAmount = liquids.get(liquid);
            float parentAmount = connectParent.liquids.get(liquid);
            //but if when this is smaller than that one
            //Suppose that this is 10 and that 20
            //our goal is to make the "rest" equals
            //so the param "amount" will got a correct value

            //Condition 1
            //this=8, that=12, amount=2, arrange=11
            //Obviously it wrong
            //in fact this remains 2L and that remains 8L and give 2L, so we need to make the rest 4L((2+8-2)/2)
            //so this is to be 6 and that is to be 16
            //take this 2L and give that 4L

            //Condition 2
            //this=2, that=18, amount=8
            //thisR=8, thatR=2, aR=6
            //thisC=2, thatC=-4
            float thisRemains = block.liquidCapacity - thisAmount;
            float parentRemains = connectParent.block.liquidCapacity - parentAmount;
            float arrangeRemains = (thisRemains + parentRemains - amount) / 2f;

            float thisTobeChange = thisRemains - arrangeRemains;
            float parentToBeChange = parentRemains - arrangeRemains;

            super.handleLiquid(source, liquid, thisTobeChange);
            connectParent.handleLiquid(source, liquid, parentToBeChange);


        }
    }
}
