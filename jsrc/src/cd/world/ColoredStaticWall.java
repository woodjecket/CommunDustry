package cd.world;

import arc.graphics.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.world.blocks.environment.*;

public class ColoredStaticWall extends StaticWall{
    public Color color;

    public ColoredStaticWall(String name, Color color){
        super(name);
        this.color = color;
    }

    public ColoredStaticWall(String name){
        super(name);
    }

    @Override
    public void createIcons(MultiPacker packer){
        var pixmap = new Pixmap(32, 32);
        pixmap.fill(color);
        for(int x = 1; x <= 32; x++){
            for(int y = 1; y <= 32; y++){
                if(x == 1 || x == 32 || y == 1 || y == 32){
                    pixmap.set(x, y, Color.gray);
                }
            }
        }
        packer.add(PageType.environment, name, pixmap);
        super.createIcons(packer);
    }
}
