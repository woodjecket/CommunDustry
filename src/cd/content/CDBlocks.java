package cd.content;

import arc.graphics.*;
import cd.struct.recipe.Recipe;
import cd.world.block.*;
import cd.world.block.environment.*;
import mindustry.content.*;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.power.Battery;
import mindustry.world.blocks.power.SolarGenerator;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

import static mindustry.type.ItemStack.with;

public class CDBlocks {
    public static FiniteOre finiteCopper;

    public static Airport airport;

    public static Block ashWall, ashFloor, ashBoulder, deadSapling, enrichedSandFloor, enrichedSandWall, enrichedSandBoulder,
            graniteFloor, graniteWall, graniteBoulder,
            permafrostFloor, permafrostWall, vine;

    public static CoreBlock industryCentrum;

    public static SolarGenerator ev;

    public static Drill smallDrill;

    public static Battery hvWire, smallBattery;

    public static Conveyor cdConveyor;

    public static MultiCrafter smallFurnace, smallPresser, smallArcFurnace;

    public static Pump smallPump;

    public static PowerTurret alert, shore;

    public static void load() {
        finiteCopper = new FiniteOre(Items.copper);
        airport = new Airport("airport") {{
            requirements(Category.crafting, ItemStack.with());
        }};
        ashWall = new StaticWall("ash-wall");
        enrichedSandWall = new StaticWall("enriched-sand-wall");
        graniteWall = new StaticWall("granite-wall");
        permafrostWall = new StaticWall("permafrost-wall");

        ashFloor = new Floor("ash-floor");
        enrichedSandFloor = new Floor("enriched-sand-floor");
        graniteFloor = new Floor("granite-floor");
        permafrostFloor = new Floor("permafrost-floor");

        ashBoulder = new Prop("ash-boulder") {{
            ashFloor.asFloor().decoration = this;
        }};
        enrichedSandBoulder = new Prop("enriched-sand-boulder") {{
            enrichedSandFloor.asFloor().decoration = this;
        }};
        graniteBoulder = new Prop("granite-boulder") {{
            graniteFloor.asFloor().decoration = this;
        }};
        deadSapling = new StaticTree("dead-sapling");
        vine = new Prop("vine") {{
            alwaysReplace = false;
            instantDeconstruct = false;
        }};

        industryCentrum = new CoreBlock("industry-centrum") {{
            size = 5;
            requirements(Category.effect, new ItemStack[]{});
        }};

        ev = new SolarGenerator("ev") {{
            size = 4;
            powerProduction = 2f;
            requirements(Category.power, new ItemStack[]{});
        }};

        smallDrill = new Drill("small-drill") {{
            size = 2;
            tier = 5;
            requirements(Category.production, new ItemStack[]{});
        }};

        hvWire = new Battery("hv-wire") {{
            size = 1;
            consumePowerBuffered(10f);
            requirements(Category.power, new ItemStack[]{});
        }};

        smallBattery = new Battery("small-battery") {{
            size = 2;
            consumePowerBuffered(4000f);
            requirements(Category.power, new ItemStack[]{});
        }};

        cdConveyor = new Conveyor("cd-conveyor") {{
            requirements(Category.distribution, new ItemStack[]{});
            speed = 0.08f;
        }};

        smallFurnace = new MultiCrafter("small-furnace") {{
            size = 3;
            recipes.recipes.add(new Recipe.RecipeBuilder()
                    .itemsIn(Items.sand, 2, Items.coal, 2)
                    .powerIn(1f)
                    .itemsOut(Items.silicon, 1)
                    .build()
            );
            requirements(Category.crafting, new ItemStack[]{});
        }};

        smallArcFurnace = new MultiCrafter("small-arc-furnace") {{
            size = 3;
            recipes.recipes.add(
                    new Recipe.RecipeBuilder()
                            .itemsIn(Items.sand, 2, Items.graphite, 2)
                            .powerIn(1f)
                            .itemsOut(Items.silicon, 1)
                            .build(),
                    new Recipe.RecipeBuilder()
                            .itemsIn(Items.sand, 2, Items.coal, 2)
                            .powerIn(1f)
                            .itemsOut(Items.silicon, 1)
                            .build()
            );
            requirements(Category.crafting, new ItemStack[]{});
        }};

        smallPresser = new MultiCrafter("small-presser") {{
            size = 2;
            recipes.recipes.add(
                    new Recipe.RecipeBuilder()
                            .itemsIn(Items.coal,2)
                            .powerIn(1f)
                            .itemsOut(Items.graphite, 1)
                            .parallel(2)
                            .build(),
                    new Recipe.RecipeBuilder()
                            .itemsIn(Items.graphite, 2)
                            .powerIn(1f)
                            .itemsOut(Items.surgeAlloy,1)
                            .build()
            );
            requirements(Category.crafting, new ItemStack[]{});
        }};

        smallPump = new Pump("small-pump") {{
            size = 2;
            requirements(Category.liquid, new ItemStack[]{});
        }};

        alert = new PowerTurret("alert") {{
            size = 2;
            requirements(Category.turret, new ItemStack[]{});
            range = 165f;

            shoot.firstShotDelay = 40f;

            recoil = 2f;
            reload = 80f;
            shake = 2f;
            shootEffect = Fx.lancerLaserShoot;
            smokeEffect = Fx.none;
            heatColor = Color.red;
            size = 2;
            scaledHealth = 280;
            targetAir = false;
            moveWhileCharging = false;
            accurateDelay = false;
            shootSound = Sounds.laser;
            coolant = consumeCoolant(0.2f);

            consumePower(6f);

            shootType = new LaserBulletType(140) {{
                colors = new Color[]{Pal.lancerLaser.cpy().a(0.4f), Pal.lancerLaser, Color.white};
                //TODO merge
                chargeEffect = new MultiEffect(Fx.lancerLaserCharge, Fx.lancerLaserChargeBegin);

                buildingDamageMultiplier = 0.25f;
                hitEffect = Fx.hitLancer;
                hitSize = 4;
                lifetime = 16f;
                drawSize = 400f;
                collidesAir = false;
                length = 173f;
                ammoMultiplier = 1f;
                pierceCap = 4;
            }};
        }};

        shore = new PowerTurret("shore") {{
            size = 4;
            requirements(Category.turret, new ItemStack[]{});
            range = 165f;

            shoot.firstShotDelay = 40f;

            recoil = 2f;
            reload = 80f;
            shake = 2f;
            shootEffect = Fx.lancerLaserShoot;
            smokeEffect = Fx.none;
            heatColor = Color.red;
            size = 2;
            scaledHealth = 280;
            targetAir = false;
            moveWhileCharging = false;
            accurateDelay = false;
            shootSound = Sounds.laser;
            coolant = consumeCoolant(0.2f);

            consumePower(6f);

            shootType = new LaserBulletType(140) {{
                colors = new Color[]{Pal.lancerLaser.cpy().a(0.4f), Pal.lancerLaser, Color.white};
                //TODO merge
                chargeEffect = new MultiEffect(Fx.lancerLaserCharge, Fx.lancerLaserChargeBegin);

                buildingDamageMultiplier = 0.25f;
                hitEffect = Fx.hitLancer;
                hitSize = 4;
                lifetime = 16f;
                drawSize = 400f;
                collidesAir = false;
                length = 173f;
                ammoMultiplier = 1f;
                pierceCap = 4;
            }};
        }};
    }

}
