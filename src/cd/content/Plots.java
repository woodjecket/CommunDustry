package cd.content;

import arc.struct.*;
import cd.struct.gal.*;
import cd.struct.gal.act.*;

import static cd.content.Avtars.*;

public class Plots{
    public static Plot plotTest = new Plot(){{
        acts = new Seq<>(new Act[]{
        new FadeInAct(zenith,alpha),
        new FadeOutAct(),
        });
    }};

    public static void load(){

    }

}
