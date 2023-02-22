package cd.type;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.TextureAtlas.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.type.*;

public class MetaDust extends Item{
    public static AtlasRegion metaDust;
    public Item item;
    private int[] palettei;

    public MetaDust(Item item){
        super(item.name + "-dust", item.color);
        this.item = item;
        palettei = new int[]{color.mul(0.81f).rgba8888(), color.rgba8888(), color.mul(1.24f).rgba8888()};
        explosiveness = item.explosiveness * 1.2f;
        flammability = item.flammability * 1.3f;
        charge = item.charge * 0.8f;
        localizedName = Core.bundle.format("commumdustry-item-dust", item.localizedName);
        //Log.info(localizedName);
    }

    public MetaDust(Item item, Color c1, Color c2, Color c3){
        this(item);
        palettei = new int[]{c1.rgba8888(), c2.rgba8888(), c3.rgba8888()};
    }

    public MetaDust(Item item, String c1, String c2, String c3){
        this(item);
        palettei = new int[]{Color.valueOf(c1).rgba8888(), Color.valueOf(c2).rgba8888(), Color.valueOf(c3).rgba8888()};
    }

    @Override
    public void loadIcon(){
        super.loadIcon();
        metaDust = Core.atlas.find("commumdustry-meta-dust");
        fullIcon = uiIcon = Core.atlas.find(Vars.content.transformName(name));
    }

    @Override
    public void createIcons(MultiPacker packer){
        var dustBase = Core.atlas.getPixmap(metaDust);
        Pixmap out = new Pixmap(dustBase.width, dustBase.height);
        for(int x = 0; x < dustBase.width; x++){
            for(int y = 0; y < dustBase.height; y++){
                int rawColor = dustBase.get(x, y);
                int index = switch(rawColor){
                    case 0xffffffff -> 0;
                    case 0xdcc6c6ff, 0xdbc5c5ff -> 1;
                    case 0x9d7f7fff, 0x9e8080ff -> 2;
                    default -> -1;
                };
                out.setRaw(x, y, index == -1 ? dustBase.get(x, y) : palettei[index]);
            }
        }
        Drawf.checkBleed(out);
        packer.add(PageType.main, Vars.content.transformName(name), out);
    }
}
