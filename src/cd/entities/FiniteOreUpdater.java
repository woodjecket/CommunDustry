package cd.entities;

import arc.Events;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import cd.io.FiniteOreCustomChunk;
import cd.world.block.environment.Finite;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.Drill;
import mindustry.world.meta.BlockFlag;

public class FiniteOreUpdater {
    public static final int DEFAULT_ORE_AMOUNT = 100;
    @SuppressWarnings("unused")
    private final FiniteOreCustomChunk focc = new FiniteOreCustomChunk();

    private final Seq<Building> drills = new Seq<>();
    private final Seq<Tile> tmpTileSeq = new Seq<>(36);
    ;

    public void update() {
        if (!Vars.state.isPaused())
            for (Building building : drills) {
                if (building instanceof Drill.DrillBuild drill) {
                    if (drill.dominantItems > 0 && drill.progress + emulateDrillProgress(drill) >= ((Drill) drill.block).getDrillTime(drill.dominantItem) && drill.items.total() < drill.block.itemCapacity) {
                        consumeGroundOnce(drill);
                    }
                }
            }
    }

    //emulate progress
    private static float emulateDrillProgress(Drill.DrillBuild drill) {
        if (drill.items.total() < drill.block.itemCapacity && drill.dominantItems > 0 && drill.efficiency > 0) {
            float speed = Mathf.lerp(1f, ((Drill) drill.block).liquidBoostIntensity, drill.optionalEfficiency) * drill.efficiency;
            var wup = Mathf.approachDelta(drill.warmup, speed, ((Drill) drill.block).warmupSpeed);
            return drill.delta() * drill.dominantItems * speed * wup;
        }
        return 0f;
    }

    private void consumeGroundOnce(Drill.DrillBuild drill) {
        tmpTileSeq.clear();
        drill.tile.getLinkedTilesAs(drill.block, tmpTileSeq);
        tmpTileSeq.retainAll(t -> t.drop() == drill.dominantItem);
        if (tmpTileSeq.contains(this::isInfiniteFloor)) return;
        tmpTileSeq.sort((t1, t2) -> FiniteOreCustomChunk.amountMap.get(t2, DEFAULT_ORE_AMOUNT) - FiniteOreCustomChunk.amountMap.get(t1,DEFAULT_ORE_AMOUNT));
        Log.infoList(tmpTileSeq, FiniteOreCustomChunk.amountMap);
        var first = tmpTileSeq.first();
        FiniteOreCustomChunk.amountMap.increment(first, DEFAULT_ORE_AMOUNT, -1);
        if (FiniteOreCustomChunk.amountMap.get(first) < 1) {
            exhaust(first);
        }
    }

    private void exhaust(Tile t) {
        t.setOverlay(((Finite) t.overlay()).exhausted());
    }

    private boolean isInfiniteFloor(Tile tile) {
        return !(dropFloor(tile) instanceof Finite);
    }

    private Floor dropFloor(Tile t) {
        return t.overlay() == Blocks.air || t.overlay().itemDrop == null ? t.floor() : t.overlay();
    }
    public void worldChange() {
        drills.clear();
        for (Team team : Team.all) {
            drills.add(Vars.indexer.getFlagged(team, BlockFlag.drill));

        }
    }

    public void init() {
        Events.run(EventType.Trigger.update, this::update);
        Events.run(EventType.BlockBuildEndEvent.class, this::worldChange);
    }

}
