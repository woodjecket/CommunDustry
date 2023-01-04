package cd.type.blocks;

import mindustry.world.blocks.production.GenericCrafter;

public class PneuCrafter extends GenericCrafter {

    public PneuCrafter(String name) {
        super(name);
    }

    public class PneuCrafterBuilding extends GenericCrafterBuild{
        public float pressure;

        @Override
        public void updateTile(){
            super.updateTile();
        }

        public void calculatePressure() {
        }
    }
    
}
