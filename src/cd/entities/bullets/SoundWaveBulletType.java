package cd.entities.bullets;

import arc.graphics.g2d.Lines;
import arc.math.*;
import mindustry.entities.Units;
import mindustry.entities.bullet.*;
import mindustry.gen.*;

public class SoundWaveBulletType extends BasicBulletType {
    public float waveSpeed = 1f;
    public float attenuateRange = lifetime * 0.75f * waveSpeed;
    public float attenuatePercent = height * 0.05f;

    @Override
    public void init(Bullet b) {
        super.init(b);
        b.data = new WaveBullet() {{
            waveParent = b;
            speed = waveSpeed;
            angle = width;
            thickness = height;
        }};
    }

    @Override
    public void draw(Bullet b) {
        if (b.data instanceof WaveBullet p) {
            Lines.stroke(p.thickness, backColor);
            Lines.arc(b.x, b.y, p.dst, width / 360, b.rotation() - 90 + (180 - width) / 2);
        }
    }

    @Override
    public void update(Bullet b) {
        super.update(b);
        if (b.data instanceof WaveBullet w) {
            w.update();
        }
    }

    protected float calculateRange() {
        return Math.max(lifetime * waveSpeed, 0);
    }

    {
        collides = false;
        absorbable = hittable = false;
        pierce = pierceBuilding = true;
        keepVelocity = false;
        speed = 0;
    }

    public class WaveBullet {
        public Bullet waveParent;
        public float angle;
        public float thickness;
        public float dst = 0;

        public void update() {
            //还没写伤害建筑
            if (!waveParent.isAdded()) return;
            if (dst > attenuateRange){
                thickness -= attenuatePercent;
            }
            if (thickness <= 0){
                damage = thickness = 0;
                waveParent.remove();
            }
            dst += waveSpeed;
            Units.nearbyEnemies(waveParent.team, waveParent.x, waveParent.y, dst + thickness,
                    b -> {
                        if (!b.within(waveParent.x, waveParent.y, dst) &&
                                Angles.angleDist(waveParent.rotation(), waveParent.angleTo(b)) < angle / 2
                        ) {
                            waveParent.collision(b, b.x, b.y);
                        }
                    }
            );
        }
    }
}
