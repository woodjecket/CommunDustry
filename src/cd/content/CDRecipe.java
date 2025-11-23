package cd.content;

import arc.struct.Seq;
import cd.struct.recipe.Recipe;
import cd.struct.recipe.Recipe.RecipeBuilder;
import cd.struct.recipe.reactant.ReactantAnyItem;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;

public class CDRecipe {
    public static Seq<Recipe> serpulo = new Seq<>();
    //Serpulo recipe
    public static Recipe siliconSmelter, siliconCrucible, kiln, graphitePress, plastaniumCompressor, multiPress,
            phaseWeaver, surgeSmelter, pyratiteMixer, blastMixer, cryofluidMixer,
            melter, separator, disassembler, sporePress, pulverizer, incinerator, coalCentrifuge;

    public static void load() {
        graphitePress = new RecipeBuilder()
                .itemsOut(Items.graphite, 1)
                .itemsIn(Items.coal, 2)
                .time(90)
                .craftEffect(Fx.pulverizeMedium)
                .build();

        multiPress = new RecipeBuilder()
                .itemsOut(Items.graphite, 2)
                .itemsIn(Items.coal, 3)
                .liquidsIn(Liquids.water, 0.1f)
                .powerIn(1.8f)
                .time(30)
                .craftEffect(Fx.pulverizeMedium)
                .build();

        siliconSmelter = new RecipeBuilder()
                .itemsOut(Items.silicon, 1)
                .itemsIn(Items.coal, 1, Items.sand, 2)
                .powerIn(0.50f)
                .time(40)
                .craftEffect(Fx.smeltsmoke)
                .build();

        siliconCrucible = new RecipeBuilder()
                .itemsOut(Items.silicon, 8)
                .itemsIn(Items.coal, 4, Items.sand, 6, Items.pyratite, 1)
                .powerIn(4f)
                .time(90)
                .craftEffect(Fx.smeltsmoke)
                .build();

        kiln = new RecipeBuilder()
                .itemsOut(Items.metaglass, 1)
                .itemsIn(Items.lead, 1, Items.sand, 1)
                .powerIn(0.60f)
                .time(30)
                .craftEffect(Fx.smeltsmoke)
                .build();

        plastaniumCompressor = new RecipeBuilder()
                .itemsOut(Items.plastanium, 1)
                .itemsIn(Items.titanium, 2)
                .liquidsIn(Liquids.oil, 0.25f)
                .powerIn(3f)
                .time(60)
                .craftEffect(Fx.formsmoke)
                .updateEffect(Fx.plasticburn)
                .build();

        phaseWeaver = new RecipeBuilder()
                .itemsOut(Items.phaseFabric, 1)
                .itemsIn(Items.thorium, 4, Items.sand, 10)
                .powerIn(5f)
                .time(120)
                .craftEffect(Fx.smeltsmoke)
                .build();

        surgeSmelter = new RecipeBuilder()
                .itemsOut(Items.surgeAlloy, 1)
                .itemsIn(Items.copper, 3, Items.lead, 4, Items.titanium, 2, Items.silicon, 3)
                .powerIn(4f)
                .time(75)
                .craftEffect(Fx.smeltsmoke)
                .build();

        cryofluidMixer = new RecipeBuilder()
                .liquidsOut(Liquids.cryofluid, 12f / 60f)
                .itemsIn(Items.titanium, 1)
                .liquidsIn(Liquids.water, 12f / 60f)
                .powerIn(1f)
                .time(120)
                .build();

        pyratiteMixer = new RecipeBuilder()
                .itemsOut(Items.pyratite, 1)
                .itemsIn(Items.coal, 1, Items.lead, 2, Items.sand, 2)
                .powerIn(0.20f)
                .time(60)
                .build();

        blastMixer = new RecipeBuilder()
                .itemsOut(Items.blastCompound, 1)
                .itemsIn(Items.pyratite, 1, Items.sporePod, 1)
                .powerIn(0.40f)
                .time(60)
                .build();

        melter = new RecipeBuilder()
                .liquidsOut(Liquids.slag, 12f / 60f)
                .itemsIn(Items.scrap, 1)
                .powerIn(1f)
                .time(10)
                .build();

        separator = new RecipeBuilder()
                .chanceItemOut(Items.copper, 5, Items.lead, 3, Items.graphite, 2, Items.titanium, 2)
                .liquidsIn(Liquids.slag, 4f / 60f)
                .powerIn(1.1f)
                .time(35)
                .build();

        disassembler = new RecipeBuilder()
                .chanceItemOut(Items.sand, 2, Items.graphite, 1, Items.titanium, 1, Items.thorium, 1)
                .itemsIn(Items.scrap, 1)
                .liquidsIn(Liquids.slag, 0.12f)
                .powerIn(4f)
                .time(15)
                .build();

        sporePress = new RecipeBuilder()
                .liquidsOut(Liquids.oil, 18f / 60f)
                .itemsIn(Items.sporePod, 1)
                .powerIn(0.7f)
                .time(20)
                .craftEffect(Fx.none)
                .build();

        pulverizer = new RecipeBuilder()
                .itemsOut(Items.sand, 1)
                .itemsIn(Items.scrap, 1)
                .powerIn(0.50f)
                .time(40)
                .craftEffect(Fx.pulverize)
                .updateEffect(Fx.pulverizeSmall)
                .build();

        coalCentrifuge = new RecipeBuilder()
                .itemsOut(Items.coal, 1)
                .liquidsIn(Liquids.oil, 0.1f)
                .powerIn(0.7f)
                .time(30)
                .craftEffect(Fx.coalSmeltsmoke)
                .build();

        incinerator = new RecipeBuilder()
                .addReactants(new ReactantAnyItem())
                .powerIn(0.50f)
                .time(60)
                .build();
        serpulo.add(Recipe.all);
    }
}
