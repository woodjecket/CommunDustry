package cd.content;


import arc.graphics.*;
import cd.type.blocks.CatalyzerCrafter;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import static mindustry.type.ItemStack.*;

public class CDBlocks {
    public static Block basicFreezer,basicDirectlyHydrogenPeroxideCrafter,basicElectrolyzer;

    public void load() {
        basicFreezer = new GenericCrafter("basic-freezer"){{
            requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
            craftEffect = CDFx.iceCraft;
            outputItem = new ItemStack(CDItems.ice, 1);
            craftTime = 60f;
            size = 2;
            hasPower = true;
            hasLiquids = false;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.07f;
            consumeLiquid(Liquids.water, 0.1f);
        }};

        basicDirectlyHydrogenPeroxideCrafter = new CatalyzerCrafter("basic-directly-hydrogen-peroxide-crafter"){{
            requirements(Category.crafting, with(Items.lead, 20, Items.silicon, 60, Items.titanium, 80, Items.graphite, 100));
            craftEffect = Fx.freezing;
            outputLiquid = new LiquidStack(CDLiquids.hydrogenPeroxide, 0.1f);
            craftTime = 120f;
            size = 3;
            hasPower = true;
            hasLiquids = false;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("7666c6")));
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.07f;
            consumeLiquids(new LiquidStack[]{new LiquidStack(Liquids.hydrogen, 0.05f),new LiquidStack(Liquids.ozone, 2f / 60f)});
            catalyzerNecessity = true;
            catalyzer = with(CDItems.platinum,2);
            catalyzerScale = new float[]{1f};
            catalyzerChance = 0.0001f;
        }};
        basicElectrolyzer = new GenericCrafter("basic-electrolyzer"){{
            requirements(Category.crafting, with(Items.copper, 50, Items.lead, 40, Items.silicon, 130, Items.graphite, 80));
            size = 3;

            researchCostMultiplier = 1.2f;
            craftTime = 10f;
            rotate = true;
            invertFlip = true;

            liquidCapacity = 50f;

            consumeLiquid(Liquids.water, 10f / 60f);
            consumePower(1f);

            drawer = new DrawMulti(
                new DrawRegion("-bottom"),
                new DrawLiquidTile(Liquids.water, 2f),
                new DrawBubbles(Color.valueOf("7693e3")){{
                    sides = 10;
                    recurrence = 3f;
                    spread = 6;
                    radius = 1.5f;
                    amount = 20;
                }},
                new DrawRegion(),
                new DrawLiquidOutputs(),
                new DrawGlowRegion(){{
                    alpha = 0.7f;
                    color = Color.valueOf("c4bdf3");
                    glowIntensity = 0.3f;
                    glowScale = 6f;
                }}
            );

            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.08f;

            regionRotated1 = 3;
            outputLiquids = LiquidStack.with(Liquids.ozone, 4f / 60, Liquids.hydrogen, 6f / 60);
            liquidOutputDirections = new int[]{1, 3};
        }};
        
    }
}
