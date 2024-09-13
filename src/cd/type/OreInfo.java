package cd.type;

import arc.graphics.Color;
import mindustry.graphics.Pal;

public class OreInfo {
    public static OreInfo t1 = new OreInfo(Color.acid),t2=new OreInfo(Color.sky),t3=new OreInfo(Color.gold);
    public final Color color;

    public OreInfo(Color accent) {
        this.color = accent;
    }
}
