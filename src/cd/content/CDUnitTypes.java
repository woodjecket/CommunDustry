package cd.content;

import cd.entities.bullets.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.type.ammo.*;

public class CDUnitTypes{
    public static UnitType soundWaveTest;

    public void load(){
        soundWaveTest = new UnitType("sound-wave-test-2.2.1-PRE-ALPHA"){{
            constructor = UnitTypes.poly.constructor;
            flying = true;
            drag = 0.05f;
            speed = 2.6f;
            rotateSpeed = 15f;
            accel = 0.1f;
            range = 130f;
            health = 400;
            buildSpeed = 0.5f;
            engineOffset = 6.5f;
            hitSize = 9f;
            lowAltitude = true;
            ammoType = new PowerAmmoType(900);
            mineTier = 2;
            mineSpeed = 3.5f;
            abilities.add(new RepairFieldAbility(5f, 60f * 8, 50f));
            weapons.add(new Weapon("poly-weapon"){{
                mirror = false;
                reload = 30f;
                ejectEffect = Fx.none;
                recoil = 2f;
                shootSound = Sounds.missile;
                x = 0;
                bullet = new SoundWaveBulletType() {{
                    width = 45;
                    height = 8;
                    lifetime = 80f;
                    damage = 10f;
                    waveSpeed = 5;
                }};
            }});
        }};
    }
}
