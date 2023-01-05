package cd.entities.component.pneu;

import arc.util.Log;
import cd.entities.component.BaseComponent;
import cd.type.blocks.pneumatic.PneuInterface;
import mindustry.gen.Building;

public class PneuCompressorComponent extends BaseComponent{
    public float maxOperatePressure = 10f;
    public float outputPressure = 1f;

    @Override
    public boolean onShouldConsume(Building b){
        PneuInterface bPneu = (PneuInterface)b;
        return bPneu.getPressure() < maxOperatePressure;
        //return true;
    }

    @Override
    public void onCraft(Building b){
        PneuInterface bPneu = (PneuInterface) b;
        bPneu.setPressure(bPneu.getPressure()+outputPressure);
    } 
    
}
