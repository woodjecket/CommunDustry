package cd.world.component;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import cd.entities.building.*;
import cd.world.blocks.multi.*;
import cd.world.blocks.multi.MultiStructPort.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import java.util.concurrent.atomic.*;

import static mindustry.Vars.tilesize;

public class MainMultiComponent extends BaseComponent{
    private IMultiData data = new OMultiData();
    private ObjectMap<Liquid, Point2> liquidOutputPos = new ObjectMap<>();

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
            b.items.each((item, i) -> {
                m.getPorts().filter(port -> port.canInputItem(item))
                .each(port -> {
                    b.items.remove(item, 1);
                    port.items.add(item, 1);
                });
            });
            b.liquids.each((liquid, i) -> {
                m.getPorts().filter(port -> port.canInputLiquid(liquid) && port.offsetPos.equals(liquidOutputPos.get(liquid)))
                .each(port -> {
                    float remains = port.block.liquidCapacity - port.liquids.get(liquid);
                    remains = Math.max(remains, b.liquids.get(liquid));
                    b.liquids.remove(liquid, remains);
                    port.liquids.add(liquid, remains);
                });
            });
        }
    }

    /**
     * No the f**king central offset. Just count from the main block's tile center
     * (real center or real center's lower left, depends on size) to the lower-left to the block you want
     */
    public void dataOf(Object... a){
        data.valueOf(a);
    }

    public void directionOf(Object... a){
        for(int i = 0; i < a.length - 1; i = i + 2){
            liquidOutputPos.put((Liquid)a[i], ((Point2)a[i + 1]).cpy());
        }
    }

    private boolean structDone(Building build){
        final int baseRotation = build.rotation;
        AtomicBoolean b = new AtomicBoolean(true);
        data.getEachable().each(p -> {
            int ctx = build.tileX() + p.x, cty = build.tileY() + p.y;
            Building building = Vars.world.build(ctx, cty);
            if(building == null){
                b.set(false);
                return;
            }
            if(building.block == data.getByPosRotation(p,baseRotation).getBlock()){
                Log.info("(@,@) @,@", ctx, cty, building.rotation, baseRotation);
                /*
                 * 0 0/1/2/3 0/1/2/3
                 * 1 0/1/2/3 1/2/3/0
                 * 2 0/1/2/3 2/3/0/1
                 * 3 0/1/2/3 3/0/1/2*/
                boolean b1 = isPosAtLowerLeft(ctx, cty, building) && b.get() && isProperRotation(baseRotation, p, building);
                b.set(b1);
                //Vars.ui.showLabel(Strings.format("x:@,y:@,ll:@,block:@,want:@,b:@",ctx,cty, isPosAtLowerLeft(ctx, cty, building), building.block,data.getByPosRotation(p,baseRotation).getBlock().b.get()),1f/60f,ctx*tilesize,cty*tilesize);
                if(b1 && building instanceof MultiStructPortBuild ms){
                    ms.connectParent = build;
                    if(build instanceof IMulti m) m.addPorts(ms, p);
                }
            }else b.set(false);
        });
        return b.get();
    }

    private boolean isProperRotation(int baseRotation, Point2 p, Building building){
        return !building.block.rotate || (building.rotation + baseRotation) % 4 == data.getByPosRotation(p,baseRotation).getRotation();
    }

    @Override
    public void onEntityDraw(Building b){
        final int baseRotation = b.rotation;
        super.onEntityDraw(b);
        Draw.alpha(0.5f);
        data.getEachable().each(p -> {
            int ctx = b.tileX() + p.x, cty = b.tileY() + p.y;
            int bSize = data.getByPosRotation(p,baseRotation).getBlock().size;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            if(Vars.world.build(ctx, cty) != null && Vars.world.build(ctx, cty).block == data.getByPosRotation(p,baseRotation).getBlock()){
                if(!isPosAtLowerLeft(ctx, cty, Vars.world.build(ctx, cty)) || !isProperRotation(baseRotation,p,Vars.world.build(ctx, cty))){
                    Draw.rect(data.getByPosRotation(p,baseRotation).getBlock().region, dx, dy, b.rotation * 90);
                }
            }else{
                Draw.rect(data.getByPosRotation(p,baseRotation).getBlock().region, dx, dy, b.rotation * 90);
            }
        });
    }

    @Override
    public void onDrawPlace(Block b, int x, int y, int rotation){
        super.onDrawPlace(b, x, y, rotation);
        data.getEachable().each(p -> {
            int ctx = x + p.x, cty = y + p.y;
            int bSize = data.getByPosRotation(p,rotation).getBlock().size;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            Draw.alpha(0.5f);
            Draw.rect(data.getByPosRotation(p,rotation).getBlock().region, dx, dy, rotation);
        });
    }

}
