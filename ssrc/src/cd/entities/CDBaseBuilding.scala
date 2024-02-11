package cd.entities

import cd.struct.TileChange
import mindustry.gen.Building

trait CDBaseBuilding extends Building with TileChange {
  var isRemoved: Boolean = false
}


