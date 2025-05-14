package cd.struct.gal;

import arc.struct.*;

/** A class to describe a plot */
public class Plot{
    public Seq<Act> acts = new Seq<>();
    private int _index;

    public void ensue(){
        while(acts.get(_index).act()){
            _index++;
        }
        _index++;
        _index %= acts.size;
    }
}
