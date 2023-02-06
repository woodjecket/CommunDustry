package cd.world.component;

import mindustry.gen.*;
import mindustry.world.*;

public interface Component{

    void onUpdateTile(Building b);

    void onDestroyed(Building b);

    void onCreateExplosion(Building b);

    void onCraft(Building b);

    boolean onShouldConsume(Building b);

    float onEfficiencyScale(Building b);

    void onInit(Block b);

    void onSetStats(Block b);

    void onLoad();

    void onPlace(Building b);

    void onDrawPlace(Block b, int x, int y, int rotation);

    void onEntityDraw(Building b);

    void onSetBars(Block b);

}
