package cd.map.vein;

import cd.struct.vein.VeinTile;

public interface VeinGenerator {
    VeinTile get(int x, int y);
}
