package cd.content;

import arc.struct.*;
import cd.struct.gal.*;
import cd.struct.gal.act.*;

import static cd.content.Avtars.*;

public class Plots{
    public static Plot plotTest = new Plot(){{
        acts = new Seq<>(new Act[]{
        new FadeInAct(zenith,alpha),
        new SpeechAct(zenith,"鲦鱼出游从容，是鱼之乐也。"),
        new SpeechAct(alpha,"子非鱼，安知鱼之乐？"),
        new SpeechAct(zenith,"子非我，安知我不知鱼之乐？"),
        new SpeechAct(alpha,"我非子，固不知子矣；子固非鱼也，子之不知鱼之乐，全矣！"),
        new SpeechAct(zenith,"请循其本。子曰“汝安知鱼乐”云者，既已知吾知之而问我，我知之濠上也。"),
        new FadeOutAct(),
        });
    }};

    public static void load(){

    }

}
