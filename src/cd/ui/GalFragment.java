package cd.ui;

import arc.input.*;
import arc.scene.*;
import arc.util.*;
import mindustry.gen.*;

public class GalFragment{
    public static String[] testFlight = {
    "庄子", "鲦鱼出游从容，是鱼之乐也。",
    "惠子", "子非鱼，安知鱼之乐？",
    "庄子", "子非我，安知我不知鱼之乐？",
    "惠子", "我非子，固不知子矣；子固非鱼也，子之不知鱼之乐，全矣！",
    "庄子", "请循其本。子曰“汝安知鱼乐”云者，既已知吾知之而问我，我知之濠上也。"
    };
    public static int count = 0;
    public static boolean autoPlay = false;
    public static int autoPlayMultiplier = 1;
    public static String currentAvtar = testFlight[count];
    public static String currentText = testFlight[count + 1];

    public static void build(Group parent){
        parent.fill(full -> {
            full.center().bottom().visible(() -> true);

            full.table(Tex.buttonEdge2, outline -> {
                outline.table(special -> {
                    special.table(left -> {
                        left.table(Tex.buttonEdge2, avtar -> {
                            avtar.label(() -> currentAvtar).grow().marginRight(5f);
                        }).growY();
                        left.left();
                    }).grow();
                    special.table(right -> {
                        right.button(Icon.playSmall, 15f, () -> {
                            autoPlay = true;
                            currentText += "3";
                        });
                        right.button(Icon.pencilSmall, 15f, () -> {
                            autoPlayMultiplier = autoPlayMultiplier == 1 ? 2 : 1;
                            currentText = currentText + autoPlayMultiplier;
                        });
                        right.right();
                    }).grow();
                }).grow().size(750f, 35f).row();


                outline.table(Tex.buttonEdge2, speech -> {
                    speech.labelWrap(() -> currentText).grow().get().setAlignment(Align.topLeft);
                    speech.clicked(KeyCode.mouseLeft, GalFragment::toggleText);
                }).size(750f, 195f);


            }).size(800f, 250f);
        });
    }

    private static void toggleText(){
        count ++ ;
        count %= testFlight.length / 2;
        currentAvtar = testFlight[count * 2];
        currentText = testFlight[count * 2 + 1];
    }
}
