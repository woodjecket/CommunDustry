package cd.ui

import arc.fx.filters.CrtFilter.LineStyle
import arc.graphics.{Color, Colors}
import arc.graphics.g2d.{Draw, Fill, Lines}
import arc.scene.Element
import mindustry.graphics.Drawf

class ElementArrow(val arrowLength: Float) extends Element {
  override def draw(): Unit = {
    Fill.rect(x,y,arrowLength,8f)
    Fill.tri(x + arrowLength / 2 + 4, y, x + arrowLength / 2 - 6, y + 8, x + arrowLength / 2 - 6, y - 8)
  }
}
