package cd.struct.gal.act;

import cd.manager.*;
import cd.struct.gal.*;

public class FadeOutAct extends Act{
    @Override
    public boolean act(){
        GalManager.visible = false;
        return false;
    }
}
