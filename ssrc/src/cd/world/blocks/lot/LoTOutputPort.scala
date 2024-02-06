package cd.world.blocks.lot

import arc.scene.ui.layout.Table
import arc.util.ArcRuntimeException
import cd.struct.lot.{ContentStack, LoTChangeFilterPocket, LoTPocket, LoTRequireContentPocket}
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Cons2}
import cd.util.Utils
import cd.world.ContentSelection
import cd.world.blocks.CDBaseBlock
import mindustry.`type`.{Item, ItemStack, Liquid, LiquidStack}
import mindustry.ctype.UnlockableContent
import mindustry.gen.Building

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class LoTOutputPort(name: String) extends CDBaseBlock(name) {
  configurable = true
  config(classOf[Item], (b: LoTOutputPortBuild, i:Item)=>b.requirements += i)
  config(classOf[Liquid], (b: LoTOutputPortBuild, i:Liquid)=>b.requirements += i)
  configClear((tile: LoTOutputPortBuild) => tile.requirements.clear())
  
  class LoTOutputPortBuild extends Building with LoTNetworkDevice {
    var requirements: ArrayBuffer[UnlockableContent] = ArrayBuffer[UnlockableContent]()
   
    var warmup1: Float = 0f;
    
    override def buildConfiguration(table: Table): Unit = {
      ContentSelection.buildTables( table, Utils.itemAndLiquid, () => requirements, this.configure, selectionRows, selectionColumns,block = LoTOutputPort.this)
    }
    override def updateTile(): Unit = {
      super.updateTile()
      if(network != null && warmup1 < 1f) {
        requirements.foreach {
          case item: Item => if(items.get(item) < block.itemCapacity) {
            network.postPocket(new LoTChangeFilterPocket(mutable.Map(item-> true)))
            network.postPocket(
              new LoTRequireContentPocket(ContentStack(item, (block.itemCapacity - items.get(item)).toFloat)))
          }
          case liquid: Liquid => if(liquids.get(liquid) < block.liquidCapacity) {
            new LoTChangeFilterPocket(mutable.Map(liquid -> true))
            network.postPocket(
              new LoTRequireContentPocket(ContentStack(liquid, block.liquidCapacity - liquids.get(liquid))))
          }
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
