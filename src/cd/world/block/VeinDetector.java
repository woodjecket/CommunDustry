package cd.world.block;

import arc.math.Rand;
import cd.CDMod;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Block;

public class VeinDetector extends Block {
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
            warmup += delta() ;
            if(warmup >= 1f) {
                warmup %= 1;
                int sx = rand.nextInt(24) + tileX() -12;
                int sy = rand.nextInt(24) + tileY() -12;
                CDMod.vm.get(Vars.world.tile(sx,sy));
            }
        }
    }
}
