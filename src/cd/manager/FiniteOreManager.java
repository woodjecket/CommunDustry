package cd.manager;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import cd.world.block.environment.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;

import java.util.*;

/**
 * 几个改进点：
 * 一、接入矿脉生成
 * 二、抽象出MockMiner为之后的兼容做准备
 * */
public class FiniteOreManager{
    private static final Seq<Tile> tmpTiles = new Seq<>();
    private static Seq<Building> worldDrills = new Seq<>();
    private static Seq<Unit> worldMiners = new Seq<>();

    static{

        Events.run(Trigger.update, FiniteOreManager::update);
        Events.on(EventType.TileChangeEvent.class, e -> {
            worldDrills.clear();
            Groups.build.each(FiniteOreManager::isDrill, b -> worldDrills.add(b));
        });
        Events.on(EventType.UnitCreateEvent.class, e -> {
            worldMiners.clear();
            Groups.unit.each(Minerc.class::isInstance, b -> worldMiners.add(b));
        });
        Events.on(EventType.StateChangeEvent.class, e -> {
            Core.app.post(() -> {
                worldDrills.clear();
                worldMiners.clear();
                Groups.build.each(FiniteOreManager::isDrill, b -> worldDrills.add(b));
                Groups.unit.each(Minerc.class::isInstance, b -> worldMiners.add(b));
            });
        });
        Events.on(EventType.SaveLoadEvent.class, e -> {
            Vars.world.tiles.eachTile(t -> {
                if(t.overlay() instanceof FiniteOre){
                    t.extraData = getTileCapacity(t);
                }
            });
        });
        Events.on(EventType.TileChangeEvent.class, e -> {
            if(Vars.world.isGenerating()) return;

            try{
                var oresField = Vars.indexer.getClass().getDeclaredField("ores");
                oresField.setAccessible(true);
                IntSeq[][][] ores = (IntSeq[][][])oresField.get(Vars.indexer);

                var tile = e.tile;
                    for(Item item : Vars.content.items()){
                        if(Vars.indexer.hasOre(item)){
                            var quadrantSize = 20;
                            int qx = tile.x / quadrantSize, qy = tile.y / quadrantSize;
                            if(ores[item.id][qx][qy]!= null && ores[item.id][qx][qy].contains(tile.pos() ) && tile.drop() != item){
                                //add position of quadrant to list
                                ores[item.id][qx][qy].removeValue(tile.pos());
                            }
                        }

                }
            }catch(NoSuchFieldException | IllegalAccessException ex){
                throw new RuntimeException(ex);
            }

        });
    }

    public static boolean isDrill(Building building){
        return building.block instanceof Drill || building.block instanceof BeamDrill;
    }

    public static int getTileCapacity(Tile tile){
        return 100;
    }

    /** Mock whether there will be a mine next frame */
    public static void update(){
        for(Building building : worldDrills){
            if(building instanceof Drill.DrillBuild drill && !Vars.state.isPaused()){
                var progressD = 0f;
                //emulate progress
                if(drill.items.total() < drill.block.itemCapacity && drill.dominantItems > 0 && drill.efficiency > 0){
                    float speed = Mathf.lerp(1f, ((Drill)drill.block).liquidBoostIntensity, drill.optionalEfficiency) * drill.efficiency;
                    var wup = Mathf.approachDelta(drill.warmup, speed, ((Drill)drill.block).warmupSpeed);
                    progressD = drill.delta() * drill.dominantItems * speed * wup;
                }
                if(drill.dominantItems > 0 && drill.progress + progressD >= ((Drill)drill.block).getDrillTime(drill.dominantItem)
                && drill.items.total() < drill.block.itemCapacity){
                    //offload(dominantItem);
                    removeSlack(drill.tile);
                }
            }
            if(building instanceof BeamDrill.BeamDrillBuild beam && !Vars.state.isPaused()){

                float multiplier = Mathf.lerp(1f, ((BeamDrill)beam.block).optionalBoostIntensity, beam.optionalEfficiency);
                float drillTime = ((BeamDrill)beam.block).getDrillTime(beam.lastItem);

                var timeD = beam.edelta() * multiplier;

                if(beam.time + timeD >= drillTime){
                    for(Tile tile : beam.facing){
                        Item drop = tile == null ? null : tile.wallDrop();
                        if(beam.items.total() < beam.block.itemCapacity && drop != null){
                            //items.add(drop, 1);
                            removeSlack(beam.tile);
                        }
                    }
                }
            }
        }
        for(Unit unit : worldMiners){
            if(unit.mineTile != null){
                Building core = unit.closestCore();
                Item item = unit.getMineResult(unit.mineTile);
                if(unit.mining() && item != null){
                    var timerD = Time.delta * unit.type.mineSpeed * Vars.state.rules.unitMineSpeed(unit.team());

                    if(unit.mineTimer + timerD >= 50.0F + (unit.type.mineHardnessScaling ? item.hardness * 15.0F : 15.0F)){

                        if(core != null && unit.within(core, 220.0F) && core.acceptStack(item, 1, unit) == 1 && unit.offloadImmediately()){
                            if(unit.item() == item && !Vars.net.client()){
                                //unit.addItem(item);
                                removeSlack(unit.mineTile);
                            }
                        }else if(unit.acceptsItem(item)){
                            //InputHandler.transferItemToUnit(item, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), this);
                            removeSlack(unit.mineTile);
                        }
                    }
                }
            }
        }
    }

    private static void removeSlack(Tile drill){
        if(drill.block() != null || drill.block() != Blocks.air){
            drill.getLinkedTilesAs(drill.block(), tmpTiles);
        }else{
            tmpTiles.clear();
            tmpTiles.add(drill);
        }
        tmpTiles.removeAll(t -> !(t.overlay() instanceof FiniteOre));
        tmpTiles.sort((Comparator.comparingInt(a -> a.extraData)));
        Tile firstOpt = tmpTiles.firstOpt();
        if(firstOpt != null){
            firstOpt.extraData -= 1;
            if((((FiniteOre)firstOpt.overlay()).shouldRecache(firstOpt))){
                firstOpt.recache();
            }
            if(firstOpt.extraData <= -1){
                firstOpt.setOverlay(((FiniteOre)firstOpt.overlay()).exhauseted);
                Events.fire(new TileChangeEvent().set(firstOpt));
            }
            //Vars.ui.showLabel(String.valueOf(firstOpt.extraData), 1, firstOpt.drawx(), firstOpt.drawy());
        }
    }
}
