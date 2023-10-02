package cd.world.blocks

import mindustry.world.draw.{DrawBlock, DrawDefault}

trait CDDrawerBlock {
  val drawer: DrawBlock = new DrawDefault()

}
