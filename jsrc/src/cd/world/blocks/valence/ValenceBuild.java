package cd.world.blocks.valence;

import cd.type.valence.*;

import static cd.type.valence.ItemsValence.ValenceModule;

public interface ValenceBuild{

    float getHeat();

    void setHeat(float amount);

    Formula getFormula();

    ValenceModule getModule();

    void updateValence();

    ResultMap getMap();

    int getValence();

    void updateDialog();
}
