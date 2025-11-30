package cd.ctype;

import arc.util.Nullable;
import cd.content.Avatars;
import cd.content.Plots;
import cd.struct.recipe.Recipe;
import cd.struct.vein.VeinType;

/** Don't use ordinal(), use id instead*/
public enum ExtendContentType {
    /*
    Vanilla Coordination
    item(Item.class, 0),
    block(Block.class, 1),
    bullet(BulletType.class, 3),
    liquid(Liquid.class, 4),
    status(StatusEffect.class, 5),
    unit(UnitType.class, 6),
    weather(Weather.class, 7),
    sector(SectorPreset.class, 9),
    error(null,12),
    planet(Planet.class,13),
    team(TeamEntry.class,15),
    unitCommand(UnitCommand.class,16),
    unitStance(UnitStance.class,17),
    */

    avatar(Avatars.class, 0),
    plot(Plots.class, 1),
    recipe(Recipe.class, 2),
    vein(VeinType.class, 3);

    public static final ExtendContentType[] all = values();

    public final @Nullable Class<?> contentClass;
    public final int id;
    ExtendContentType(Class<?> contentClass, int id){
        this.contentClass = contentClass;
        this.id = id;
    }

}
