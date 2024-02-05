package cd.struct.lot

import arc.util.ArcRuntimeException
import mindustry.`type`.{Item, ItemStack, Liquid, LiquidStack}
import mindustry.ctype.UnlockableContent

case class ContentStack private(private val obj: Object) {
  def get() = obj
  
  def getContent(): UnlockableContent = obj match {
    case itemStack: ItemStack => itemStack.item
    case liquidStack: LiquidStack => liquidStack.liquid
    case _ => throw new ArcRuntimeException("No")
  }
  
  def getAmount(): Float = obj match {
    case itemStack: ItemStack => itemStack.amount.toFloat
    case liquidStack: LiquidStack => liquidStack.amount
    case _ => throw new ArcRuntimeException("No")
  }
}

object ContentStack {
  def apply(content: UnlockableContent, amount: Float): ContentStack = content match {
    case item: Item => new ContentStack(new ItemStack(item, amount.toInt))
    case liquid: Liquid => new ContentStack(new LiquidStack(liquid, amount))
    case _ => throw new ArcRuntimeException("No")
  }
}
