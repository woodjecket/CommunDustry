package cd.type.blocks;

import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import cd.entities.component.BaseComponent;
import cd.type.blocks.pneumatic.PneuInterface;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

import mindustry.graphics.Pal;
import arc.Core;

public class ComponentBlock extends Block {
    public float visualExplodePressure = 15f;
    public BaseComponent component;
    public boolean hasPressure;
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

    @Override
    public void setBars(){
        if(hasPressure){
        addBar("pressure",
                (ComponentBuild entity) -> new Bar(
                        () -> Core.bundle.format("bar.pressureamount", entity.pressure),
                        () -> Pal.lightOrange, () -> entity.pressure / visualExplodePressure));}
    }

    public class ComponentBuild extends Building implements PneuInterface {
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
