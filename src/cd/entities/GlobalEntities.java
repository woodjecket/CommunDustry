package cd.entities;

import arc.*;
import arc.struct.*;
import mindustry.*;
import mindustry.game.EventType.*;

public class GlobalEntities{
    static{
        Events.run(Trigger.update,GlobalEntities::update);
    }
    public static Seq<GlobalEntity> entities = new Seq<>();

    public static void update(){
        if(!Vars.state.isPaused()){
            entities.each(GlobalEntity::update);
        }
    }
}
