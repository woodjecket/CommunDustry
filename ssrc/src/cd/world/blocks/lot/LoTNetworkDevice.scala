package cd.world.blocks.lot

import cd.struct.lot.{ContentStack, LoTNetwork, LoTPocket}
import mindustry.ctype.UnlockableContent

import scala.collection.mutable

trait LoTNetworkDevice {
  var network: LoTNetwork = null
  def obtain(stack: ContentStack):Unit
  
  def require(stack: ContentStack): Boolean
  
  def afford(stack: ContentStack) : Unit
  
  def canAfford(stack: ContentStack): Boolean
  
  
  var filters: mutable.Map[UnlockableContent,Boolean] = mutable.HashMap[UnlockableContent,Boolean]()
  
  def responsePocket(pocket: LoTPocket): Boolean
}
