package cd.world.component;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import cd.entities.building.*;
import cd.world.blocks.*;
import cd.world.stat.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;

import static arc.math.Mathf.*;
import static java.lang.Math.*;
import static mindustry.Vars.*;

public class LaserComponent extends BaseComponent{
    public static int lastChange = -1;
    public boolean acceptLaserEnergy;
    public boolean provideLaserEnergy;
    public float laserEnergyOutput;
    public float maxLaserEnergy = 10f;
    public float consumeLaserEnergy;
    public int laserRange = 5;
    public float laserTransportEfficiency = 0.5f / 60f;
    public int attenuateRange = 3;
    public float attenuatePercent = 0.05f;
    public Color laserColor1 = Color.white;
    public Color laserColor2 = Color.valueOf("ffd9c2");
    public float pulseScl = 7, pulseMag = 0.05f;
    public float laserBaseWidth = 0.4f;
    public String laserType;
    public TextureRegion laser, laserStart, laserEnd;

    @Override
    public void onLoad(){
        laser = Core.atlas.find(content.transformName(laserType) + "-laser", "power-beam");
        laserEnd = Core.atlas.find(content.transformName(laserType) + "-laser-start", "power-beam-end");
        laserStart = Core.atlas.find(content.transformName(laserType) + "-laser-end", "power-beam-end");
    }

    @Override
    public void onDrawPlace(Block b, int x, int y, int rotation){
        if(provideLaserEnergy) drawLaserDestination(b, x, y, rotation);
        if(acceptLaserEnergy) drawLaserFrom(b, x, y, rotation);
    }

    private void drawLaserFrom(Block b, int x, int y, int rotation){
        for(int i = 0; i < 4; i++){
            if(rotation == i) continue;
            int len = 0;
            Building from = null;
            var dir = Geometry.d4[i];
            int dx = dir.x;
            int dy = dir.y;
            int offset = b.size / 2;
            for(int j = 1 + offset; j <= laserRange + offset; j++){
                var other = world.build(x + j * dir.x, y + j * dir.y);
                if(other != null && !(other instanceof ILaserBuilding || other.block.underBullets)){
                    break;
                }

                if(other instanceof ILaserBuilding l && other.team == player.team() && l.isProvideLaserEnergy(x, y)){
                    len = j;
                    from = other;
                    break;
                }
            }

            if(from != null){
                Drawf.dashLine(Pal.place,
                x * tilesize + dx * (tilesize * b.size / 2f + 2),
                y * tilesize + dy * (tilesize * b.size / 2f + 2),
                x * tilesize + dx * (len) * tilesize,
                y * tilesize + dy * (len) * tilesize
                );
                Drawf.square(from.x, from.y, from.block.size * tilesize / 2f + 2.5f, 0f, Pal.place);
            }
        }
    }

    private void drawLaserDestination(Block b, int x, int y, int rotation){
        int maxLen = laserRange + b.size / 2;
        Building dest = null;
        var dir = Geometry.d4[rotation];
        int dx = dir.x, dy = dir.y;
        int offset = b.size / 2;
        for(int j = 1 + offset; j <= laserRange + offset; j++){
            var other = world.build(x + j * dir.x, y + j * dir.y);
            if(other != null && !((other instanceof ILaserBuilding) || other.block.underBullets)){
                break;
            }
            if(other instanceof ILaserBuilding l && other.team == player.team() && l.isAcceptLaserEnergy()){
                maxLen = j;
                dest = other;
                break;
            }
        }
        Drawf.dashLine(Pal.placing,
        x * tilesize + dx * (tilesize * b.size / 2f + 2),
        y * tilesize + dy * (tilesize * b.size / 2f + 2),
        x * tilesize + dx * (maxLen) * tilesize,
        y * tilesize + dy * (maxLen) * tilesize
        );
        if(dest != null){
            Drawf.square(dest.x, dest.y, dest.block.size * tilesize / 2f + 2.5f, 0f);
        }
    }

    @Override
    public void onInit(Block b){
        b.updateClipRadius((laserRange + 1) * tilesize);
    }

    public boolean isProvideLaserEnergy(Building b, int bx, int by){
        if(provideLaserEnergy){
            var dir = Geometry.d4[b.rotation];
            if(!(((b.tile.x == bx) ? 0 : sign(bx - b.tile.x)) == dir.x && ((b.tile.y == by) ? 0 : sign(by - b.tile.y)) == dir.y)) return false;
            if(((ILaserBuilding)b).getLaserChild() == null) return true;
            int nowDst = Math.max(abs(bx - b.tile.x), abs(by - b.tile.y));
            int dst = Math.max(Math.abs(((ILaserBuilding)b).getLaserChild().tile.x - b.tile.x), Math.abs(((ILaserBuilding)b).getLaserChild().tile.y - b.tile.y));
            return nowDst < dst;
        }else{
            return false;
        }
    }

    public boolean isAcceptLaserEnergy(){
        //Anyway, if them face the energy will attenuate.
        return acceptLaserEnergy;
    }

    @Override
    public void onUpdateTile(Building b){
        //世界每次有方块更新就让world.tileChanges变化，此处达到每次更新才重新计算的效果
        if(provideLaserEnergy){
            if(lastChange != world.tileChanges){
                lastChange = world.tileChanges;
                updateChild(b);
            }
            updateLaserEnergy(b);
        }

    }

    private void updateLaserEnergy(Posc b){
        var bLaser = (ILaserBuilding)b;
        Building child = null;
        if(bLaser.getLaserChild() != null){
            child = bLaser.getLaserChild();
        }
        var childLaser = (ILaserBuilding)child;
        if(childLaser == null) return;
        if(childLaser.getLaserEnergy() > ((IComp)child.block).getComp(LaserComponent.class).maxLaserEnergy) return;
        if(bLaser.getLaserEnergy() < 0) return;
        var outRangeDst = max(attenuateRange, dst(b.tileX(), b.tileY(), child.tileX(), child.tileY())) - attenuateRange;
        var realEfficiency = laserTransportEfficiency * max((1 - outRangeDst * attenuatePercent), 0f);
        if(bLaser.getLaserEnergy() >= realEfficiency + FLOAT_ROUNDING_ERROR){
            bLaser.changeLaserEnergy(-realEfficiency);
            childLaser.changeLaserEnergy(realEfficiency);
        }
    }

    @Override
    public void onCraft(Building b){
        var bLaser = (ILaserBuilding)b;
        bLaser.changeLaserEnergy(bLaser.getLaserEnergy() > maxLaserEnergy ? 0 : laserEnergyOutput - consumeLaserEnergy);
    }

    private void updateChild(Building b){
        var bLaser = (ILaserBuilding)b;
        //先前的子节点
        var prev = bLaser.getLaserChild();
        //方向乘数
        var dir = Geometry.d4[b.rotation];
        //置零
        bLaser.setLaserChild(null);
        //偏移一点
        int offset = b.block.size / 2;
        //找方向范围内第一个带isAbsorbLaserEnergy的方块，j是距离
        //从中心开算，但是范围应该从边缘算，所以有偏移
        for(int j = 1 + offset; j <= laserRange + offset; j++){
            //开启遍历，坐标x是此方块x+距离*方向乘数，y同理
            var other = world.build(b.tile.x + j * dir.x, b.tile.y + j * dir.y);
            if(other==null)break;
            if(other.block.size > 1 && other.tileX() != b.tile.x + j * dir.x){
                break;
            }
            if(other instanceof ILaserBuilding && !((ILaserBuilding)other).isAcceptLaserEnergy()){
                break;
            }

            if(other.team == Vars.player.team() && other instanceof ILaserBuilding l && l.isAcceptLaserEnergy()){
                bLaser.setLaserChild(other);
                break;
            }
            if(!(other instanceof ILaserBuilding) && !other.block.underBullets){
                break;
            }
        }
        //这个方向扫完了，把这个方向的连接存下来
        var next = bLaser.getLaserChild();
        //对照一下，看看连接和刚才的是不是一个
        if(next != prev){
            //要是有现在的连接的话
            if(next != null){
                //记下他
                bLaser.setLaserChild(next);
            }
        }


    }

    @Override
    public void onEntityDraw(Building b){
        var bLaser = (ILaserBuilding)b;
        //如果设置里的激光可见度（laserOpacity）为0，还画个瘠铂
        if(Mathf.zero(Renderer.laserOpacity)) return;
        //宣告以下绘制在power层进行
        Draw.z(Layer.power);
        //画笔颜色根据电够不够混合一下
        Draw.color(laserColor1, laserColor2, (1f - bLaser.getLaserEnergy() / maxLaserEnergy) * 0.86f + Mathf.absin(3f, 0.1f));
        //画笔不透明度改成设置里的
        Draw.alpha(Renderer.laserOpacity);
        //算一算目前激光宽度（Width），基础加波动
        float w = laserBaseWidth + Mathf.absin(pulseScl, pulseMag);
        //四面开花
        //格子值存在，且（那个方块不是同类结点，格子的xy都和自己的不一样，格子后建造且他连接范围不比自己长，或者他连接范围就比自己短这四个条件满足其一）
        if(bLaser.getLaserChild() != null && (!(bLaser.getLaserChild().block instanceof ILaserBuilding node) ||
        (bLaser.getLaserChild().tileX() != b.tileX() && bLaser.getLaserChild().tileY() != b.tileY()) ||
        (bLaser.getLaserChild().id > b.id && laserRange >= node.getLaserRange()) || laserRange > node.getLaserRange())){
            //我和他之间，是x轴距离更长还是y轴，挑个长的赋值给dst
            int dst = Math.max(Math.abs(bLaser.getLaserChild().tile.x - b.tile.x), Math.abs(bLaser.getLaserChild().tile.y - b.tile.y));
            //距离还不到两人1/2，就是贴贴状态，就不发激光了
            if(dst > 1 + b.block.size / 2){
                //还没贴贴，先看看方向乘数
                var point = Geometry.d4[b.rotation];
                //算算一半的格子宽
                float poff = tilesize / 2f;
                //第一个贴图是激光中间，第二个是头尾两边，从自己对应方向的边缘，到对方的边缘，宽度取刚才算的。就可以激光贴贴了
                Drawf.laser(laser, laserStart, laserEnd, b.x + poff * b.block.size * point.x, b.y + poff * b.block.size * point.y, bLaser.getLaserChild().tile.worldx() - poff * point.x, bLaser.getLaserChild().tile.worldy() - poff * point.y, w);
            }
        }
        Draw.reset();
    }

    public int getLaserRange(){
        return laserRange;
    }

    @Override
    public void onSetBars(Block b){
        b.addBar("laser-energy", (entity) -> new Bar(
        () -> Core.bundle.format("bar.laser-energy", ((ILaserBuilding)entity).getLaserEnergy()),
        () -> laserColor2,
        () -> ((ILaserBuilding)entity).getLaserEnergy() / maxLaserEnergy
        ));
    }

    @Override
    public void onSetStats(Block b){
        b.stats.add(CDStat.maxLaser, maxLaserEnergy);
        if(laserEnergyOutput != 0) b.stats.add(CDStat.laserOutput, laserEnergyOutput, CDStat.perConsume);
        if(consumeLaserEnergy != 0) b.stats.add(CDStat.laserConsume, consumeLaserEnergy, CDStat.perConsume);
    }

    @Override
    public boolean onShouldConsume(Building b){
        return provideLaserEnergy || !acceptLaserEnergy || ((ILaserBuilding)b).getLaserEnergy() > consumeLaserEnergy;
    }
}
