package cd.world.blocks.lot

import cd.struct.lot.{ContentStack, LoTNetwork, LoTPocket}
import cd.world.blocks.CDBaseBlock
import mindustry.gen.Building

class LoTCore(name: String) extends CDBaseBlock(name)  {
  class LoTCoreBuild extends Building with LoTNetworkDevice{
    network = LoTNetwork()
    override def updateTile(): Unit = {
      proximity().toArray.foreach({
        case loTNetworkDevice: LoTNetworkDevice => loTNetworkDevice.network = network
      })
    }
    override def obtain(stack: ContentStack): Unit = {}
  
    override def require(stack: ContentStack): Boolean = false
  
    override def afford(stack: ContentStack): Unit = { }
  
    override def canAfford(stack: ContentStack): Boolean = false
  
    override def responsePocket(pocket: LoTPocket): Boolean = false
  }
}
