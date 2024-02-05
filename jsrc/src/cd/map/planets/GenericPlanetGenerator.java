package cd.map.planets;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import cd.content.*;
import mindustry.ai.*;
import mindustry.ai.BaseRegistry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class GenericPlanetGenerator extends PlanetGenerator{
    public static Rand staticRand = new Rand();
    public BaseGenerator basegen = new BaseGenerator();
    private SpawnRoom spawn;
    private float positionMultiplier = 10f;
    private Seq<SpawnRoom> enemies = new Seq<>();

    @Override
    public float getHeight(Vec3 position){
        return noise3dvm(position, positionMultiplier, 3) / 5f;
    }

    @Override
    public Color getColor(Vec3 position){
        Block block1 = getBlock1(position);
        //replace salt with sand color
        if(block1 == Blocks.salt) return Blocks.sand.mapColor;
        //I AM ALMOST BLIND
        if(block1 == Blocks.ice) return Tmp.c1.set(block1.mapColor);
        return Tmp.c1.set(block1.mapColor).a(1f - block1.albedo);
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        tile.floor = getBlock1(position);
        tile.block = tile.floor.asFloor().wall;
        if(Ridged.noise3d(seed + 1, position.x, position.y, position.z, 2, 22) > 0.31){
            tile.block = Blocks.air;
        }
    }

    @Override
    public void generate(Tiles tiles, Sector sec, int seed){
        this.tiles = tiles;
        this.seed = seed + baseSeed;
        sector = sec;
        width = tiles.width;
        height = tiles.height;
        staticRand.setSeed(sec.id + seed + baseSeed);
        TileGen gen = new TileGen();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                gen.reset();
                //Commundustry hacked here
                //Make the position on the sphere to generate river
                Vec3 position = sector.rect.project(x / (float)tiles.width, y / (float)tiles.height).setLength(1f);
                genTile(position, gen);
                tiles.set(x, y, new Tile(x, y, gen.floor, gen.overlay, gen.overlay));
            }
        }

        generate(tiles);
    }

    @Override
    public void generate(){
        cells(4);
        distort(10f, 12f);
        clearBasedOnRoom();
        cells(1);
        distort(10f, 6f);
    }

    //TOOD Rewrite
    private void genOres(){
        Seq<Block> ores = Seq.with(Blocks.oreCopper, Blocks.oreLead);
        float poles = Math.abs(sector.tile.v.y);
        float nmag = 0.5f;
        float scl1 = 1f;
        float addscl = 1.3f;

        if(Simplex.noise3d(seed, 2, 0.5, scl1, sector.tile.v.x, sector.tile.v.y, sector.tile.v.z) * nmag + poles > 0.25f * addscl){
            ores.add(Blocks.oreCoal);
        }

        if(Simplex.noise3d(seed, 2, 0.5, scl1, sector.tile.v.x + 1, sector.tile.v.y, sector.tile.v.z) * nmag + poles > 0.5f * addscl){
            ores.add(Blocks.oreTitanium);
        }

        if(Simplex.noise3d(seed, 2, 0.5, scl1, sector.tile.v.x + 2, sector.tile.v.y, sector.tile.v.z) * nmag + poles > 0.7f * addscl){
            ores.add(Blocks.oreThorium);
        }

        if(staticRand.chance(0.25)){
            ores.add(Blocks.oreScrap);
        }

        FloatSeq frequencies = new FloatSeq();
        for(int i = 0; i < ores.size; i++){
            frequencies.add(staticRand.random(-0.1f, 0.01f) - i * 0.01f + poles * 0.04f);
        }

        pass((x, y) -> {
            if(!floor.asFloor().hasSurface()) return;

            int offsetX = x - 4, offsetY = y + 23;
            for(int i = ores.size - 1; i >= 0; i--){
                Block entry = ores.get(i);
                float freq = frequencies.get(i);
                if(Math.abs(0.5f - noise(offsetX, offsetY + i * 999, 2, 0.7, (40 + (i << 1)))) > 0.22f + i * 0.01 &&
                Math.abs(0.5f - noise(offsetX, offsetY - i * 999, 1, 1, (30 + (i << 2)))) > 0.37f + freq){
                    ore = entry;
                    break;
                }
            }

            if(ore == Blocks.oreScrap && staticRand.chance(0.33)){
                floor = Blocks.metalFloorDamaged;
            }
        });
    }

    private void clearBasedOnRoom(){
        float constraint = 1.3f;
        float radius = width / 2f / Mathf.sqrt3;
        int rooms = staticRand.random(2, 5);
        Seq<SpawnRoom> roomseq = new Seq<>();

        for(int i = 0; i < rooms; i++){
            Tmp.v1.trns(staticRand.random(360f), staticRand.random(radius / constraint));
            float rx = (width / 2f + Tmp.v1.x);
            float ry = (height / 2f + Tmp.v1.y);
            float maxrad = radius - Tmp.v1.len();
            float rrad = Math.min(staticRand.random(9f, maxrad / 4f), 15f);
            roomseq.add(new SpawnRoom((int)rx, (int)ry, (int)rrad));
        }

        //check positions on the map to place the player spawn. this needs to be in the corner of the map
        //Enemy base up to threat*4
        int enemySpawns = staticRand.random(1, Math.max((int)(sector.threat * 4), 1));
        int offset = staticRand.nextInt(360);
        float length = width / 2.55f - staticRand.random(13, 23);
        int angleStep = 5;
        int riverLiquidCheckRad = 5;
        for(int i = 0; i < 360; i += angleStep){
            int angle = offset + i;
            int cx = (int)(width / 2 + Angles.trnsx(angle, length));
            int cy = (int)(height / 2 + Angles.trnsy(angle, length));

            int riverLiquidTiles = 0;

            //check for riverLiquid presence
            for(int rx = -riverLiquidCheckRad; rx <= riverLiquidCheckRad; rx++){
                for(int ry = -riverLiquidCheckRad; ry <= riverLiquidCheckRad; ry++){
                    Tile tile = tiles.get(cx + rx, cy + ry);
                    if(tile == null || tile.floor().liquidDrop != null){
                        riverLiquidTiles++;
                    }
                }
            }

            if(riverLiquidTiles <= 4 || (i + angleStep >= 360)){
                roomseq.add(spawn = new SpawnRoom(cx, cy, staticRand.random(8, 15)));

                for(int j = 0; j < enemySpawns; j++){
                    float enemyOffset = staticRand.range(60f);
                    Tmp.v1.set(cx - width / 2, cy - height / 2).rotate(180f + enemyOffset).add(width / 2, height / 2);
                    SpawnRoom espawn = new SpawnRoom((int)Tmp.v1.x, (int)Tmp.v1.y, staticRand.random(8, 16));
                    roomseq.add(espawn);
                    enemies.add(espawn);
                }

                break;
            }
        }

        //clear radius around each room
        for(SpawnRoom spawnRoom : roomseq){
            erase(spawnRoom.x, spawnRoom.y, spawnRoom.radius);
        }

        //randomly connect rooms together
        int connections = staticRand.random(Math.max(rooms - 1, 1), rooms + 3);
        for(int i = 0; i < connections; i++){
            roomseq.random(staticRand).connect(roomseq.random(staticRand));
        }

        for(SpawnRoom spawnRoom : roomseq){
            spawn.connect(spawnRoom);
        }
    }

    private float noise3dvm(Vec3 vec3, float multiplier, int octaves){
        return Ridged.noise3d(seed, vec3.x * multiplier, vec3.y * multiplier, vec3.z * multiplier, octaves, 1f);
    }

    private float noise3sdvm(int seed, Vec3 vec3, float multiplier, int octaves){
        return Ridged.noise3d(seed, vec3.x * multiplier, vec3.y * multiplier, vec3.z * multiplier, octaves, 1f);
    }

    private void genEnemy(Seq<SpawnRoom> enemies, boolean naval){
        float difficulty = sector.threat;
        ints.clear();
        ints.ensureCapacity(width * height / 4);

        int ruinCount = staticRand.random(-2, 4);
        if(ruinCount > 0){
            int padding = 25;

            //create list of potential positions
            for(int x = padding; x < width - padding; x++){
                for(int y = padding; y < height - padding; y++){
                    Tile tile = tiles.getn(x, y);
                    if(!tile.solid() && (tile.drop() != null || tile.floor().liquidDrop != null)){
                        ints.add(tile.pos());
                    }
                }
            }

            ints.shuffle(staticRand);

            int placed = 0;
            float diffRange = 0.4f;
            //try each position
            for(int i = 0; i < ints.size && placed < ruinCount; i++){
                int val = ints.items[i];
                int x = Point2.x(val), y = Point2.y(val);

                //do not overwrite player spawn
                if(Mathf.within(x, y, spawn.x, spawn.y, 18f)){
                    continue;
                }

                float range = difficulty + staticRand.random(diffRange);

                Tile tile = tiles.getn(x, y);
                BasePart part = null;
                if(tile.overlay().itemDrop != null){
                    part = bases.forResource(tile.drop()).getFrac(range);
                }else if(tile.floor().liquidDrop != null && staticRand.chance(0.05)){
                    part = bases.forResource(tile.floor().liquidDrop).getFrac(range);
                }else if(staticRand.chance(0.05)){ //ore-less parts are less likely to occur.
                    part = bases.parts.getFrac(range);
                }

                //actually place the part
                if(part != null && BaseGenerator.tryPlace(part, x, y, Team.derelict, (cx, cy) -> {
                    Tile other = tiles.getn(cx, cy);
                    if(other.floor().hasSurface()){
                        other.setOverlay(Blocks.oreScrap);
                        for(int j = 1; j <= 2; j++){
                            for(Point2 p : Geometry.d8){
                                Tile t = tiles.get(cx + p.x * j, cy + p.y * j);
                                if(t != null && t.floor().hasSurface() && staticRand.chance(j == 1 ? 0.4 : 0.2)){
                                    t.setOverlay(Blocks.oreScrap);
                                }
                            }
                        }
                    }
                })){
                    placed++;

                    int debrisRadius = Math.max(part.schematic.width, part.schematic.height) / 2 + 3;
                    Geometry.circle(x, y, tiles.width, tiles.height, debrisRadius, (cx, cy) -> {
                        float dst = Mathf.dst(cx, cy, x, y);
                        float removeChance = Mathf.lerp(0.05f, 0.5f, dst / debrisRadius);

                        Tile other = tiles.getn(cx, cy);
                        if(other.build != null && other.isCenter()){
                            if(other.team() == Team.derelict && staticRand.chance(removeChance)){
                                other.remove();
                            }else if(staticRand.chance(0.5)){
                                other.build.health = other.build.health - staticRand.random(other.build.health * 0.9f);
                            }
                        }
                    });
                }
            }
        }

        //remove invalid ores
        for(Tile tile : tiles){
            if(tile.overlay().needsSurface && !tile.floor().hasSurface()){
                tile.setOverlay(Blocks.air);
            }
        }

        Schematics.placeLaunchLoadout(spawn.x, spawn.y);

        for(SpawnRoom espawn : enemies){
            tiles.getn(espawn.x, espawn.y).setOverlay(Blocks.spawn);
        }

        if(sector.hasEnemyBase()){
            basegen.generate(tiles, enemies.map(r -> tiles.getn(r.x, r.y)), tiles.get(spawn.x, spawn.y), state.rules.waveTeam, sector, difficulty);

            state.rules.attackMode = sector.info.attack = true;
        }else{
            state.rules.winWave = sector.info.winWave = 10 + 5 * (int)Math.max(difficulty * 10, 1);
        }

        float waveTimeDec = 0.4f;

        state.rules.waveSpacing = Mathf.lerp(60 * 65 * 2, 60f * 60f * 1f, Math.max(difficulty - waveTimeDec, 0f));
        state.rules.waves = true;
        state.rules.env = sector.planet.defaultEnv;
        state.rules.enemyCoreBuildRadius = 600f;

        //spawn air only when spawn is blocked
        state.rules.spawns = Waves.generate(difficulty, new Rand(sector.id), state.rules.attackMode,
        state.rules.attackMode && spawner.countGroundSpawns() == 0, naval);
    }

    private Block getBlock1(Vec3 position){
        if(getRiver(position) != null){
            return getRiver(position);
        }
        var rheight = noise3dvm(position, positionMultiplier, 7);
        if(rheight > 0.4f){
            return Blocks.ice;
        }else if(rheight > 0f){
            return CDBlocks.enrichedSandFloor;
        }else{
            return CDBlocks.graniteFloor;
        }
    }

    private float getPositionMultiplier(){
        return positionMultiplier;
    }

    private Block getRiver(Vec3 position){
        var rheight1 = noise3dvm(position, positionMultiplier, 3);
        var rheight2 = noise3dvm(position, positionMultiplier, 5);
        var rheight = rheight1 * rheight2;
        if(rheight < 0f){
            return Blocks.water;
        }else{
            return null;
        }
    }

    //see it just a black box
    public class SpawnRoom{
        public int x, y, radius;
        public ObjectSet<SpawnRoom> connected = new ObjectSet<>();
        private int strokeMin = 4;
        private int strokeMax = 10;
        private float nscaleMin = 700;
        private float nscaleMax = 900;

        SpawnRoom(int x, int y, int radius){
            this.x = x;
            this.y = y;
            this.radius = radius;
            //connect itself?
            connected.add(this);
        }

        void connect(SpawnRoom to){
            // if it has already been added or it is just itself then return
            if(!connected.add(to) || to == this) return;

            Vec2 midpoint = Tmp.v1.set(to.x, to.y).add(x, y).scl(0.5f);
            staticRand.nextFloat();

            //add randomized offset to avoid straight lines
            midpoint.add(Tmp.v2.setToRandomDirection(staticRand).scl(Tmp.v1.dst(x, y)));

            midpoint.sub(width / 2f, height / 2f).limit(width / 2f / Mathf.sqrt3).add(width / 2f, height / 2f);

            int mx = (int)midpoint.x, my = (int)midpoint.y;

            join(x, y, mx, my);
            join(mx, my, to.x, to.y);
        }

        void join(int x1, int y1, int x2, int y2){
            float nscl = staticRand.random(nscaleMin, nscaleMax); //(600.0,840.0)
            int stroke = staticRand.random(strokeMin, strokeMax);
            brush(
            pathfind(x1, y1, x2, y2, tile -> (tile.solid() ? 50f : 0f) + noise(tile.x, tile.y, 2, 0.4f, 1f / nscl) * 500, Astar.manhattan), stroke);
        }

        void connectLiquid(SpawnRoom to){
            if(to == this) return;

            Vec2 midpoint = Tmp.v1.set(to.x, to.y).add(x, y).scl(0.5f);
            staticRand.nextFloat();

            //add randomized offset to avoid straight lines
            midpoint.add(Tmp.v2.setToRandomDirection(staticRand).scl(Tmp.v1.dst(x, y)));
            midpoint.sub(width / 2f, height / 2f).limit(width / 2f / Mathf.sqrt3).add(width / 2f, height / 2f);

            int mx = (int)midpoint.x, my = (int)midpoint.y;

            joinLiquid(x, y, mx, my);
            joinLiquid(mx, my, to.x, to.y);
        }

        void joinLiquid(int x1, int y1, int x2, int y2){
            float nscl = staticRand.random(nscaleMin, nscaleMax);
            int rad = staticRand.random(strokeMin << 1, strokeMax << 1);
            int avoid = 2 + rad;
            var path = pathfind(x1, y1, x2, y2, tile -> (tile.solid() || !tile.floor().isLiquid ? 70f : 0f)
            + noise(tile.x, tile.y, 2, 0.4f, 1f / nscl) * 500, Astar.manhattan);
            path.each(t -> {
                //don't place liquid paths near the core
                if(Mathf.dst2(t.x, t.y, x2, y2) <= avoid * avoid){
                    return;
                }

                for(int x3 = -rad; x3 <= rad; x3++){
                    for(int y3 = -rad; y3 <= rad; y3++){
                        int wx = t.x + x, wy = t.y + y1;
                        if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x3, y3, rad)){
                            Tile other = tiles.getn(wx, wy);
                            other.setBlock(Blocks.air);
                            if(Mathf.within(x3, y3, rad - 1) && !other.floor().isLiquid){
                                Floor floor1 = other.floor();
                                //TODO does not respect tainted floors
                                other.setFloor((Floor)(floor1 == Blocks.sand ||
                                floor1 == Blocks.salt ? Blocks.sandWater : Blocks.darksandTaintedWater));
                            }
                        }
                    }
                }
            });
        }
    }
}
