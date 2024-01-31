package cd.content;

import arc.graphics.*;
import cd.map.planets.*;
import mindustry.content.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.world.meta.*;

public class CDPlanets{
    public static Planet meadtear;

    public static void load(){
        meadtear = new Planet("meadtear-test-2.2.1-PRE-ALPHA", Planets.sun, 1f, 3){{
            generator = new GenericPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
            new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("997b66").a(0.75f), 2, 0.42f, 1f, 0.43f),
            new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("6f6f91").a(0.5f), 2, 0.42f, 1.5f, 0.49f)
            );
            // for test
            alwaysUnlocked = true;
            landCloudColor = Color.valueOf("555555");
            defaultEnv = Env.scorching | Env.terrestrial;
            startSector = 7;
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            orbitSpacing = 1f;
            totalRadius += 2.6f;
            lightSrcTo = 0.5f;
            lightDstFrom = 0.2f;
            defaultCore = Blocks.coreBastion;
            iconColor = Color.valueOf("555555");
            allowWaves = true;
            allowWaveSimulation = true;
            allowSectorInvasion = true;
            allowLaunchSchematics = true;
            enemyCoreSpawnReplace = true;
            allowLaunchLoadout = true;
            //doesn't play well with configs
            prebuildBase = false;

        }};

    }
}

