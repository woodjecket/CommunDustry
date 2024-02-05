package cd.world.blocks.lot

import arc.util.ArcRuntimeException
import cd.struct.lot.{ContentStack, LoTChangeFilterPocket, LoTPocket, LoTRequireContentPocket}
import cd.world.blocks.CDBaseBlock
import mindustry.`type`.{Item, ItemStack, Liquid, LiquidStack}
import mindustry.ctype.UnlockableContent
import mindustry.gen.Building

import scala.collection.mutable.ArrayBuffer

class LoTOutputPort(name: String) extends CDBaseBlock(name) {
  class LoTOutputPortBuild extends Building with LoTNetworkDevice {
    val requirements: ArrayBuffer[UnlockableContent] = ArrayBuffer[UnlockableContent]()
    
    var warmup1: Float = 0f;
    
    override def updateTile(): Unit = {
      super.updateTile()
      if(network != null && warmup1 < 1f) {
        requirements.foreach {
          case item: Item => if(items.get(item) < block.itemCapacity) network.postPocket(
            new LoTRequireContentPocket(ContentStack(item, (block.itemCapacity - items.get(item)).toFloat)))
          case liquid: Liquid => if(liquids.get(liquid) < block.liquidCapacity) network.postPocket(
            new LoTRequireContentPocket(ContentStack(liquid, block.liquidCapacity - liquids.get(liquid))))
          case _ => throw new ArcRuntimeException("No")
        }
        warmup1 = 1f;
      }
    }
    
    override def obtain(stack: ContentStack): Unit = stack.get() match {
      case itemStack: ItemStack => items.add(itemStack.item, itemStack.amount)
      case liquidStack: LiquidStack => liquids.add(liquidStack.liquid, liquidStack.amount)
      case _ => throw new ArcRuntimeException("No")
    }
    
    override def require(stack: ContentStack): Boolean = requirements.contains(stack.getContent())
    
    override def afford(stack: ContentStack): Unit = {}
    
    override def canAfford(stack: ContentStack): Boolean = false
    
    override def responsePocket(pocket: LoTPocket): Boolean = pocket match {
      case _: LoTRequireContentPocket => true
      case _: LoTChangeFilterPocket => true
      case _ => false
    }
  }
}
