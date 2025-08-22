package cd.manager;

import arc.*;
import arc.math.*;
import arc.struct.*;
import cd.world.block.environment.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.production.Drill.*;

import java.util.*;

public class FiniteOreManager{
private static final Seq<Tile> tmpTiles = new Seq<>();
    private static Seq<Building> worldDrills = new Seq<>();

    static{
        Events.run(Trigger.update, FiniteOreManager::update);
        Events.on(EventType.TileChangeEvent.class, e -> {
            worldDrills.clear();
            Groups.build.each(DrillBuild.class::isInstance, b -> worldDrills.add(b));
        });
        Events.on(EventType.StateChangeEvent.class, e -> {
            Core.app.post(() -> {
                worldDrills.clear();
                Groups.build.each(DrillBuild.class::isInstance, b -> worldDrills.add(b));
            });
        });
    }

    public static int getTileCapacity(Tile tile){
        return 10;
    }

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
                    removeSlack(drill);
                }
            }
        }
    }

    private static void removeSlack(DrillBuild drill){
        drill.tile.getLinkedTilesAs(drill.block,tmpTiles);
        tmpTiles.removeAll(t->!(t.overlay() instanceof FiniteOre));
        tmpTiles.sort((Comparator.comparingInt(a -> -a.extraData)));
        Tile firstOpt = tmpTiles.firstOpt();
        if(firstOpt != null){
            if(firstOpt.extraData == 0){
                firstOpt.extraData = getTileCapacity(firstOpt);
            }
            firstOpt.extraData -= 1;
            if(firstOpt.extraData <= 0){
                firstOpt.setOverlay(((FiniteOre)firstOpt.overlay()).exhauseted);
            }
            Vars.ui.showLabel(String.valueOf(firstOpt.extraData),1,firstOpt.drawx(),firstOpt.drawy());
        }
    }
}
