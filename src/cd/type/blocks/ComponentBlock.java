package cd.type.blocks;

import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import cd.entities.component.BaseComponent;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class ComponentBlock extends Block {
    public BaseComponent component;
    public DrawBlock drawer = new DrawDefault();

    public ComponentBlock(String name) {
        super(name);
        update = true;
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    public class ComponentBuild extends Building  {
        //define pneumatic
        public float pressure;

        public float pressure() {
            return pressure;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float p) {
            this.pressure = p;
        }

        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public void drawLight() {
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public void updateTile() {
            super.updateTile();
            component.onUpdateTile(this);
        }


        @Override
        public void onDestroyed() {
            super.onDestroyed();
            component.onDestroyed(this);
        }

        public void createExplosion() {
            component.onCreateExplosion(this);
        }

    }

}
