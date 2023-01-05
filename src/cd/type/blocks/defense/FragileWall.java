package cd.type.blocks.defense;

import arc.math.Mathf;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.Wall;

public class FragileWall extends Wall {
    public float brokenThreshold = 100;
    public float damageMultiple = 0.9f;
    public float regainSpeed = 0.1f;

    public FragileWall(String name) {
        super(name);
        update = true;
    }

    @Override
    public void setBars() {
        addBar("damage", build -> new Bar("damagedAmount", Pal.health, () -> ((FragileWallBuilding) build).damagedAmount / brokenThreshold));
        super.setBars();
    }

    public class FragileWallBuilding extends Wall.WallBuild {
        public float damagedAmount = 0;

        @Override
        public void updateTile() {
            damagedAmount = Mathf.approach(damagedAmount, 0, regainSpeed);
        }

        @Override
        public float handleDamage(float amount) {
            damagedAmount += amount * damageMultiple;
            if (damagedAmount > brokenThreshold) kill();
            return super.handleDamage(amount) * damageMultiple;
        }
    }
}
