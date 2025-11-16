package cd.world.block;

import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Structs;
import arc.util.io.Reads;
import cd.CDMod;
import cd.struct.vein.VeinEntity;
import cd.struct.vein.VeinTile;
import cd.struct.vein.VeinType;
import mindustry.Vars;
import mindustry.entities.EntityIndexer;
import mindustry.gen.Building;
import mindustry.world.Block;

import java.util.Arrays;

public class VeinDrill extends Block {
    public int radius = 10;
    public int baseDrillTime = 20;
    public boolean autoSwitch = true;

    public VeinDrill(String name) {
        super(name);
        update = true;
        hasItems = true;
    }

    public class VeinDrillBuild extends Building {
        public Seq<VeinTile> tiles = new Seq<>();
        public ObjectMap<VeinType, Seq<VeinTile>> indices = new ObjectMap<>();
        public VeinType selected;
        public VeinEntity drilling;
        public int currentZ = -75;

        public float progress;

        @Override
        public void updateTile() {
            super.updateTile();
            if (selected == null || indices.get(selected).allMatch(vt -> vt.exhausted(currentZ, selected)) && autoSwitch)
                refreshItem();
            if (drilling == null || drilling.exhausted()) refreshDrilling();
            updateDrilling();
        }

        private void updateDrilling() {
            if (drilling == null) return;
            progress += edelta() / baseDrillTime;
            if (progress >= 1) {
                progress %= 1;
                drilling.consume();
                offload(drilling.offload());
            }
        }

        private void refreshDrilling() {
            if (selected == null) {
                drilling = null;
                return;
            }
            var veins = indices.get(selected).find(vt -> !vt.exhausted(currentZ, selected)).veins;
            drilling = Structs.find(veins, v -> v.type == selected);
        }

        private void refreshItem() {
            for (var entry : indices) {
                if (indices.get(entry.key).contains(vt -> !vt.exhausted(currentZ, entry.key))) {
                    selected = entry.key;
                    return;
                }
            }
            selected = null;
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
                        tiles.addUnique(CDMod.vm.get(Vars.world.tile(i, j), true));
                    }
                }
            }
            tiles.remove((VeinTile) null);
            indices.clear();
            for (var vt : tiles) {
                for (var v : vt.veins) {
                    var s = indices.get(v.type, Seq::new);
                    if (vt.veins.length > 0) s.addUnique(vt);
                }
            }
        }
    }
}
