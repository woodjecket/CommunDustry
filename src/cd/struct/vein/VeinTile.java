package cd.struct.vein;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import mindustry.Vars;
import mindustry.world.Tile;

public class VeinTile {
    public Vein[] veins;
    public Tile tile;
    public void drawVein(){
        Draw.color(Color.green);
        Fill.rect(tile.drawx(),tile.drawx(), Vars.tilesize, Vars.tilesize);
    }
}
