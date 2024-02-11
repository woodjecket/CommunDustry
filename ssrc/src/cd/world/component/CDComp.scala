package cd.world.component

import cd.content.Applyable
import cd.entities.CDBaseBuilding
import mindustry.gen.Building
import mindustry.world.Block

class CDComp {
}

object CDComp extends Applyable {
  trait CDBlockComp extends Block {}
  
  trait CDBuildingComp extends Building with CDBaseBuilding{
    override def updateTile(): Unit = {
      super.updateTile()
    }
  }
  
}
