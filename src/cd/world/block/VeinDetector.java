package cd.world.block;

import arc.math.Mathf;
import arc.math.Rand;
import cd.CDMod;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Block;

public class VeinDetector extends Block {
    int radius = 200;
    public VeinDetector(String name) {
        super(name);
        update = true;
    }
    public class VeinDetectorBuild extends Building{
        public float warmup;
        public Rand rand = new Rand();
        @Override
        public void updateTile() {
            super.updateTile();
            warmup += delta();
            if(warmup >= 1f) {
                warmup %= 1;
                int tx = tileX(), ty = tileY();
                for (int i = tx - radius / 2; i < tx + radius / 2; i++) {
                    for (int j = ty - radius / 2; j < ty + radius / 2; j++) {
                        if(Mathf.within(i,j,tx,ty,radius)){
                            CDMod.vm.get(Vars.world.tile(i, j), false);
                        }
                    }
                }
            }
        }
    }
}
