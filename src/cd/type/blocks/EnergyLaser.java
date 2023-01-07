package cd.type.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;

import java.util.*;

import static mindustry.Vars.*;

public class EnergyLaser extends PowerBlock{
    public int range = 5;

    public Color laserColor1 = Color.white;
    public Color laserColor2 = Color.valueOf("ffd9c2");
    public float pulseScl = 7, pulseMag = 0.05f;
    public float laserWidth = 0.4f;

    public TextureRegion laser, laserEnd;

    public EnergyLaser(String name){
        super(name);
        consumesPower = outputsPower = false;
        drawDisabled = false;
        envEnabled |= Env.space;
        allowDiagonal = false;
        underBullets = true;
        priority = TargetPriority.transport;
    }

    @Override
    public void setBars(){
        super.setBars();

        addBar("power", PowerNode.makePowerBalance());
        addBar("batteries", PowerNode.makeBatteryBalance());
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.powerRange, range, StatUnit.blocks);
    }

    @Override
    public void init(){
        super.init();

        updateClipRadius((range + 1) * tilesize);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        for(int i = 0; i < 4; i++){
            int maxLen = range + size / 2;
            Building dest = null;
            var dir = Geometry.d4[i];
            int dx = dir.x, dy = dir.y;
            int offset = size / 2;
            for(int j = 1 + offset; j <= range + offset; j++){
                var other = world.build(x + j * dir.x, y + j * dir.y);

                //hit insulated wall
                if(other != null && other.isInsulated()){
                    break;
                }

                if(other != null && other.block.hasPower && other.team == Vars.player.team() && !(other.block instanceof PowerNode)){
                    maxLen = j;
                    dest = other;
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
        }
    }

    @Override
    public void changePlacementPath(Seq<Point2> points, int rotation, boolean diagonal){
        if(!diagonal){
            Placement.calculateNodes(points, this, rotation, (point, other) -> Math.max(Math.abs(point.x - other.x), Math.abs(point.y - other.y)) <= range);
        }
    }

    public class EnergyLaserBuild extends Building{
        //current links in cardinal directions
        public Building[] links = new Building[1];
        public Tile[] dests = new Tile[4];
        public int lastChange = -2;

        @Override
        public void updateTile(){
            //世界每次有方块更新就让world.tileChanges变化，此处达到每次更新才重新计算的效果
            if(lastChange != world.tileChanges){
                lastChange = world.tileChanges;
                updateDirections();
            }
        }

        @Override
        public BlockStatus status(){
            if(Mathf.equal(power.status, 0f, 0.001f)) return BlockStatus.noInput;
            if(Mathf.equal(power.status, 1f, 0.001f)) return BlockStatus.active;
            return BlockStatus.noOutput;
        }

        @Override
        public void draw(){
            super.draw();
            //如果设置里的激光可见度（laserOpacity）为0，还画个瘠铂
            if(Mathf.zero(Renderer.laserOpacity)) return;
            //宣告以下绘制在power层进行
            Draw.z(Layer.power);
            //画笔颜色根据电够不够混合一下
            Draw.color(laserColor1, laserColor2, (1f - power.graph.getSatisfaction()) * 0.86f + Mathf.absin(3f, 0.1f));
            //画笔不透明度改成设置里的
            Draw.alpha(Renderer.laserOpacity);
            //算一算目前激光宽度（Width），基础加波动
            float w = laserWidth + Mathf.absin(pulseScl, pulseMag);
            //四面开花
            for(int i = 0; i < 4; i++){
                //这个方向的格子值存在，且（那个方块不是同类结点，格子的xy都和自己的不一样，格子后建造且他连接范围不比自己长，或者他连接范围就比自己短这四个条件满足其一）
                if(dests[i] != null && (!(links[i].block instanceof BeamNode node) ||
                (links[i].tileX() != tileX() && links[i].tileY() != tileY()) ||
                (links[i].id > id && range >= node.range) || range > node.range)){
                    //我和他之间，是x轴距离更长还是y轴，挑个长的赋值给dst
                    int dst = Math.max(Math.abs(dests[i].x - tile.x), Math.abs(dests[i].y - tile.y));
                    //距离还不到两人1/2，就是贴贴状态，就不发激光了
                    if(dst > 1 + size / 2){
                        //还没贴贴，先看看方向乘数
                        var point = Geometry.d4[i];
                        //算算一半的格子宽
                        float poff = tilesize / 2f;
                        //第一个贴图是激光中间，第二个是头尾两边，从自己对应方向的边缘，到对方的边缘，宽度取刚才算的。就可以激光贴贴了
                        Drawf.laser(laser, laserEnd, x + poff * size * point.x, y + poff * size * point.y, dests[i].worldx() - poff * point.x, dests[i].worldy() - poff * point.y, w);
                    }
                }
            }

            Draw.reset();
        }

        @Override
        public void pickedUp(){
            Arrays.fill(links, null);
            Arrays.fill(dests, null);
        }

        public void updateDirections(){
            //四面开花
            for(int i = 0; i < 4; i++){
                //先前的连接
                var prev = links[i];
                //方向乘数
                var dir = Geometry.d4[i];
                //两个置零
                links[i] = null;
                dests[i] = null;
                //偏移一点
                int offset = size / 2;
                //找方向范围内第一个带电的方块，j是距离
                //从中心开算，但是范围应该从边缘算，所以有偏移
                for(int j = 1 + offset; j <= range + offset; j++){
                    //开启遍历，坐标x是此方块x+距离*方向乘数，y同理
                    var other = world.build(tile.x + j * dir.x, tile.y + j * dir.y);

                    //绝缘了，这个方向后面不扫了
                    if(other != null && other.isInsulated()){
                        break;
                    }

                    //扫到了电力节点，润
                    if(other != null && other.block.hasPower && other.team == team && !(other.block instanceof PowerNode)){
                        //这个方向连接就是这个电力节点，格子按坐标获取
                        links[i] = other;
                        dests[i] = world.tile(tile.x + j * dir.x, tile.y + j * dir.y);
                        break;
                    }
                }
                //这个方向扫完了，把这个方向的连接存下来
                var next = links[i];
                //对照一下，看看连接和刚才的是不是一个
                if(next != prev){
                    //不是的话，看看原来是不是没有
                    if(prev != null){
                        //不是没有还换了，原来的连接叫它把本方块的记录删了
                        prev.power.links.removeValue(pos());
                        //然后自己也删了它的记录，双向删好友
                        power.links.removeValue(prev.pos());
                        //造个新表
                        PowerGraph newgraph = new PowerGraph();
                        //重排一遍
                        newgraph.reflow(this);
                        //原来的连接的表和现在造的不一样的话，，，
                        if(prev.power.graph != newgraph){
                            //就叫原来连接也重排一个
                            PowerGraph og = new PowerGraph();
                            og.reflow(prev);
                        }
                    }

                    //要是有现在的连接的话
                    if(next != null){
                        //PowerModule记下他
                        power.links.addUnique(next.pos());
                        //叫他也记下我
                        next.power.links.addUnique(pos());
                        //把我的表里连上他
                        power.graph.addGraph(next.power.graph);
                    }
                }
            }
        }
    }
}
