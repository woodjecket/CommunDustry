package cd.world.blocks

import mindustry.world.Block


trait CDBaseBlock extends Block{
  var useProxyModule: Boolean = false
  
  update = true
  hasItems = true
  hasLiquids = true
}
