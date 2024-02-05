package cd.world;

import arc.graphics.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.world.blocks.environment.*;

public class ColoredFloor extends Floor{
    public Color color;
    public ColoredFloor(String name, Color color){
        super(name);
        this.color = color;
    }

    public ColoredFloor(String name){
        super(name);
    }

    @Override
    public void createIcons(MultiPacker packer){
        var pixmap = new Pixmap(32,32);
        pixmap.fill(color);
        packer.add(PageType.environment, name, pixmap);
        super.createIcons(packer);
    }
}
