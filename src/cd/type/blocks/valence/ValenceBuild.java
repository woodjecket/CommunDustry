package cd.type.blocks.valence;

import cd.type.valence.Formula;
import cd.type.valence.ResultMap;
import cd.ui.ValenceMapDialog;

import static cd.type.valence.ItemsValence.*;

public interface ValenceBuild{

    void setHeat(float amount);

    float getHeat();

    Formula getFormula();

    ValenceModule getModule();

    void updateValence();

    ResultMap getMap();

    int getValence();

    void updateDialog();
}
