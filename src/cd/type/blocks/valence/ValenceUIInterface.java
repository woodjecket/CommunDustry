package cd.type.blocks.valence;

import cd.type.valence.Formula;
import cd.type.valence.RMap;

public interface ValenceUIInterface {
    void configureBuild(Object obj);

    void updateUI();

    boolean is2D();

    RMap getMap();

    Formula getFormula();

    void updateFormula();


}
