package cd.map.dv.data;

import cd.type.OreInfo;

public class DeepVeinPosition {
    public int x,y;
    public OreInfo[] ores;

    public DeepVeinPosition(int x, int y, OreInfo o1, OreInfo o2, OreInfo o3) {
        this.x = x;
        this.y = y;
        ores = new OreInfo[]{o1, o2, o3};
    }
}
