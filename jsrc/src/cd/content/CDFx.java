package cd.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.entities.*;
import mindustry.graphics.*;

import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

public final class CDFx{
    public static final Effect

    iceCraft = new Effect(15, e -> randLenVectors(e.id, 6, 4f + e.fin() * 5f, (x, y) -> {
        color(Color.white, Color.valueOf("93ccea"), e.fin());
        Fill.square(e.x + x, e.y + y, 0.5f + e.fout() * 2f, 45);
    })),
    pneuSmoke = new Effect(100, e -> {
        color(Color.gray, Pal.darkishGray, e.fin());
        Fill.circle(e.x, e.y, (7f - e.fin() * 7f) / 3f);
    });

    private CDFx(){
    }
}
