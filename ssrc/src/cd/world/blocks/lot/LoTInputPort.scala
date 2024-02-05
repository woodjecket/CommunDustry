package cd.world.blocks.lot

import arc.util.ArcRuntimeException
import cd.struct.lot.{ContentStack, LoTChangeFilterPocket, LoTPocket, LoTRequireContentPocket}
import cd.world.blocks.CDBaseBlock
import mindustry.`type`.{Item, ItemStack, Liquid, LiquidStack}
import mindustry.gen.{Building, Teamc}

class LoTInputPort(name: String) extends CDBaseBlock(name) {
  
  
  override def init(): Unit = {
    for(i <- itemFilter.indices) itemFilter(i) = true
    for(i <- liquidFilter.indices) liquidFilter(i) = true
    super.init()
  }
  
  class LotInputPortBuild extends Building with LoTNetworkDevice {
    override def updateTile(): Unit = {
      super.updateTile()
    }
    
    override def obtain(stack: ContentStack): Unit = {}
    
    override def require(stack: ContentStack): Boolean = false
    
    override def afford(stack: ContentStack): Unit = {
      stack.get() match {
        case itemStack: ItemStack => items.remove(itemStack.item, itemStack.amount)
        case liquidStack: LiquidStack => liquids.remove(liquidStack.liquid, liquidStack.amount)
        case _ => throw new ArcRuntimeException("No")
      }
      network.postPocket(LoTRequireContentPocket(stack))
    }
    
    override def canAfford(stack: ContentStack): Boolean = stack.get() match {
      case itemStack: ItemStack => items.has(itemStack.item, itemStack.amount)
      case liquidStack: LiquidStack => liquids.get(liquidStack.liquid) >= liquidStack.amount
      case _ => throw new ArcRuntimeException("No")
    }
    
    
    override def responsePocket(pocket: LoTPocket): Boolean = pocket match {
      case _: LoTRequireContentPocket => true
      case _: LoTChangeFilterPocket => true
      case _ => false
    }
    
    override def acceptItem(source: Building, item: Item): Boolean =
      this.items.get(item) < this.getMaximumAccepted(item) && filters(item)
    
    override def acceptLiquid(source: Building, liquid: Liquid): Boolean = filters(liquid)
    
    override def acceptStack(item: Item, amount: Int, source: Teamc): Int =
      if(!this.acceptItem(this, item) || !this.block.hasItems || source != null && (source.team ne this.team)) 0
      else Math.min(this.getMaximumAccepted(item) - this.items.get(item), amount)
    
  }
}
