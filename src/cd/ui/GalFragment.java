package cd.ui;

import arc.*;
import arc.graphics.*;
import arc.input.*;
import arc.scene.*;
import arc.util.*;
import mindustry.gen.*;

public class GalFragment{
    public static final Color ALPHA_WHITE = new Color(0xffffffbb);
    public static int count;
    public static boolean autoPlay;
    public static int autoPlayMultiplier = 1;

    private static String[] testFlight = {"庄子", "鲦鱼出游从容，是鱼之乐也。", "惠子", "子非鱼，安知鱼之乐？", "庄子", "子非我，安知我不知鱼之乐？", "惠子", "我非子，固不知子矣；子固非鱼也，子之不知鱼之乐，全矣！", "庄子", "请循其本。子曰“汝安知鱼乐”云者，既已知吾知之而问我，我知之濠上也。"};
    public static String currentAvtar = testFlight[count];
    public static String currentText = testFlight[count + 1];

    public static void build(Group parent){
        tachieLayer(parent);

        speechLayer(parent);
    }

    private static void tachieLayer(Group parent){
        parent.fill(full -> {

            full.center().bottom().visible(() -> true);

            full.table(frame -> {

                frame.center().bottom();

                frame.table(left -> {
                    final boolean[] scaling = {false};
                    left.image(Core.atlas.find("commundustry-v4-animdustry-alpha")).self(c -> {
                        c.get().setScaling(Scaling.bounded);
                        c.get().update(() -> {
                            if(!scaling[0]){
                                if(count % 2 == 0){
                                    scaling[0] = true;
                                    c.get().addAction(new ColorAndScaleToAction(){
                                        {
                                            setDuration(0.1f);
                                            setScale(1f);
                                            endColor = Color.white;
                                        }
                                        public void end(){
                                            scaling[0] = false;
                                            c.get().color.set(Color.white);
                                        }
                                    });
                                }else{
                                    scaling[0] = true;
                                    c.get().addAction(new ColorAndScaleToAction(){
                                        {
                                            setDuration(0.15f);
                                            setScale(0.8f);
                                            endColor = ALPHA_WHITE;
                                        }

                                        public void end(){
                                            scaling[0] = false;
                                        }
                                    });
                                }
                            }
                        });
                    }).left().bottom();
                }).size(300f, 600f);

                frame.table().size(400f, 1f);

                frame.table(right -> {
                    final boolean[] scaling = {false};
                    right.image(Core.atlas.find("commundustry-v4-animdustry-zenith")).self(c -> {
                        c.get().setScaling(Scaling.bounded);
                        c.get().update(() -> {
                            if(!scaling[0]){
                                if(count % 2 != 0){
                                    scaling[0] = true;
                                    c.get().addAction(new ColorAndScaleToAction(){
                                        {
                                            setDuration(0.1f);
                                            setScale(1f);
                                            endColor = Color.white;
                                        }
                                        public void end(){
                                            scaling[0] = false;
                                            c.get().color.set(Color.white);
                                        }
                                    });
                                }else{
                                    scaling[0] = true;
                                    c.get().addAction(new ColorAndScaleToAction(){
                                        {
                                            setDuration(0.15f);
                                            setScale(0.8f);
                                            endColor = ALPHA_WHITE;
                                        }

                                        public void end(){
                                            scaling[0] = false;
                                        }
                                    });
                                }
                            }
                        });
                    }).right().bottom();
                }).size(300f, 600f);
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
