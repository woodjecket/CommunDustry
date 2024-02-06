package cd.util

import arc.struct
import mindustry.Vars
import mindustry.`type`.{ItemStack, LiquidStack}
import mindustry.ctype.UnlockableContent

object Utils {
  def iwith[T](xs: T*): Array[ItemStack] =
    ItemStack.`with`(xs.asInstanceOf[Array[AnyRef]])

  def lwith[T](xs: T*): Array[LiquidStack] =
    LiquidStack.`with`(xs.asInstanceOf[Array[AnyRef]])

  val itemAndLiquid : Array[UnlockableContent] = {
    val seq = new struct.Seq[UnlockableContent]()
    seq.add(Vars.content.items).add(Vars.content.liquids);
    seq.toArray(classOf[UnlockableContent])
  }
}
