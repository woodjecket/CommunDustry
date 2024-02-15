package cd.entities

import mindustry.`type`.Liquid
import mindustry.gen.Building

trait LiquidFiltered extends CDBaseBuilding{
  override def acceptLiquid(source: Building, liquid: Liquid): Boolean =
    super.acceptLiquid(source, liquid) && block.liquidFilter(liquid.id)
}
