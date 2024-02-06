package cd.world.blocks

import mindustry.world.Block
import mindustry.world.draw.{DrawBlock, DrawDefault}


class CDBaseBlock(name: String) extends Block(name){
  update = true
  hasItems = true
  hasLiquids = true
  val drawer: DrawBlock = new DrawDefault()
}
