package cd.world.blocks.unit;

import arc.math.geom.*;
import arc.util.*;
import cd.entities.unit.*;
import cd.type.unit.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

import java.lang.reflect.*;

import static mindustry.Vars.tilesize;

public class UnitStop extends Block{
    public int areaSize = 6;

    public UnitStop(String name){
        super(name);
        size |= 1;
        rotate = true;
        update = true;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
        x *= tilesize;
        y *= tilesize;
        x += offset;
        y += offset;

        Rect rect = getRect(Tmp.r1, x, y, rotation);

        Drawf.dashRect(valid ? Pal.accent : Pal.remove, rect);

    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        //overlapping construction areas not allowed; grow by a tiny amount so edges can't overlap either.
        Rect rect = getRect(Tmp.r1, tile.worldx() + offset, tile.worldy() + offset, rotation).grow(0.1f);
        return !Groups.build.contains(b -> b instanceof UnitStopBuild && getRect(Tmp.r2, b.x, b.y, b.rotation).overlaps(rect));
    }

    private Rect getRect(Rect rect, float x, float y, int rotation){
        rect.setCentered(x, y, areaSize * tilesize);
        float len = tilesize * (areaSize + size) / 2f;

        rect.x += Geometry.d4x(rotation) * len;
        rect.y += Geometry.d4y(rotation) * len;

        return rect;
    }

    public class UnitStopBuild extends Building{
        public BlockOnUnit captured;
        public Rect captureZone = getRect(new Rect(), x, y, rotation);

        @Override
        public void drawSelect(){
            super.drawSelect();
            Drawf.dashRect(Pal.accent, captureZone);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            if(captured.toStop && !captured.stopped){
                //Stop it
                if(canStop(captured)){
                    Point2 origin = getOrigin();
                    captured.tiles.eachTile(t -> {
                        var worldTile = Vars.world.tile(t.x + origin.x, t.y + origin.y);
                        cutTo(t, worldTile);
                        if(Groups.build.contains(b -> b == worldTile.build)){
                            Groups.build.add(worldTile.build);
                            Groups.all.add(worldTile.build);
                        }
                        captured.builds.remove(worldTile.build);
                    });
                    captured.tiles.fill();
                }
                captured.stopped = true;
            }
            if(!captured.toStop && captured.stopped){
                //Start it
                if(canStart(captured)){
                    Point2 origin = getOrigin();
                    captured.tiles.fill();
                    captured.tiles.each((ux, uy) -> {
                        var worldTile = Vars.world.tile(ux + origin.x, uy + origin.y);
                        cutTo(worldTile, captured.tiles.get(ux, uy));
                        Groups.build.remove(worldTile.build);
                        Groups.all.remove(worldTile.build);
                        captured.builds.addUnique(worldTile.build);
                    });
                }
                captured.stopped = false;
            }
        }

        private void cutTo(Tile from, Tile to){
            to.build = from.build;
            if(from.build != null){
                from.build.tile = to;
            }
            setBlock(to, from.block());
            from.build = null;


        }

        private void setBlock(Tile tile, Block block){
            try{
                Field field = tile.getClass().getDeclaredField("block");
                field.setAccessible(true);
                field.set(tile, block);
            }catch(NoSuchFieldException | IllegalAccessException e){
                throw new RuntimeException(e);
            }

        }

        private Point2 getOrigin(){
            Point2 rot = Geometry.d4[rotation];
            Point2 tail = Geometry.d4[(rotation + 3) % 4];
            Point2 side = new Point2(tileX() + rot.x * (block.size - 1) / 2,
            tileY() + rot.y * (block.size - 1) / 2
            );
            int size1 = ((BlockOnUnitType)captured.type).size;
            int size1Offset = (size1 | 1) >> 2;
            return new Point2(side.x + rot.x * size1 + tail.x * size1Offset,
            side.y + rot.y * size1 + tail.y * size1Offset);
        }


        private boolean canStop(BlockOnUnit captured){
            return true;
        }


        private boolean canStart(BlockOnUnit captured){
            return true;
        }
    }
}
