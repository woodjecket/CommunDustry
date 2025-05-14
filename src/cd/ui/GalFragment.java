package cd.ui;

import arc.graphics.*;
import arc.input.*;
import arc.scene.*;
import arc.util.*;
import cd.manager.*;
import mindustry.content.*;
import mindustry.gen.*;

public class GalFragment{
    public static final Color ALPHA_WHITE = new Color(0xffffffbb);

    public static void build(Group parent){
        //illustLayer(parent);
        //optionLayer(parent);
        tachieLayer(parent);
        speechLayer(parent);
    }

    private static void illustLayer(Group parent){
        parent.fill(full -> {
            full.center().visible(() -> true);
            full.table(Tex.buttonEdge2, outline -> {
                outline.image(Blocks.router.region).size(60f).get().setScaling(Scaling.fill);
            });
        });
    }

    private static void optionLayer(Group parent){
        parent.fill(full -> {
            full.center().visible(() -> true);
            full.table(Tex.buttonEdge2, outline -> {
                outline.button("yes", () -> {
                    GalManager.currentText += "yes";
                }).minSize(300f, 50f).row();
                outline.button("我非子，固不知子矣；子固非鱼也，子之不知鱼之乐，全矣！", () -> {
                    GalManager.currentText += "no";
                }).minSize(300f, 50f);
            });
        });
    }

    private static void tachieLayer(Group parent){
        parent.fill(full -> {

            full.center().bottom();

            full.table(frame -> {

                frame.center().bottom();

                frame.table(left -> {
                    final boolean[] scaling = {false};
                    left.image(Tex.whiteui).self(c -> {
                        c.get().setScaling(Scaling.bounded);

                        c.get().update(() -> {

                            if(GalManager.leftAvtar != null){
                                c.get().setDrawable(GalManager.leftAvtar.tachie);
                            }else{
                                c.get().setDrawable(Tex.whiteui);
                            }

                            if((!scaling[0])){
                                if(GalManager.speakingAvtar == GalManager.leftAvtar && GalManager.frontAvtar != GalManager.leftAvtar){

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
                                            GalManager.frontAvtar = GalManager.leftAvtar;
                                        }
                                    });
                                }else if(GalManager.speakingAvtar != GalManager.leftAvtar && GalManager.frontAvtar == GalManager.leftAvtar){

                                    scaling[0] = true;
                                    c.get().addAction(new ColorAndScaleToAction(){
                                        {
                                            setDuration(0.15f);
                                            setScale(0.8f);
                                            endColor = ALPHA_WHITE;
                                        }

                                        public void end(){
                                            scaling[0] = false;
                                            GalManager.frontAvtar = null;
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
                    right.image(Tex.whiteui).self(c -> {
                        c.get().setScaling(Scaling.bounded);

                        c.get().update(() -> {

                            if(GalManager.rightAvtar != null){
                                c.get().setDrawable(GalManager.rightAvtar.tachie);
                            }else{
                                c.get().setDrawable(Tex.whiteui);
                            }

                            if((!scaling[0])){
                                if(GalManager.speakingAvtar == GalManager.rightAvtar && GalManager.frontAvtar != GalManager.rightAvtar){

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
                                            GalManager.frontAvtar = GalManager.rightAvtar;
                                        }
                                    });
                                }else if(GalManager.speakingAvtar != GalManager.rightAvtar && GalManager.frontAvtar == GalManager.rightAvtar){

                                    scaling[0] = true;
                                    c.get().addAction(new ColorAndScaleToAction(){
                                        {
                                            setDuration(0.15f);
                                            setScale(0.8f);
                                            endColor = ALPHA_WHITE;
                                        }

                                        public void end(){
                                            scaling[0] = false;
                                            GalManager.frontAvtar = null;
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
            full.center().bottom();

            full.table(Tex.buttonEdge2, outline -> {

                outline.table(special -> {

                    special.table(left -> {

                        left.table(Tex.buttonEdge2, avtar -> avtar.label(() -> GalManager.speakingAvtar == null ? "" : GalManager.speakingAvtar.name).grow().marginRight(5f)).growY();
                        left.left();

                    }).grow();

                    special.table(right -> {

                        right.button(Icon.playSmall, 15f, () -> GalManager.autoPlay = true);
                        right.button(Icon.pencilSmall, 15f, () -> GalManager.autoPlayMultiplier = GalManager.autoPlayMultiplier == 1 ? 2 : 1);
                        right.right();

                    }).grow();

                }).grow().size(750f, 35f).row();

                outline.table(Tex.buttonEdge2, speech -> {

                    speech.labelWrap(() -> GalManager.currentText).grow().get().setAlignment(Align.topLeft);
                    speech.clicked(KeyCode.mouseLeft, GalFragment::toggleText);

                }).size(750f, 195f);

            }).size(800f, 250f);
        });
    }

    private static void toggleText(){
        GalManager.ensue();
    }
}
