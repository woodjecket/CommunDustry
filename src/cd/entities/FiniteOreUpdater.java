package cd.entities;

import arc.Events;
import arc.math.Mathf;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
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
    private final Runnable _update = this::update;
    @SuppressWarnings("unused")
    private final  FiniteOreCustomChunk focc = new FiniteOreCustomChunk();

    public final ObjectIntMap<Tile> amountMap = new ObjectIntMap<>();

    private final Seq<Building> drills = new Seq<>();
    private final Seq<Tile> tmpTileSeq = new Seq<>(36);

    public void update() {
        for (Building building : drills) {
            if(building instanceof Drill.DrillBuild drill && !Vars.state.isPaused()){
                var progressD = 0f ;
                //emulate progress
                if(drill.items.total() < drill.block.itemCapacity && drill.dominantItems > 0 && drill.efficiency > 0){
                    float speed = Mathf.lerp(1f, ((Drill) drill.block).liquidBoostIntensity, drill.optionalEfficiency) * drill.efficiency;
                    var wup = Mathf.approachDelta(drill.warmup, speed, ((Drill) drill.block).warmupSpeed);
                    progressD = drill.delta() *drill. dominantItems * speed * wup;
                }
                if (drill.dominantItems > 0 && drill.progress + progressD >= ((Drill) drill.block).getDrillTime(drill.dominantItem)
                        && drill.items.total() < drill.block.itemCapacity) {
                    //offload(dominantItem);
                    consumeGroundOnce(drill);
                }
            }
        }
    }

    private void consumeGroundOnce(Drill.DrillBuild drill) {
        tmpTileSeq.clear();
        drill.tile.getLinkedTilesAs(drill.block,tmpTileSeq);
        tmpTileSeq.retainAll(t->t.drop() == drill.dominantItem);
        if(tmpTileSeq.contains(this::isInfiniteFloor)) return;
        tmpTileSeq.sort((t1,t2)->amountMap.get(t2) - amountMap.get(t1));
        var first = tmpTileSeq.first();
        amountMap.increment(first,99,-1);
        if(amountMap.get(first) < 1){
            exhaust(first);
        }
    }

    private void exhaust(Tile t) {
        t.setOverlay(((Finite) t.overlay()).exhausted());
    }

    private boolean isInfiniteFloor(Tile tile) {
        return !(dropFloor(tile) instanceof Finite);
    }

    private Floor dropFloor(Tile t){
        return t.overlay() == Blocks.air || t.overlay().itemDrop == null ? t.floor() : t.overlay();
    }


    public void worldChange() {
        drills.clear();
        for (Team team : Team.all) {
            drills.add(Vars.indexer.getFlagged(team, BlockFlag.drill));

        }
    }

    public void init() {
        Events.run(EventType.Trigger.update, _update);
        Events.run(EventType.BlockBuildEndEvent.class,this::worldChange);
    }

}
