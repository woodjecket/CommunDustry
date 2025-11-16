package cd.struct.vein;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Structs;
import mindustry.Vars;
import mindustry.world.Tile;

public class VeinTile {
    public VeinEntity[] veins;
    public Tile tile;
    public boolean detected;
    public void drawVein(){
        if(veins.length > 0 && detected && Structs.contains(veins,v->!v.exhausted())){
            Draw.color(Color.green);
            Fill.rect(tile.drawx(), tile.drawy(), Vars.tilesize, Vars.tilesize);
        }
    }

    public boolean exhausted(int z) {
        return Structs.contains(veins, v->!exhausted(z,v));
    }

    private boolean exhausted(int z, VeinEntity vein) {
        return Mathf.within(z, vein.centerZ, vein.type.baseZThreshold) && !vein.exhausted();
    }

    public boolean shouldWrite(){
        return Structs.contains(veins, veinEntity -> !veinEntity.untouched);
    }
}
