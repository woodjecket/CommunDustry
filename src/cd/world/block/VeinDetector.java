package cd.world.block;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import cd.CDConfig;
import cd.CDMod;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.world.Block;

public class VeinDetector extends Block {
    public int radius = 20;
    public int maxDepth = CDConfig.defaultDepth;
    private float discoveryTime = 60f * 20f;

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
        public float warmth;

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
                        if (got != null) got.veins.each(ve -> {
                            if (ve.depth <= maxDepth) ve.shouldWrite = ve.detected = true;
                        });
                    }
                }
            }

            progress += edelta() / discoveryTime;
            progress = Mathf.clamp(progress);
            warmth += edelta() / discoveryTime;
            warmth = Mathf.clamp(warmth);
        }

        @Override
        public void draw() {
            super.draw();
            if(progress >= 1f) return;
            float line = progress * radius * 2;
            float x2, y2;
            x2 = (float) (Math.sin(Time.time / 40f) * line);
            y2 = (float) (Math.cos(Time.time / 40f) * line);
            Draw.color(Pal.accent);
            Lines.line(x, y, x + x2, y + y2);

            float axis = line + 2f;
            Lines.line(x - axis, y, x + axis, y);
            Lines.line(x, y - axis, x, y + axis);


            for (int i = 0; i < ((int) (warmth * 5)); i++) {
                if (i == ((int) (warmth * 5)) - 1) Draw.alpha((5f * warmth) % 1f);
                Lines.circle(x, y, radius / 3f * (i + 1f) + 2f);
            }
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
