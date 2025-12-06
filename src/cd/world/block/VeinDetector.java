package cd.world.block;

import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
import cd.CDConfig;
import cd.CDMod;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Block;

public class VeinDetector extends Block {
    public int radius = 20;
    public int maxDepth = CDConfig.defaultDepth;
    private float discoveryTime = 60f * 10f;

    public VeinDetector(String name) {
        super(name);
        update = true;
        configurable = true;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        CDMod.vm.draw();
    }

    public class VeinDetectorBuild extends Building {
        public float progress;
        public float smoothEfficiency = 1f;

        @Override
        public void drawConfigure() {
            super.drawConfigure();
            CDMod.vm.draw();
        }

        @Override
        public void updateTile() {
            super.updateTile();
            smoothEfficiency = Mathf.lerpDelta(smoothEfficiency, efficiency, 0.05f);

            int pr = (int) (radius * progress);
            int tx = tileX(), ty = tileY();
            for (int i = tx - pr / 2; i < tx + pr / 2; i++) {
                for (int j = ty - pr / 2; j < ty + pr / 2; j++) {
                    if (Mathf.within(i, j, tx, ty, pr)) {
                        var got = CDMod.vm.get(Vars.world.tile(i, j));
                        if (got != null) got.veins.each(ve -> {if (ve.depth <= maxDepth) ve.shouldWrite = ve.detected = true;});
                    }
                }
            }

            progress += edelta() / discoveryTime;
            progress = Mathf.clamp(progress);
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            progress = read.f();
        }
    }
}
