package cd.world.component;

import arc.graphics.g2d.*;
import cd.world.multi.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;

import java.util.concurrent.atomic.*;

import static mindustry.Vars.tilesize;

public class MainMultiComponent extends BaseComponent{
    public IMultiData data = new OMultiData();

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
    }

    /**
     * No the f**king central offset. Just count from the main block's tile center
     * (real center or real center's lower left, depends on size) to the lower-left to the block you want
     */
    public void dataOf(Object... a){
        data.valueOf(a);
    }

    private boolean structDone(Posc build){
        AtomicBoolean b = new AtomicBoolean(true);
        data.getEachable().each(p -> {
            int ctx = build.tileX() + p.x, cty = build.tileY() + p.y;
            if(Vars.world.build(ctx, cty) == null){
                b.set(false);
                return;
            }
            if(Vars.world.build(ctx, cty).block == data.getByOffsetPos(p)){
                b.set(isPosAtLowerLeft(ctx, cty, Vars.world.build(ctx, cty)) && b.get());
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
