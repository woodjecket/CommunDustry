package cd.content;

import arc.graphics.*;
import cd.entities.component.MultiComponent;
import cd.entities.component.pneu.PneuComponent;
import cd.entities.component.pneu.PneuCompressorComponent;
import cd.entities.component.pneu.PneuCrafterComponent;
import cd.type.blocks.ComponentBlock;
import cd.type.blocks.ComponentCrafter;
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

    public static Block 
        basicFreezer,
        basicDirectlyH2O2Crafter,
        basicElectrolyzer,
        pneuconduit,
        pneucrafter,
        pneucompressor;

    public void load() {
        basicFreezer = new GenericCrafter("basic-freezer") {
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
                craftEffect = CDFx.iceCraft;
                outputItem = new ItemStack(CDItems.ice, 1);
                craftTime = 60f;
                size = 4;
                hasPower = true;
                hasLiquids = false;
                drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("93ccea")));
                ambientSound = Sounds.smelter;
                ambientSoundVolume = 0.07f;
                consumeLiquid(Liquids.water, 0.1f);
            }
        };
        basicDirectlyH2O2Crafter = new ComponentCrafter("basic-directly-h2o2-crafter") {
            {
                requirements(Category.crafting,
                        with(Items.lead, 20, Items.silicon, 60, Items.titanium, 80, Items.graphite, 100));
                craftEffect = Fx.freezing;
                outputLiquid = new LiquidStack(CDLiquids.H2O2, 0.1f);
                craftTime = 120f;
                size = 4;
                hasPower = true;
                hasLiquids = false;
                drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("7666c6")));
                ambientSound = Sounds.smelter;
                ambientSoundVolume = 0.07f;
                consumeLiquids(new LiquidStack[] { new LiquidStack(Liquids.hydrogen, 0.05f),
                        new LiquidStack(Liquids.ozone, 2f / 60f) });
                component = CDComponent.basicDirectlyH2O2CrafterCatalyzerComponent;
            }
        };
        basicElectrolyzer = new GenericCrafter("basic-electrolyzer") {
            {
                requirements(Category.crafting,
                        with(Items.copper, 50, Items.lead, 40, Items.silicon, 130, Items.graphite, 80));
                size = 4;

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
                        new DrawBubbles(Color.valueOf("7693e3")) {
                            {
                                sides = 10;
                                recurrence = 3f;
                                spread = 6;
                                radius = 1.5f;
                                amount = 20;
                            }
                        },
                        new DrawRegion(),
                        new DrawLiquidOutputs(),
                        new DrawGlowRegion() {
                            {
                                alpha = 0.7f;
                                color = Color.valueOf("c4bdf3");
                                glowIntensity = 0.3f;
                                glowScale = 6f;
                            }
                        });

                ambientSound = Sounds.electricHum;
                ambientSoundVolume = 0.08f;

                regionRotated1 = 3;
                outputLiquids = LiquidStack.with(Liquids.ozone, 4f / 60, Liquids.hydrogen, 6f / 60);
                liquidOutputDirections = new int[] { 1, 3 };
            }
        };
        pneuconduit = new ComponentBlock("conduit") {{
            requirements(Category.distribution,
            with(Items.copper, 50, Items.lead, 40, Items.silicon, 130, Items.graphite, 80));
            component = new PneuComponent();
            hasPressure = true;
            visualExplodePressure = 15f;
        }};
        pneucrafter = new ComponentCrafter("crafter"){{
            requirements(Category.crafting,
            with(Items.copper, 50, Items.lead, 40, Items.silicon, 130, Items.graphite, 80));
            component = new MultiComponent(new PneuComponent(), new PneuCrafterComponent());
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            outputItem = new ItemStack(Items.graphite, 1);
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consumeItem(Items.coal, 2);
            hasPressure = true;
            visualExplodePressure = 15f;
        }};
        pneucompressor = new ComponentCrafter("compressor"){{
            requirements(Category.crafting,
            with(Items.copper, 50, Items.lead, 40, Items.silicon, 130, Items.graphite, 80));
            requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
            outputItem = new ItemStack(Items.graphite, 1);
            craftTime = 90f;
            size = 2;
            hasItems = true;
            consumeItem(Items.coal, 2);
            component = new MultiComponent(new PneuComponent(), new PneuCompressorComponent());
            hasPressure = true;
            visualExplodePressure = 15f;
        }};
    }
}

