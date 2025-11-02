package cd.ui;

import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Nullable;
import arc.util.Scaling;
import mindustry.core.UI;
import mindustry.ctype.UnlockableContent;
import mindustry.ui.Styles;

public class UIUtils {

    public static Stack stack(TextureRegion region, float amount){
        return stack(region,amount,32f);
    }
    public static Stack stack(TextureRegion region, int amount){
        return stack(region,amount,32f);
    }

        public static Stack stack(TextureRegion region, float amount, float size){
        Stack stack = new Stack();

        stack.add(new Table(o -> {
            o.left();
            o.add(new Image(region)).size(size).scaling(Scaling.fit);
        }));

        if(amount != 0){
            stack.add(new Table(t -> {
                t.left().bottom();
                t.add(amount + "").style(Styles.outlineLabel).get().setFontScale(0.7f);
                t.pack();
            }));
        }

        return stack;
    }

    public static Stack stack(TextureRegion region, int amount,float size){
        Stack stack = new Stack();

        stack.add(new Table(o -> {
            o.left();
            o.add(new Image(region)).size(size).scaling(Scaling.fit);
        }));

        if(amount != 0){
            stack.add(new Table(t -> {
                t.left().bottom();
                t.add(amount + "").style(Styles.outlineLabel).get().setFontScale(0.7f);
                t.setScale(0.7f);
                t.pack();
            }));
        }

        return stack;
    }
}
