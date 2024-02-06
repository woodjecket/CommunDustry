package cd.world.modules

import arc.math.geom.Point2
import arc.util.io.{Reads, Writes}
import arc.util.{ArcRuntimeException, Nullable}
import cd.world.modules.ProxyItemModule.id
import mindustry.Vars
import mindustry.`type`.{Item, ItemSeq, ItemStack}
import mindustry.gen.Building
import mindustry.world.modules.ItemModule

class ProxyItemModule extends ItemModule {
  var main: Building = null
  lazy val mainItem: ItemModule = main.items
  
  override def write(write: Writes): Unit = {
    //Write amount = 1
    write.s(1)
    //Write firstItem = copper
    write.s(1)
    //Write fistItemAMount = main's id
    write.i(id(main))
    //If the mod is unloaded, just give the player some copper as PUNISHMENT
  }
  
  override def read(read: Reads, legacy: Boolean): Unit = {
    if(read.s == 1 && read.s == 1) {
      main = Vars.world.build(read.i)
    } else {
      throw new ArcRuntimeException("F**k you")
    }
  }
  
  
  override def toString: String = s"ProxyItemModule: ${Point2.unpack(id(main))}"
  
  override def copy: ItemModule = mainItem.copy()
  
  override def set(other: ItemModule): Unit = mainItem.set(other)
  
  override def updateFlow(): Unit = mainItem.updateFlow()
  
  override def stopFlow(): Unit = mainItem.stopFlow()
  
  override def length: Int = mainItem.length()
  
  override def getFlowRate(item: Item): Float = mainItem.getFlowRate(item)
  
  override def hasFlowItem(item: Item): Boolean = mainItem.hasFlowItem(item)
  
  override def each(cons: ItemModule.ItemConsumer): Unit = mainItem.each(cons)
  
  override def sum(calc: ItemModule.ItemCalculator): Float = mainItem.sum(calc)
  
  override def has(id: Int): Boolean = mainItem.has(id)
  
  override def has(item: Item): Boolean = mainItem.has(item)
  
  override def has(item: Item, amount: Int): Boolean = mainItem.has(item, amount)
  
  override def has(stacks: Array[ItemStack]): Boolean = mainItem.has(stacks)
  
  override def has(items: ItemSeq): Boolean = mainItem.has(items)
  
  override def has(stacks: java.lang.Iterable[ItemStack]): Boolean = mainItem.has(stacks)
  
  override def has(stacks: Array[ItemStack], multiplier: Float): Boolean = mainItem.has(stacks, multiplier)
  
  override def hasOne(stacks: Array[ItemStack]): Boolean = mainItem.hasOne(stacks)
  
  override def empty: Boolean = mainItem.empty()
  
  override def total: Int = mainItem.total()
  
  override def any: Boolean = mainItem.any()
  
  @Nullable override def first: Item = mainItem.first()
  
  @Nullable override def take: Item = mainItem.take()
  
  @Nullable override def takeIndex(takeRotation: Int): Item = mainItem.takeIndex(takeRotation)
  
  override def nextIndex(takeRotation: Int): Int = mainItem.nextIndex(takeRotation)
  
  override def get(id: Int): Int = mainItem.get(id)
  
  override def get(item: Item): Int = mainItem.get(item)
  
  override def set(item: Item, amount: Int): Unit = mainItem.set(item, amount)
  
  override def add(stacks: java.lang.Iterable[ItemStack]): Unit = mainItem.add(stacks)
  
  override def add(stacks: ItemSeq): Unit = mainItem.add(stacks)
  
  override def add(items: ItemModule): Unit = mainItem.add(items)
  
  override def add(item: Item, amount: Int): Unit = mainItem.add(item, amount)
  
  override def handleFlow(item: Item, amount: Int): Unit = mainItem.handleFlow(item, amount)
  
  override def undoFlow(item: Item): Unit = mainItem.undoFlow(item)
  
  override def remove(item: Item, amount: Int): Unit = mainItem.remove(item, amount)
  
  override def remove(stacks: Array[ItemStack]): Unit = mainItem.remove(stacks)
  
  override def remove(stacks: ItemSeq): Unit = mainItem.remove(stacks)
  
  override def remove(stacks: java.lang.Iterable[ItemStack]): Unit = mainItem.remove(stacks)
  
  override def remove(stack: ItemStack): Unit = mainItem.remove(stack)
  
  override def clear(): Unit = mainItem.clear()
}

object ProxyItemModule {
  def id(mainBuild: Building): Int = mainBuild.tile.pos()
}
