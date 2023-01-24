package cd.entities.bullets;

import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class SoundWaveBulletType extends BasicBulletType{
    float waveInterval = 0.7f;
    @Override
    public void draw(Bullet b){
        super.draw(b);
        float expectWaveLength = Mathf.dst(b.aimX,b.aimY,b.originX,b.originY);
        for(var i = waveInterval; i <= expectWaveLength; i = i + waveInterval){
            Drawf.circles(b.originX, b.originY,i);
        }
    }

    @Override
    public void update(Bullet b){
        float expectWaveLength = Mathf.dst(b.aimX,b.aimY,b.x,b.originY);
        Groups.unit.intersect(b.originX,b.originY,range*2,range*2,u->{
            if(u.within(b.originX,b.originY,expectWaveLength)){
                Log.info("hit(@,@)",u.x,u.y);
                u.damage(damage);
            }
        });
    }

    protected float calculateRange(){
        return Math.max(maxRange, 0);
    }

    {
        collideFloor = collideTerrain = true;
        absorbable = hittable = false;
    }
}
