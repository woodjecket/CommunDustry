package cd.struct.gal.act;

import cd.manager.*;
import cd.struct.gal.*;

public class SpeechAct extends Act{
    public Avtar avtar;
    public String text;

    public SpeechAct(Avtar avtar, String text){
        this.avtar = avtar;
        this.text = text;
    }

    @Override
    public boolean act(){
        GalManager.speakingAvtar = avtar;
        GalManager.currentText = text;
        return false;
    }
}
