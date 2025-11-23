package cd.ui;

import arc.Core;
import arc.func.Floatp;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.ScissorStack;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.scene.style.Drawable;
import arc.scene.ui.layout.WidgetGroup;
import mindustry.gen.Tex;

public class TableBar extends WidgetGroup {
    private static Rect scissor = new Rect();

    public Floatp fraction;
    public Prov<Color> color;
    public float value, lastValue, blink;

    public TableBar(Floatp fraction, Prov<Color> color) {
        this.fraction = fraction;
        this.color = color;
    }

    @Override
    public void draw() {
        if (fraction == null) return;
        if (color == null || color.get() == null) return;

        float computed = Mathf.clamp(fraction.get());

        if (lastValue > computed) {
            blink = 1f;
            lastValue = computed;
        }

        if (Float.isNaN(lastValue)) lastValue = 0;
        if (Float.isInfinite(lastValue)) lastValue = 1f;
        if (Float.isNaN(value)) value = 0;
        if (Float.isInfinite(value)) value = 1f;
        if (Float.isNaN(computed)) computed = 0;
        if (Float.isInfinite(computed)) computed = 1f;

        blink = Mathf.lerpDelta(blink, 0f, 0.2f);
        value = Mathf.lerpDelta(value, computed, 0.15f);

        Drawable bar = Tex.bar;

        Draw.colorl(0.1f);
        Draw.alpha(parentAlpha);
        bar.draw(x, y, width, height);
        Draw.color(color.get());
        Draw.alpha(parentAlpha);

        Drawable top = Tex.barTop;
        float topWidth = width * value;

        if (topWidth > Core.atlas.find("bar-top").width) {
            top.draw(x, y, topWidth, height);
        } else {
            if (ScissorStack.push(scissor.set(x, y, topWidth, height))) {
                top.draw(x, y, Core.atlas.find("bar-top").width, height);
                ScissorStack.pop();
            }
        }

        Draw.color();
        var e = children.first();
        e.setPosition(width / 2, height / 2);
        super.draw();
    }


}
