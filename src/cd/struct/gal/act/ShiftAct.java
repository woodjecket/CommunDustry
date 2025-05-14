package cd.struct.gal.act;

import cd.manager.*;
import cd.struct.gal.*;

public class ShiftAct extends Act{
    public Direction direction;
    public Avtar avtar;

    public ShiftAct(Direction direction, Avtar avtar){
        this.direction = direction;
        this.avtar = avtar;
    }

    @Override
    public boolean act(){
        if(direction == Direction.left){
            GalManager.leftAvtar = avtar;
        }else{
            GalManager.rightAvtar = avtar;
        }
        return false;
    }


    public static enum Direction{
        left, right;
    }
}
