package cd.world.blocks.defense;

import arc.*;
import arc.math.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.meta.*;

public class FragileWall extends Wall{
    public float brokenThreshold = 100;
    public float damagedMultiple = 0.9f;
    public float regainSpeed = 0.1f;

    public FragileWall(String name){
        super(name);
        update = true;
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(new Stat("broken-threshold"), brokenThreshold, new StatUnit("damage", false));
        stats.add(new Stat("regain-speed"), regainSpeed * 60, StatUnit.perSecond);
        stats.add(new Stat("damaged-multiple"), (1 - damagedMultiple) * 100, StatUnit.percent);
    }

    @Override
    public void setBars(){
        addBar("damage", build -> new Bar(() -> Core.bundle.format("bar.damage-amount"), () -> Pal.health, () -> ((FragileWallBuilding)build).damagedAmount / brokenThreshold));
        super.setBars();
    }

    public class FragileWallBuilding extends Wall.WallBuild{
        public float damagedAmount = 0;

        @Override
        public void updateTile(){
            damagedAmount = Mathf.approach(damagedAmount, 0, regainSpeed);
        }

        @Override
        public float handleDamage(float amount){
            damagedAmount += amount * damagedMultiple;
            if(damagedAmount > brokenThreshold) kill();
            return super.handleDamage(amount) * damagedMultiple;
        }
    }
}
