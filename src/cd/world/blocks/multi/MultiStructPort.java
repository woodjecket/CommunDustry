package cd.world.blocks.multi;

import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

public class MultiStructPort extends Block{
    public MultiStructPort(String name){
        super(name);
        update = true;
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
        public void handleItem(Building source, Item item){
            if((connectParent != null && connectParent.items.get(item)<connectParent.block.itemCapacity)||items.get(item)>=block.itemCapacity){
                connectParent.handleItem(source, item);
            }else{
                super.handleItem(source, item);
            }
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount) {
            Vars.ui.showLabel(Strings.format("x:@,y:@,1:@",tileX(),tileY(),(connectParent != null && connectParent.liquids.get(liquid)>=connectParent.block.liquidCapacity/2f)|| liquids.get(liquid)>= block.liquidCapacity),1f/60f,x,y);
            if((connectParent != null && connectParent.liquids.get(liquid)<connectParent.block.liquidCapacity) || liquids.get(liquid)>= block.liquidCapacity/2f){
                connectParent.handleLiquid(source, liquid,amount);
            }else{
                super.handleLiquid(source, liquid,amount);
            }
        }
    }
}
