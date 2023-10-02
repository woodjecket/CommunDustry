package cd.util

import mindustry.`type`.{ItemStack, LiquidStack}

object Utils {
  def iwith[T](xs: T*): Array[ItemStack] =
    ItemStack.`with`(xs.asInstanceOf[Array[AnyRef]])

  def lwith[T](xs: T*): Array[LiquidStack] =
    LiquidStack.`with`(xs.asInstanceOf[Array[AnyRef]])

}
