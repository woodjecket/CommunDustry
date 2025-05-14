package cd.struct.gal.act;

import cd.manager.*;
import cd.struct.gal.*;

public class FadeInAct extends Act{
    public Avtar left,right;

    public FadeInAct(Avtar left, Avtar right){
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean act(){
        GalManager.leftAvtar = left;
        GalManager.rightAvtar = right;
        GalManager.visible = true;
        return true;
    }
}
