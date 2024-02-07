package cd.util

import arc.{Events, struct}
import cd.util.SAMConversation.lamdba2Cons
import mindustry.Vars
import mindustry.`type`.LiquidStack
import mindustry.ctype.UnlockableContent
import mindustry.game.EventType

object Utils {
  private var tileChanged = 0
  Events.on(classOf[EventType.WorldLoadEvent], (_:EventType.WorldLoadEvent) => {
    tileChanged = -1
  })
  def lwith[T](xs: T*): Array[LiquidStack] =
    LiquidStack.`with`(xs.asInstanceOf[Array[AnyRef]])

  val itemAndLiquid : Array[UnlockableContent] = {
    val seq = new struct.Seq[UnlockableContent]()
    seq.add(Vars.content.items).add(Vars.content.liquids);
    seq.toArray(classOf[UnlockableContent])
  }
  

}
