package cd.type.blocks.laser;

import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

import static arc.math.Mathf.sign;
import static mindustry.Vars.*;

public class DirectLaserEnd extends Block{

    public int range = 5;
    public float laserBaseEnergy = 10f;
    public int attenuateRange = 3;
    public float attenuatePercent = 0.05f;

    public Color laserColor1 = Color.white;
    public Color laserColor2 = Color.valueOf("ffd9c2");
    public float pulseScl = 7, pulseMag = 0.05f;
    public float laserBaseWidth = 0.4f;


    public DirectLaserEnd(String name){
        super(name);
        allowDiagonal = false;
        drawArrow = true;
        update = true;
        rotate = true;
        rotateDraw = true;
    }


    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){

        for(int i = 0; i < 4; i++){
            int len = 0;
            Building from = null;
            var dir = Geometry.d4[i];
            int dx = dir.x, dy = dir.y;
            int offset = size / 2;
            for(int j = 1 + offset; j <= range + offset; j++){
                var other = world.build(x + j * dir.x, y + j * dir.y);

                //hit insulated wall
                if(other != null && !(other instanceof LaserInterface)){
                    break;
                }

                if(other != null && other.team == player.team() && (((LaserInterface)other).canProvide(x, y))){
                    len = j;
                    from = other;
                    break;
                }
            }

            if(from != null){
                Drawf.dashLine(Pal.place,
                x * tilesize + dx * (tilesize * size / 2f + 2),
                y * tilesize + dy * (tilesize * size / 2f + 2),
                x * tilesize + dx * (len) * tilesize,
                y * tilesize + dy * (len) * tilesize
                );
                Drawf.square(from.x, from.y, from.block.size * tilesize / 2f + 2.5f, 0f, Pal.place);
            }
        }
    }

    @Override
    public void setBars(){
        super.setBars();
        //addBar("batteries", PowerNode.makeBatteryBalance());
    }

    @Override
    public void init(){
        super.init();

        updateClipRadius((range + 1) * tilesize);
    }

    public class DirectLaserProviderBuild extends Building implements LaserInterface{
        public Seq<Building> laserParent = new Seq<>();
        public Building laserChild;
        public Tile laserChildTile;
        public float laserEnergy;

        @Override
        public void addLaserParent(Building b){
            laserParent.add(b);
        }

        @Override
        public void removeLaserParent(Building b){
            laserParent.remove(b);
        }

        @Override
        public void changeLaserEnergy(float c){
            laserEnergy += c;
        }

        @Override
        public boolean isAbsorbLaserEnergy(int bx, int by){
            var dir = Geometry.d4[rotation];
            return ((tile.x == bx) ? 0 : sign(bx - tile.x)) == dir.x && ((tile.y == by) ? 0 : sign(by - tile.y)) == dir.y;
        }

        @Override
        public Building getLaserChild(){
            return laserChild;
        }

        @Override
        public void setLaserChild(Building b){
            laserChild = b;
        }

        @Override
        public boolean canProvide(int bx, int by){
            return false;
        }

        @Override
        public void updateTile(){
        }


        @Override
        public int laserRange(){
            return range;
        }

        @Override
        public void pickedUp(){
            laserChild = null;
            laserChildTile = null;
        }


    }
}
