package cd.world.block.environment;

import cd.CDMod;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.OverlayFloor;

public class FiniteOre extends OreBlock implements Finite{
    public OverlayFloor exhausted;

    public FiniteOre(Item ore) {
        super(ore);
    }


    @Override
    public void drawBase(Tile tile){
        super.drawBase(tile);

        }

    @Override
    public boolean updateRender(Tile tile) {
        return true;
    }

    @Override
    public void renderUpdate(UpdateRenderState r) {
        super.renderUpdate(r);
        var tile = r.tile;
        int amount = CDMod.oreUpdater.amountMap.get(tile,100);
        Vars.ui.showLabel(String.valueOf(amount),1,tile.worldx(),tile.worldy());
    }

    @Override
    public OverlayFloor exhausted() {
        return exhausted;
    }
}
