package cd.entities.component.pneu;

import cd.entities.component.BaseComponent;
import cd.type.blocks.pneumatic.PneuInterface;
import mindustry.gen.Building;

public class PneuCrafterComponent extends BaseComponent{
    public float maxOperatePressure = 10f;
    public float minOperatePressure = 2f;
    public float pressureConsume = 0.1f;

    @Override
    public boolean onShouldConsume(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        return bPneu.getPressure() > minOperatePressure && bPneu.getPressure() < maxOperatePressure;
    }

    @Override
    public void onCraft(Building b){
        PneuInterface bPneu = (PneuInterface) b;
        bPneu.setPressure(bPneu.getPressure()-pressureConsume);
    }
    
}
