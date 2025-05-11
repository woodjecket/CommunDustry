package cd.ui;

import arc.*;
import arc.input.*;
import arc.scene.*;
import arc.util.*;
import mindustry.gen.*;

public class GalFragment{
    private static String[] testFlight = {"庄子", "鲦鱼出游从容，是鱼之乐也。", "惠子", "子非鱼，安知鱼之乐？", "庄子", "子非我，安知我不知鱼之乐？", "惠子", "我非子，固不知子矣；子固非鱼也，子之不知鱼之乐，全矣！", "庄子", "请循其本。子曰“汝安知鱼乐”云者，既已知吾知之而问我，我知之濠上也。"};
    public static int count ;
    public static boolean autoPlay ;
    public static int autoPlayMultiplier = 1;
    public static String currentAvtar = testFlight[count];
    public static String currentText = testFlight[count + 1];

    public static void build(Group parent){
        speechLayer(parent);

        parent.fill(full -> {

            full.center().bottom().visible(() -> true);

            full.table(frame -> {
                frame.center().bottom();
                frame.image(Core.atlas.find("commundustry-v4-animdustry-alpha")).self(c -> {
                    c.get().setScaling(Scaling.bounded);
                    c.get().setOrigin(128f,1024f);
                    c.get().update(() -> c.get().setScale(count % 2 != 0 ? 0.8f: 1f));
                });
                frame.table().size(650f, 1f);
                frame.image(Core.atlas.find("commundustry-v4-animdustry-zenith")).self(c -> {
                    c.get().setScaling(Scaling.bounded);
                    c.get().setOrigin(128f,1024f);
                    c.get().update(() -> c.get().setScale(count % 2 == 0 ? 0.8f: 1f));
                });
            });
        });

    }

    private static void speechLayer(Group parent){
        parent.fill(full -> {
            full.center().bottom().visible(() -> true);

            full.table(Tex.buttonEdge2, outline -> {

                outline.table(special -> {

                    special.table(left -> {

                        left.table(Tex.buttonEdge2, avtar -> avtar.label(() -> currentAvtar).grow().marginRight(5f)).growY();
                        left.left();

                    }).grow();

                    special.table(right -> {

                        right.button(Icon.playSmall, 15f, () -> autoPlay = true);
                        right.button(Icon.pencilSmall, 15f, () -> autoPlayMultiplier = autoPlayMultiplier == 1 ? 2 : 1);
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
        count++;
        count %= testFlight.length / 2;
        currentAvtar = testFlight[count * 2];
        currentText = testFlight[count * 2 + 1];
    }
}
