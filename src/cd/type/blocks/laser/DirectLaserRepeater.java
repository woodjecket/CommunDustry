package cd.type.blocks.laser;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

import static arc.math.Mathf.sign;
import static mindustry.Vars.*;

@SuppressWarnings("ALL")
public class DirectLaserRepeater extends Block{

    public int range = 5;
    public float laserBaseEnergy = 10f;
    public int attenuateRange = 3;
    public float attenuatePercent = 0.05f;

    public Color laserColor1 = Color.white;
    public Color laserColor2 = Color.valueOf("ffd9c2");
    public float pulseScl = 7, pulseMag = 0.05f;
    public float laserBaseWidth = 0.4f;

    public TextureRegion laser, laserEnd;

    public DirectLaserRepeater(String name){
        super(name);
        allowDiagonal = false;
        drawArrow = true;
        update = true;
        rotate = true;
        rotateDraw = true;
    }

    @Override
    public void load(){
        super.load();
        laser = Core.atlas.find(content.transformName(name) + "-beam", "power-beam");
        laserEnd = Core.atlas.find(content.transformName(name) + "-beam-end", "power-beam-end");

    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        int maxLen = range + size / 2;
        Building dest = null;
        var dir = Geometry.d4[rotation];
        int dx = dir.x, dy = dir.y;
        int offset = size / 2;
        for(int j = 1 + offset; j <= range + offset; j++){
            var other = world.build(x + j * dir.x, y + j * dir.y);
            if(other != null && other.team == player.team() && other instanceof LaserInterface && ((LaserInterface)other).isAbsorbLaserEnergy(x, y)){
                maxLen = j;
                dest = other;
                break;
            }
            if(other != null && !(other instanceof LaserInterface)){
                break;
            }
        }
        Drawf.dashLine(Pal.placing,
        x * tilesize + dx * (tilesize * size / 2f + 2),
        y * tilesize + dy * (tilesize * size / 2f + 2),
        x * tilesize + dx * (maxLen) * tilesize,
        y * tilesize + dy * (maxLen) * tilesize
        );


        if(dest != null){
            Drawf.square(dest.x, dest.y, dest.block.size * tilesize / 2f + 2.5f, 0f);
        }

        for(int i = 0; i < 4; i++){
            int len = 0;
            Building from = null;
            dir = Geometry.d4[i];
            dx = dir.x;
            dy = dir.y;
            offset = size / 2;
            for(int j = 1 + offset; j <= range + offset; j++){
                var other = world.build(x + j * dir.x, y + j * dir.y);

                //hit insulated wall
                if(other != null && !(other instanceof LaserInterface)){
                    break;
                }

                if(other != null && other.team == player.team() && ((LaserInterface)other).canProvide(x, y)){
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
        public int lastChange = -2;

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
            return true;
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
            var dir = Geometry.d4[rotation];
            if(!(((tile.x == bx) ? 0 : sign(bx - tile.x)) == dir.x && ((tile.y == by) ? 0 : sign(by - tile.y)) == dir.y)) return false;
            if(laserChildTile == null) return true;
            int nowDst = Math.max(Math.abs(bx - tile.x), Math.abs(by - tile.y));
            int dst = Math.max(Math.abs(laserChildTile.x - tile.x), Math.abs(laserChildTile.y - tile.y));
            return nowDst < dst;
        }

        @Override
        public void updateTile(){
            //世界每次有方块更新就让world.tileChanges变化，此处达到每次更新才重新计算的效果
            if(lastChange != world.tileChanges){
                lastChange = world.tileChanges;
                updateChild();
            }
        }

        public void updateChild(){
            //先前的子节点
            var prev = laserChild;
            //方向乘数
            var dir = Geometry.d4[rotation];
            //置零
            laserChild = null;
            //偏移一点
            int offset = size / 2;
            //找方向范围内第一个带isAbsorbLaserEnergy的方块，j是距离
            //从中心开算，但是范围应该从边缘算，所以有偏移
            for(int j = 1 + offset; j <= range + offset; j++){
                //开启遍历，坐标x是此方块x+距离*方向乘数，y同理
                var other = world.build(tile.x + j * dir.x, tile.y + j * dir.y);

                if(other instanceof LaserInterface && !((LaserInterface)other).isAbsorbLaserEnergy(tile.x, tile.y)){
                    break;
                }

                if(other != null && other.team == Vars.player.team() && other instanceof LaserInterface && ((LaserInterface)other).isAbsorbLaserEnergy(tile.x, tile.y)){
                    laserChild = other;
                    laserChildTile = world.tile(tile.x + j * dir.x, tile.y + j * dir.y);
                    break;
                }
                if(other != null && !(other instanceof LaserInterface) && !other.block.underBullets){
                    break;
                }
            }
            //这个方向扫完了，把这个方向的连接存下来
            var next = laserChild;
            //对照一下，看看连接和刚才的是不是一个
            if(next != prev){
                //不是的话，看看原来是不是没有
                if(prev != null){
                    //不是没有还换了，原来的连接叫它把本方块的记录删了
                    ((LaserInterface)prev).removeLaserParent(this);
                    //然后自己也删了它的记录，双向删好友
                    removeLaserParent(prev);
                }

                //要是有现在的连接的话
                if(next != null){
                    //PowerModule记下他
                    setLaserChild(next);
                    //叫他也记下我
                    ((LaserInterface)next).addLaserParent(this);
                }
            }

        }

        @Override
        public int laserRange(){
            return range;
        }

        @Override
        public void draw(){
            super.draw();
            //如果设置里的激光可见度（laserOpacity）为0，还画个瘠铂
            if(Mathf.zero(Renderer.laserOpacity)) return;
            //宣告以下绘制在power层进行
            Draw.z(Layer.power);
            //画笔颜色根据电够不够混合一下
            Draw.color(laserColor1, laserColor2, (1f - laserEnergy / laserBaseEnergy) * 0.86f + Mathf.absin(3f, 0.1f));
            //画笔不透明度改成设置里的
            Draw.alpha(Renderer.laserOpacity);
            //算一算目前激光宽度（Width），基础加波动
            float w = laserBaseWidth + Mathf.absin(pulseScl, pulseMag);
            //四面开花
            //格子值存在，且（那个方块不是同类结点，格子的xy都和自己的不一样，格子后建造且他连接范围不比自己长，或者他连接范围就比自己短这四个条件满足其一）
            if(laserChild != null && (!(laserChild.block instanceof LaserInterface node) ||
            (laserChild.tileX() != tileX() && laserChild.tileY() != tileY()) ||
            (laserChild.id > id && range >= node.laserRange()) || range > node.laserRange())){
                //我和他之间，是x轴距离更长还是y轴，挑个长的赋值给dst
                int dst = Math.max(Math.abs(laserChildTile.x - tile.x), Math.abs(laserChildTile.y - tile.y));
                //距离还不到两人1/2，就是贴贴状态，就不发激光了
                if(dst > 1 + size / 2){
                    //还没贴贴，先看看方向乘数
                    var point = Geometry.d4[rotation];
                    //算算一半的格子宽
                    float poff = tilesize / 2f;
                    //第一个贴图是激光中间，第二个是头尾两边，从自己对应方向的边缘，到对方的边缘，宽度取刚才算的。就可以激光贴贴了
                    Drawf.laser(laser, laserEnd, x + poff * size * point.x, y + poff * size * point.y, laserChildTile.worldx() - poff * point.x, laserChildTile.worldy() - poff * point.y, w);
                }
            }


            Draw.reset();


        }

        @Override
        public void pickedUp(){
            laserChild = null;
            laserChildTile = null;
        }


    }
}