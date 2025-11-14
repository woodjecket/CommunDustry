package cd.struct.vein;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.util.Structs;
import mindustry.Vars;
import mindustry.world.Tile;

public class VeinTile {
    public VeinEntity[] veins;
    public Tile tile;
    public void drawVein(){
        if(veins.length > 0){
            Draw.color(Color.green);
            Fill.rect(tile.drawx(), tile.drawy(), Vars.tilesize, Vars.tilesize);
        }
    }
}
