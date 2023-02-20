package cd.world.component;

import arc.graphics.g2d.*;
import arc.util.*;
import cd.entities.building.*;
import cd.world.blocks.multi.*;
import cd.world.blocks.multi.MultiStructPort.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;

import java.util.concurrent.atomic.*;

import static mindustry.Vars.tilesize;

public class MainMultiComponent extends BaseComponent{
    private IMultiData data = new OMultiData();

    /** I did NOT check if the position is null! */
    public static boolean isPosAtLowerLeft(int tx, int ty, Building b){
        float size = b.block.size;
        float offset = size % 2 == 0 ? 1f : 0.5f;
        return Vars.world.build(tx, ty).tileX() - size / 2f + offset == tx && Vars.world.build(tx, ty).tileY() - size / 2f + offset == ty;
    }

    @Override
    public void onUpdateTile(Building b){
        super.onUpdateTile(b);
        b.enabled = structDone(b);
        if(b instanceof IMulti m){
            for(var item : Vars.content.items()){
                if(b.items.has(item)){
                    for(var multiStructPortBuild : m.getPorts()){
                        if(((MultiStructPort)multiStructPortBuild.block).isOutputItem && multiStructPortBuild.items.get(item) < multiStructPortBuild.block.itemCapacity){
                            b.items.remove(item, 1);
                            Log.info("removed @", item);
                            multiStructPortBuild.items.add(item, 1);
                            Log.info("added @", item);
                        }
                    }
                }
            }
            for(var liquid : Vars.content.liquids()){
                if(b.liquids.get(liquid) > 0){
                    for(var build : m.getPorts()){
                        if(((MultiStructPort)build.block).isOutputLiquid && build.liquids.get(liquid) < build.block.liquidCapacity){
                            float remains = build.block.liquidCapacity - build.liquids.get(liquid);
                            remains = Math.max(remains, b.liquids.get(liquid));
                            b.liquids.remove(liquid, remains);
                            build.liquids.add(liquid, remains);
                        }
                    }
                }
            }
        }
    }

    /**
     * No the f**king central offset. Just count from the main block's tile center
     * (real center or real center's lower left, depends on size) to the lower-left to the block you want
     */
    public void dataOf(Object... a){
        data.valueOf(a);
    }

    private boolean structDone(Building build){
        AtomicBoolean b = new AtomicBoolean(true);
        data.getEachable().each(p -> {
            int ctx = build.tileX() + p.x, cty = build.tileY() + p.y;
            Building building = Vars.world.build(ctx, cty);
            if(building == null){
                b.set(false);
                return;
            }
            if(building.block == data.getByOffsetPos(p)){
                boolean b1 = building.block == data.getByOffsetPos(p) && isPosAtLowerLeft(ctx, cty, building) && b.get();
                b.set(b1);
                //Vars.ui.showLabel(Strings.format("x:@,y:@,null:@,block:@,want:@",ctx,cty, false, building.block,data.getByOffsetPos(p)),1f/60f,ctx*tilesize,cty*tilesize);
                if(b1 && building instanceof MultiStructPortBuild ms){
                    ms.connectParent = build;
                    if(build instanceof IMulti m) m.addPorts(ms);
                }
            }
        });
        return b.get();
    }

    @Override
    public void onEntityDraw(Building b){
        super.onEntityDraw(b);
        Draw.alpha(0.5f);
        data.getEachable().each(p -> {
            int ctx = b.tileX() + p.x, cty = b.tileY() + p.y;
            int bSize = data.getByOffsetPos(p).size;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            if(Vars.world.build(ctx, cty) != null && Vars.world.build(ctx, cty).block == data.getByOffsetPos(p)){
                if(!isPosAtLowerLeft(ctx, cty, Vars.world.build(ctx, cty))){
                    Draw.rect(data.getByOffsetPos(p).region, dx, dy, b.rotation);
                }
            }else{
                Draw.rect(data.getByOffsetPos(p).region, dx, dy, b.rotation);
            }
        });
    }

    @Override
    public void onDrawPlace(Block b, int x, int y, int rotation){
        super.onDrawPlace(b, x, y, rotation);
        data.getEachable().each(p -> {
            int ctx = x + p.x, cty = y + p.y;
            int bSize = data.getByOffsetPos(p).size;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            Draw.alpha(0.5f);
            Draw.rect(data.getByOffsetPos(p).region, dx, dy, rotation);
        });
    }
}
