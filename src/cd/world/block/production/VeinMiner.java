package cd.world.block.production;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.util.Tmp;
import cd.io.DeepVeinCustomChunk;
import cd.type.OreInfo;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Block;

public class VeinMiner extends Block {
    public VeinMiner(String name) {
        super(name);
        update = true;
        configurable = true;
    }
    public class VeinMinerBuild extends Building{
        @Override
        public void drawConfigure() {
            super.drawConfigure();
            for(var entry: DeepVeinCustomChunk.infos){
                var tile = Vars.world.tile(entry.key);
                var color = Tmp.c1.set(0,0,0,1);
                for(OreInfo info:entry.value.ores){
                    if(info != null) {
                        color.lerp(info.color, 0.5f);
                    }
                }
                Draw.color(color);
                Fill.rect(tile.getX(),tile.getY(),8,8);
            }
        }
    }
}
