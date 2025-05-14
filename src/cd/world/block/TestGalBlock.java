package cd.world.block;

import cd.content.*;
import cd.manager.*;
import mindustry.gen.*;
import mindustry.world.*;

public class TestGalBlock extends Block{


    public TestGalBlock(String name){
        super(name);
        update = true;
    }

    public  class GalBuilding extends Building{
        @Override
        public void tapped(){
            super.tapped();
            GalManager.startPlot(Plots.plotTest);
        }
    }
}
