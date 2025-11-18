package cd.world.block;

import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.io.Reads;
import cd.CDMod;
import cd.struct.vein.VeinEntity;
import cd.struct.vein.VeinTile;
import cd.struct.vein.VeinType;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Block;

public class VeinDrill extends Block {
    public int radius = 10;
    public float baseDrillTime = 20f;
    public boolean autoSwitch = true;

    public VeinDrill(String name) {
        super(name);
        update = true;
        hasItems = true;
    }

    public class VeinDrillBuild extends Building {
        public float progress;
        public int depth = -75;
        /**
         * If null, just mine stones.
         */
        public @Nullable VeinType selectedType;
        public VeinEntity drillEntity;
        public Seq<VeinTile> tiles = new Seq<>();
        public int ambientCount = 0;
        public ObjectMap<VeinType, Seq<VeinEntity>> available = new ObjectMap<>();


        @Override
        public boolean shouldConsume() {
            return items.total() < itemCapacity && enabled;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            updateAvailable();
            if (autoSwitch && available.keys().hasNext) {
                selectedType = available.keys().next();
                available.keys().reset();
            }
            progress += edelta() / baseDrillTime;
            if (progress > 1) {
                progress %= 1;
                offload(drillEntity.produceOre());
                drillEntity = null;
            }
            if (drillEntity == null) {
                assignEntity();
            }

            if (timer(timerDump, dumpTime / timeScale)) {
                dump();
            }
        }

        private void assignEntity() {
            // 1st attempt: available
            Log.info("1st attempt: @", available.get(selectedType));
            drillEntity = available.get(selectedType) != null ? available.get(selectedType).first() : null;

            // 2nd attempt: scan the tiles
            if (drillEntity == null && ambientCount < tiles.size) {

                var vtile = tiles.get(ambientCount);
                ambientCount++;
                drillEntity = vtile.getEntity(depth);
                Log.info("2nd attempt: count:@, vtile:@, entity:@", ambientCount, vtile, drillEntity);
                if (drillEntity != null) {
                    // Passive detection
                    drillEntity.detected = true;
                }
            }

            // final: mine stones
            if (drillEntity == null) {
                Log.info("final attempt: stone");
                drillEntity = VeinEntity.infiniteStone;
            }
        }

        private void updateAvailable() {
            available.clear();
            tiles.forEach(vt -> {
                VeinEntity got;
                if ((got = vt.getEntity(depth)) != null) {
                    if (!got.exhausted() && got.detected) {
                        available.get(got.type, Seq::new).add(got);
                    }
                }
            });
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            loadTiles();
        }

        @Override
        public void placed() {
            super.placed();
            loadTiles();
        }

        public void loadTiles() {
            int tx = tileX(), ty = tileY();
            for (int i = tx - radius / 2; i < tx + radius / 2; i++) {
                for (int j = ty - radius / 2; j < ty + radius / 2; j++) {
                    if(Mathf.within(i,j,tx,ty,radius)) {
                        tiles.addUnique(CDMod.vm.get(Vars.world.tile(i, j)));
                    }
                }
            }
            tiles.remove((VeinTile) null);
        }
    }
}
