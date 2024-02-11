package cd.struct

import arc.Events
import mindustry.Vars
import cd.util.SAMConversation.lamdba2Runnable
import mindustry.game.EventType.Trigger

trait TileChange {
  
  var tileChanged = -1
  
  protected def tileChange: Boolean = if(tileChanged != Vars.world.tileChanges) {tileChanged = Vars.world.tileChanges; true } else false
  
  protected def tileChange(program: => Unit): Unit = {
    if(tileChange) program
  }
  
  protected def onUpdate(program: => Unit): Unit = {
    Events.run(Trigger.update, () => {program })
  }
}
