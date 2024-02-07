package cd.world.modules

import arc.math.geom.Point2
import arc.util.Nullable
import arc.util.io.{Reads, Writes}
import cd.entities.CDBaseBuilding
import cd.util.SLog
import mindustry.Vars
import mindustry.`type`.{Item, ItemSeq, ItemStack}
import mindustry.gen.Building
import mindustry.world.modules.ItemModule

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class ProxyGroup private (val id:Int){
  SLog.info(s"$this group is created")
  var main:Building = null
  private def this(build:Building)={
    this(build.pos())
    main = build
  }
  
  
  private val working: ArrayBuffer[CDBaseBuilding] = ArrayBuffer[CDBaseBuilding]()
  
  def +(other:CDBaseBuilding):ProxyGroup = {
    if(main == null) main = Vars.world.build(id)
    SLog.info(s"$this group add $other")
    working += other
    if(other.useProxy) {

      val otherGroup = other.items.asInstanceOf[ProxyItemModule].proxyGroup
      SLog.info(s"$this group merge $otherGroup")
      this ++ otherGroup
    } else {
      other.useProxy = true
      other.items(new ProxyItemModule(this))
      this
    }
  }
  
  def ++(other: ProxyGroup): ProxyGroup = {
    if(main == null) main = Vars.world.build(id)
    SLog.info(s"$this group merge $other")
    working ++= other.working
    other.working.foreach(_.items.asInstanceOf[ProxyItemModule].proxyGroup = this)
    this
  }
  
  override def toString: String = f"ProxyGroup:$id"
}

object ProxyGroup {
  val pool = mutable.HashMap[Int,ProxyGroup]()
  def apply(pos: Int): ProxyGroup = pool.getOrElseUpdate(pos, new ProxyGroup(pos))
}
class ProxyItemModule(var proxyGroup: ProxyGroup) extends ItemModule {
  def proxy: ItemModule = proxyGroup.main.items
  
  override def write(write: Writes): Unit = {
    //Write length = 1
    write.s(1)
    //Write firstItem = copper
    write.s(1)
    //Write firstItemAmount = main's id
    write.i(proxyGroup.id)
    //If the mod is unloaded, just give the player some copper as PUNISHMENT
  }
  
  override def read(read: Reads, legacy: Boolean): Unit = {
    //Should be read in `CDBaseBuilding`
    /*
    * short | length
    * short | id
    * int | amount*/
  }
  
  
  override def toString: String = s"ProxyItemModule: ${Point2.unpack(proxyGroup.id)}"
  
  override def copy: ItemModule = proxy.copy()
  
  override def set(other: ItemModule): Unit = proxy.set(other)
  
  override def updateFlow(): Unit = proxy.updateFlow()
  
  override def stopFlow(): Unit = proxy.stopFlow()
  
  override def length: Int = proxy.length()
  
  override def getFlowRate(item: Item): Float = proxy.getFlowRate(item)
  
  override def hasFlowItem(item: Item): Boolean = proxy.hasFlowItem(item)
  
  override def each(cons: ItemModule.ItemConsumer): Unit = proxy.each(cons)
  
  override def sum(calc: ItemModule.ItemCalculator): Float = proxy.sum(calc)
  
  override def has(id: Int): Boolean = proxy.has(id)
  
  override def has(item: Item): Boolean = proxy.has(item)
  
  override def has(item: Item, amount: Int): Boolean = proxy.has(item, amount)
  
  override def has(stacks: Array[ItemStack]): Boolean = proxy.has(stacks)
  
  override def has(items: ItemSeq): Boolean = proxy.has(items)
  
  override def has(stacks: java.lang.Iterable[ItemStack]): Boolean = proxy.has(stacks)
  
  override def has(stacks: Array[ItemStack], multiplier: Float): Boolean = proxy.has(stacks, multiplier)
  
  override def hasOne(stacks: Array[ItemStack]): Boolean = proxy.hasOne(stacks)
  
  override def empty: Boolean = proxy.empty()
  
  override def total: Int = proxy.total()
  
  override def any: Boolean = proxy.any()
  
  @Nullable override def first: Item = proxy.first()
  
  @Nullable override def take: Item = proxy.take()
  
  @Nullable override def takeIndex(takeRotation: Int): Item = proxy.takeIndex(takeRotation)
  
  override def nextIndex(takeRotation: Int): Int = proxy.nextIndex(takeRotation)
  
  override def get(id: Int): Int = proxy.get(id)
  
  override def get(item: Item): Int = proxy.get(item)
  
  override def set(item: Item, amount: Int): Unit = proxy.set(item, amount)
  
  override def add(stacks: java.lang.Iterable[ItemStack]): Unit = proxy.add(stacks)
  
  override def add(stacks: ItemSeq): Unit = proxy.add(stacks)
  
  override def add(items: ItemModule): Unit = proxy.add(items)
  
  override def add(item: Item, amount: Int): Unit = proxy.add(item, amount)
  
  override def handleFlow(item: Item, amount: Int): Unit = proxy.handleFlow(item, amount)
  
  override def undoFlow(item: Item): Unit = proxy.undoFlow(item)
  
  override def remove(item: Item, amount: Int): Unit = proxy.remove(item, amount)
  
  override def remove(stacks: Array[ItemStack]): Unit = proxy.remove(stacks)
  
  override def remove(stacks: ItemSeq): Unit = proxy.remove(stacks)
  
  override def remove(stacks: java.lang.Iterable[ItemStack]): Unit = proxy.remove(stacks)
  
  override def remove(stack: ItemStack): Unit = proxy.remove(stack)
  
  override def clear(): Unit = proxy.clear()
}

object ProxyItemModule {
  def apply(proxyGroup: ProxyGroup): ProxyItemModule = new ProxyItemModule(proxyGroup)
  
  
  def id(mainBuild: Building): Int = mainBuild.tile.pos()
}
