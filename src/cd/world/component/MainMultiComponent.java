package cd.world.component;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import cd.entities.building.*;
import cd.world.blocks.multi.*;
import cd.world.blocks.multi.MultiStructPort.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import java.util.concurrent.atomic.*;

import static mindustry.Vars.tilesize;

@SuppressWarnings("SpellCheckingInspection")
public class MainMultiComponent extends BaseComponent{
    private IMultiData data = new OMultiData();
    private ObjectMap<Liquid, Point2> liquidOutputPos = new ObjectMap<>();

    //Single-thread ONLY!!!!!!!
    private Seq<MultiStructPortBuild> getOff = new Seq<>();

    private static boolean isSufficient(int checkX, int checkY, Building building, Building main){
        /*
         * AAAA EEEE IIIQ QOOO
         * AAAA EEEE IINI ONOO
         * ACAA ECNE ICII OCOO
         * QAAA EEEQ IIII OOOO
         * r:0  r:1  r:2  r:3  */

        //So the Point C is (tileX,tileY)
        int rotation = main.rotation;
        int size = building.block.size;
        int tileX = building.tileX(), tileY = building.tileY();
        //We want to check if the Point Q is (checkX, checkY), how to do it?
        //To use the Point N!

        //Find out the point N
        //Obviously, when the rotation is 0, the N is the C
        //let's imagine another block to see what is the distance from N to C

        /*AAAAAA EEEEEE IIIIIQ QOOOOO
         * AAAAAA EEEEEE IIIIII OOOOOO
         * AAAAAA EEEEEE IIINII OONOOO
         * AACAAA EECNEE IICIII OOCOOO
         * AAAAAA EEEEEE IIIIII OOOOOO
         * QAAAAA EEEEEQ IIIIII OOOOOO*/

        //So the distance from N to C is always the same
        //Let's use a switch

        //But the example above is just for the even size
        //For the odd size, Point N is just Point C

        int nearX = tileX, nearY = tileY;
        if(size % 2 == 0){
            switch(rotation){
                case 1 -> nearX = tileX + 1;
                case 2 -> {
                    nearX = tileX + 1;
                    nearY = tileY + 1;
                }
                case 3 -> nearY = tileY + 1;
            }
        }

        //Then, from N to Q is just a diagonal
        //The horizontal and vertical distance for even is the size's half and then minus 1
        //For odd, it's the size minus 1's half
        int distance = size % 2 == 0 ? size / 2 - 1 : (size - 1) / 2;

        int goodX = nearX, goodY = nearY;

        switch(rotation){
            case 0 -> {
                //left:(-,-)
                goodX -= distance;
                goodY -= distance;
            }
            case 1 -> {
                //up:(+,-)
                goodX += distance;
                goodY -= distance;
            }
            case 2 -> {
                //right:(+,+)
                goodX += distance;
                goodY += distance;
            }
            case 3 -> {
                //down:(-,+)
                goodX -= distance;
                goodY += distance;
            }
        }

        return goodX == checkX && goodY == checkY;

    }

    private static void returnCenter(int size, Point2 p, int baseRotation){
        if(size % 2 != 0) return;
        switch(baseRotation){
            case 1 -> p.x -= 1;
            case 2 -> p.y -= 1;
            case 3 -> p.x += 1;
        }
    }

    private static void reserveCenter(int size, Point2 p, int baseRotation){
        if(size % 2 != 0) return;
        switch(baseRotation){
            case 1 -> p.x += 1;
            case 2 -> p.y += 1;
            case 3 -> p.x -= 1;
        }
    }

    @Override
    public void onUpdateTile(Building b){
        super.onUpdateTile(b);
        b.enabled = structDone(b);
        if(b instanceof IMulti m){
            var m1 = m.getPorts();
            b.items.each((item, i) -> {
                m1.filter(port -> {
                    if(port.canOutputItem(item)){
                        return true;
                    }else{
                        getOff.add(port);
                        return false;
                    }
                })
                .each(port -> {
                    b.items.remove(item, 1);
                    port.items.add(item, 1);
                });
                m1.addAll(getOff);
                getOff.clear();
            });
            b.liquids.each((liquid, i) -> {
                //Log.info(liquid);
                m1.filter(port -> {
                    //Log.info(port + " " + (port.canOutputLiquid(liquid) && port.offsetPos.equals(liquidOutputPos.get(liquid))) + " " + liquid);
                    if(port.canOutputLiquid(liquid) && port.offsetPos.equals(liquidOutputPos.get(liquid))){
                        return true;
                    }
                    getOff.add(port);
                    return false;
                })
                .each(port -> {
                    float portRemains = port.block.liquidCapacity-port.liquids.get(liquid);
                    b.liquids.remove(liquid, Math.min(portRemains,i));
                    port.liquids.add(liquid, Math.min(portRemains,i));
                });
                m1.addAll(getOff);
                getOff.clear();
            });
        }
    }

    /**
     * The data of the multi-structure.
     * To define the data properly, make sure the main block is facing the right
     * (if your main block is not {@code rotate}, just build some conveyor facing the right).
     * <p>
     * Then make a rectangular coordinate system , with the main block's center as original point.
     * (the center is just the point (tileX,tileY) )
     * @param a
     * is in the order that the 1st and 2nd param per 3 is the position of the lower-left
     * in the system just now, the 3nd is the block, or so as to make a block has the
     * different rotation you can new a {@code RotatedBlock}
     */
    public void dataOf(Object... a){
        data.valueOf(a);
    }

    public void directionOf(Object... a){
        for(int i = 0; i < a.length - 1; i = i + 2){
            liquidOutputPos.put((Liquid)a[i], ((Point2)a[i + 1]).cpy());
        }
    }

    /**
     * The check is just for 3 steps
     * Step 1: Rotate the position. Just use the {@code rotate}
     * Step 2: Change the original point. The center of the odd-size block never changes,
     * but even-size is always changes. A fix is needed to fix the change.
     * Step 3: Check for the sufficient point. After the two steps above we need to reserve
     * the point so that the index can works. Then the lower-left point is always changes
     * so we have to fix it too.
     */
    private boolean structDone(Building build){
        final int baseRotation = build.rotation;
        AtomicBoolean b = new AtomicBoolean(true);
        data.getEachable().each(p -> {
            p.rotate(baseRotation);
            reserveCenter(build.block.size, p, baseRotation);
            int ctx = build.tileX() + p.x, cty = build.tileY() + p.y;
            returnCenter(build.block.size, p, baseRotation);
            p.rotate(-baseRotation);
            Building building = Vars.world.build(ctx, cty);
            if(building == null){
                b.set(false);
                return;
            }
            if(building.block == data.get(p).getBlock()){
                /*
                 * 0 0/1/2/3 0/1/2/3
                 * 1 0/1/2/3 1/2/3/0
                 * 2 0/1/2/3 2/3/0/1
                 * 3 0/1/2/3 3/0/1/2*/
                boolean b1 = isSufficient(ctx, cty, building, build) && b.get() && isProperRotation(baseRotation, p, building);
                b.set(b1);
                if(b1 && building instanceof MultiStructPortBuild ms){
                    ms.connectParent = build;
                    if(build instanceof IMulti m) m.addPorts(ms, p);
                }
            }else b.set(false);
        });
        return b.get();
    }

    private boolean isProperRotation(int baseRotation, Point2 p, Building building){
        return !building.block.rotate || (building.rotation - baseRotation) % 4 == data.get(p).getRotation();
    }

    @Override
    public void onEntityDraw(Building b){
        final int baseRotation = b.rotation;
        super.onEntityDraw(b);
        Draw.alpha(0.5f);
        data.getEachable().each(p -> {
            p.rotate(baseRotation);
            reserveCenter(b.block.size, p, baseRotation);
            int ctx = b.tileX() + p.x, cty = b.tileY() + p.y;
            returnCenter(b.block.size, p, baseRotation);
            p.rotate(-baseRotation);
            int bSize = data.get(p).getBlock().size;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            if(Vars.world.build(ctx, cty) != null && Vars.world.build(ctx, cty).block == data.get(p).getBlock()){
                if(!isSufficient(ctx, cty, Vars.world.build(ctx, cty), b) || !isProperRotation(baseRotation, p, Vars.world.build(ctx, cty))){
                    Draw.rect(data.get(p).getBlock().uiIcon, dx, dy, data.get(p).getRotation() * 90);
                }
            }else{
                Draw.rect(data.get(p).getBlock().uiIcon, dx, dy, data.get(p).getRotation() * 90);
            }
        });
    }

    @Override
    public void onDrawPlace(Block b, int x, int y, int rotation){
        super.onDrawPlace(b, x, y, rotation);
        data.getEachable().each(p -> {
            p.rotate(rotation);
            reserveCenter(b.size, p, rotation);
            int ctx = x + p.x, cty = y + p.y;
            returnCenter(b.size, p, rotation);
            p.rotate(-rotation);
            int bSize = data.get(p).getBlock().size;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            Draw.alpha(0.5f);
            Draw.rect(data.get(p).getBlock().uiIcon, dx, dy, rotation * 90);
        });
    }

}
