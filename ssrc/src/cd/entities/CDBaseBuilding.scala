package cd.entities

import mindustry.Vars
import mindustry.gen.Building

trait CDBaseBuilding extends Building {
  var isRemoved: Boolean = false
  
  private var tileChanged = -1
  protected def tileChange: Boolean = if(tileChanged != Vars.world.tileChanges) {tileChanged = Vars.world.tileChanges; true } else false
  
  protected def tileChange(program: =>Unit): Unit = {
    if(tileChange) program
  }
  
}


