package cd.content;

import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import mindustry.entities.Effect;

public class CDFx {
    public static final Effect

    iceCraft = new Effect(15, e -> {
        randLenVectors(e.id, 6, 4f + e.fin() * 5f, (x, y) -> {
            color(Color.white, Color.valueOf("93ccea"), e.fin());
            Fill.square(e.x + x, e.y + y, 0.5f + e.fout() * 2f, 45);
        });
    });
}
