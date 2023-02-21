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

    /**
     * I did NOT check if the position is null!
     * Good point means the point of the lower left when the block's rotation is 0(left), changes with the rotation
     */
    @Deprecated
    public static boolean isAtGoodPoint(int tx, int ty, Building b){
        float offset = (float)b.block.size % 2 == 0 ? 1f : 0.5f;
        float change = b.block.size / 2f - offset;
        float vx = switch(b.rotation){
            case 0, 1 -> Vars.world.build(tx, ty).tileX() - change;
            case 2, 3 -> Vars.world.build(tx, ty).tileX() + change;
            default -> throw new IllegalArgumentException("How did you make the rotation out of 0-3?");
        };
        float offset1 = (float)b.block.size % 2 == 0 ? 1f : 0.5f;
        float change1 = b.block.size / 2f - offset1;
        float vy = switch(b.rotation){
            case 0, 3 -> Vars.world.build(tx, ty).tileY() - change1;
            case 1, 2 -> Vars.world.build(tx, ty).tileY() + change1;
            default -> throw new IllegalArgumentException("How did you make the rotation out of 0-3?");
        };
        return vx == tx && vy == ty;
    }

    private static float getCentralX(int ctx, int bSize, int rotation){
        return switch(rotation){
            case 0, 1 -> ctx + bSize / 2f - 0.5f;
            case 2, 3 -> ctx - bSize / 2f + 0.5f;
            default -> throw new IllegalArgumentException("How did you make the rotation out of 0-3?");
        };
    }

    private static float getCentralY(int cty, int bSize, int rotation){
        return switch(rotation){
            case 0, 3 -> cty + bSize / 2f - 0.5f;
            case 1, 2 -> cty - bSize / 2f + 0.5f;
            default -> throw new IllegalArgumentException("How did you make the rotation out of 0-3?");
        };
    }

    public static boolean isSufficient(int checkX, int checkY, Building building, Building main){
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

        return goodX == checkX && goodY== checkY;

    }

    @Override
    public void onUpdateTile(Building b){
        super.onUpdateTile(b);
        b.enabled = structDone(b);
        if(b instanceof IMulti m){
            var m1 = m.getPorts();
            b.items.each((item, i) -> {
                m1.filter(port -> {
                    if(port.canInputItem(item)){
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
                    //Log.info(port);
                    float remains = port.block.liquidCapacity - i;
                    remains = Math.max(remains, i);
                    b.liquids.remove(liquid, remains);
                    port.liquids.add(liquid, remains);
                });
                m1.addAll(getOff);
                getOff.clear();
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
        //Log.info(liquidOutputPos);
    }

    private boolean structDone(Building build){
        final int baseRotation = build.rotation;
        AtomicBoolean b = new AtomicBoolean(true);
        data.getEachable().each(p -> {
            p.rotate(baseRotation);
            reserveCenter(build.block.size,p,baseRotation);
            int ctx = build.tileX() + p.x, cty = build.tileY() + p.y;
            returnCenter(build.block.size,p,baseRotation);
            p.rotate(-baseRotation);
            Building building = Vars.world.build(ctx, cty);
            if(building == null){
                b.set(false);
                return;
            }
            if(building.block == data.get(p).getBlock()){
                //Log.info("(@,@) @,@", ctx, cty, building.rotation, baseRotation);
                /*
                 * 0 0/1/2/3 0/1/2/3
                 * 1 0/1/2/3 1/2/3/0
                 * 2 0/1/2/3 2/3/0/1
                 * 3 0/1/2/3 3/0/1/2*/
                boolean b1 = isSufficient(ctx, cty, building,build) && b.get() && isProperRotation(baseRotation, p, building);
                b.set(b1);
                //Vars.ui.showLabel(Strings.format("x:@,y:@,ll:@,block:@,want:@,b:@",ctx,cty, isPosAtLowerLeft(ctx, cty, building), building.block,data.getByOffsetPos(p).getBlock().b.get()),1f/60f,ctx*tilesize,cty*tilesize);
                if(b1 && building instanceof MultiStructPortBuild ms){
                    ms.connectParent = build;
                    if(build instanceof IMulti m) m.addPorts(ms, p);
                }
            }else b.set(false);
        });
        return b.get();
    }

    private static void returnCenter(int size,Point2 p, int baseRotation){
        if(size %2 != 0) return;
        switch(baseRotation){
            case 1 ->p.x-=1;
            case 2->p.y-=1;
            case 3->p.x+=1;
        }
    }

    private static void reserveCenter(int size, Point2 p, int baseRotation){
        if(size %2 != 0) return;
        switch(baseRotation){
            case 1 ->p.x+=1;
            case 2->p.y+=1;
            case 3->p.x-=1;
        }
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
            reserveCenter(b.block.size,p,baseRotation);
            int ctx = b.tileX() + p.x, cty = b.tileY() + p.y;
            returnCenter(b.block.size,p, baseRotation);
            p.rotate(-baseRotation);
            int bSize = data.get(p).getBlock().size;
            ///float dx = getGoodX(ctx, cty, bSize, data.get(p).getRotation()) * tilesize, dy = getGoodY(ctx, cty, bSize, data.get(p).getRotation()) * tilesize;
            //float dx = getCentralX(ctx, bSize, data.get(p).getRotation()) * tilesize, dy = getCentralY(cty, bSize, data.get(p).getRotation()) * tilesize;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            if(Vars.world.build(ctx, cty) != null && Vars.world.build(ctx, cty).block == data.get(p).getBlock()){
                if(!isSufficient(ctx, cty, Vars.world.build(ctx, cty),b) || !isProperRotation(baseRotation, p, Vars.world.build(ctx, cty))){
                    Draw.rect(data.get(p).getBlock().uiIcon, dx, dy, b.rotation * 90);
                }
            }else{
                Draw.rect(data.get(p).getBlock().uiIcon, dx, dy, b.rotation * 90);
            }
        });
    }

    @Override
    public void onDrawPlace(Block b, int x, int y, int rotation){
        super.onDrawPlace(b, x, y, rotation);
        data.getEachable().each(p -> {
            p.rotate(rotation);
            reserveCenter(b.size,p,rotation);
            int ctx = x + p.x, cty = y + p.y;
            returnCenter(b.size,p,rotation);
            p.rotate(-rotation);
            int bSize = data.get(p).getBlock().size;
            float dx = (ctx + bSize / 2f - 0.5f) * tilesize, dy = (cty + bSize / 2f - 0.5f) * tilesize;
            Draw.alpha(0.5f);
            Draw.rect(data.get(p).getBlock().uiIcon, dx, dy, rotation * 90);
        });
    }

}
