package cd.type.blocks;

import cd.entities.component.BaseComponent;
import mindustry.world.blocks.production.*;

public class ComponentCrafter extends GenericCrafter {
    public BaseComponent component;    

    public ComponentCrafter(String name) {
        super(name);

    }

    @Override
    public void init() {
        super.init();
        component.onInit(this);

    }

    @Override
    public void setStats() {
        super.setStats();
        component.onSetStats(this);
    }

    public class ComponentCrafterBuilding extends GenericCrafterBuild {

        @Override
        public boolean shouldConsume() {
            return super.shouldConsume() && component.onShouldConsume(this);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            component.onUpdateTile(this);
        }

        @Override
        public float efficiencyScale() {
            return component.onEfficiencyScale(this);
        }

        @Override
        public void craft(){
            super.craft();
            component.onCraft(this);
        }
    }

}

