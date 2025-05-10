package cd.ui;

import arc.input.*;
import arc.scene.*;
import arc.util.*;
import mindustry.gen.*;

public class GalFragment{
    public static boolean autoPlay = false;
    public static int autoPlayMultiplier = 1;
    public static String currentAvtar = "Zenith";
    public static String currentText = "Hello world.";

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
        currentText += "4";
    }
}
