package cd.world.component


import cd.struct.Recipe
import cd.world.component.CDBaseComp.{CDBlockComp,CDBuildingComp}

trait CDBaseCrafter extends CDBlockComp {
  var recipe: Recipe = null

  trait CDBaseCrafterBuild extends CDBuildingComp{

  }
}
