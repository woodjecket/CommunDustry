package cd.entities.component.pneu;

import cd.entities.component.*;
import cd.entities.stat.*;
import cd.type.blocks.pneumatic.*;
import mindustry.gen.*;
import mindustry.world.*;

public class PneuCompressorComponent extends BaseComponent{
    public float outputPressure = 1f;

    @Override
    public boolean onShouldConsume(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        return bPneu.getPressure() < parentComponent.getMaxOperatePressure();
    }

    @Override
    public void onCraft(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        bPneu.setPressure(bPneu.getPressure() + outputPressure);
    }

    @Override
    public void onSetStats(Block b){
        b.stats.add(CDStat.pressureOutput, outputPressure, CDStat.perConsume);
    }

}
