package cd.world.blocks.multi.craft;

import arc.scene.style.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.meta.*;

import java.util.concurrent.atomic.*;

import static mindustry.Vars.state;

@SuppressWarnings("FieldNotUsedInToString")
public class RecipePair{
    public static final RecipePair EMPTY_RECIPE_PAIR = new RecipePair(Recipe.EMPTY_RECIPE, Recipe.EMPTY_RECIPE);
    public Recipe in, out;
    public float craftTime;

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public int[] liquidOutputDirections = {-1};
    private UnlockableContent iconItem;

    public RecipePair(Recipe in, Recipe out){
        this.in = in;
        this.out = out;
    }

    public RecipePair(){
    }

    public UnlockableContent iconItem(){
        if(iconItem == null){
            if(out.items.any()) iconItem = out.items.get(0).item;
            if(in.items.any()) iconItem = in.items.get(0).item;
        }
        return iconItem;
    }

    public boolean hasLockedItem(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        in.items.each(i -> !i.item.unlockedNow() &&
        !state.rules.hiddenBuildItems.contains(i.item) &&
        !i.item.isHidden(), i -> atomicBoolean.set(true));
        out.items.each(i -> !i.item.unlockedNow() &&
        !state.rules.hiddenBuildItems.contains(i.item) &&
        !i.item.isHidden(), i -> atomicBoolean.set(true));
        return !atomicBoolean.get();
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        RecipePair that = (RecipePair)obj;

        if(!in.equals(that.in)) return false;
        if(craftTime != that.craftTime) return false;
        return out.equals(that.out);
    }

    @Override
    public int hashCode(){
        int result = in.hashCode();
        result = 4 * result + out.hashCode();
        result = 24 * result + Float.floatToIntBits(craftTime);
        return result;
    }

    @Override
    public String toString(){
        return "RecipePair{" +
        "in=" + in +
        ", out=" + out +
        ", craftTime=" + craftTime +
        '}';
    }

    public Table buildStats(){
        var recipeTable = new Table(Tex.pane);
        var recipeStat = genStats();
        buildBasicStats(recipeTable, recipeStat);
        recipeTable.table(((TextureRegionDrawable)Tex.whiteui).tint(Pal.darkestGray), ta -> {
            ta.left().defaults().left();
            ta.add(genRecipe());
        }).margin(5).left().growX().pad(3).growY().get();
        return recipeTable;
    }

    public Stats genStats(){
        Stats stats = new Stats();
        for(var item : in.items){
            stats.add(Stat.input, item);
        }
        for(var liquid : in.liquids){
            stats.add(Stat.input, liquid.liquid, liquid.amount, false);
        }
        if(in.power != 0f) stats.add(Stat.powerUse, in.power * 60f, StatUnit.powerSecond);
        if(in.heat != 0f) stats.add(Stat.input, in.heat, StatUnit.powerSecond);

        for(var item : out.items){
            stats.add(Stat.output, item);
        }
        for(var liquid : out.liquids){
            stats.add(Stat.output, liquid.liquid, liquid.amount, false);
        }
        if(out.power != 0f) stats.add(Stat.basePowerGeneration, out.power * 60f, StatUnit.powerSecond);
        if(out.heat != 0f) stats.add(Stat.output, out.heat, StatUnit.powerSecond);
        return stats;
    }

    /** Thanks Singularity */
    private static void buildBasicStats(Table details, Stats stat){
        for(StatCat cat : stat.toMap().keys()){
            OrderedMap<Stat, Seq<StatValue>> map = stat.toMap().get(cat);
            if(map.size == 0) continue;

            for(Stat state : map.keys()){
                details.table(inset -> {
                    inset.left();
                    inset.add("[lightgray]" + state.localized() + ":[] ").left();
                    Seq<StatValue> arr = map.get(state);
                    for(StatValue value : arr){
                        value.display(inset);
                        inset.add().size(10f);
                    }
                }).fillX().padLeft(10);
                details.row();
            }
        }
    }

    private Table genRecipe(){
        var table = new Table();
        in.buildTable(table);
        table.image(Icon.right).padLeft(8).padRight(8).size(30);
        out.buildTable(table);
        return table;
    }
}

