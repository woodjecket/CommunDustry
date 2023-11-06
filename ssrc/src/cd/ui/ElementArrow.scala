package cd.ui

import arc.graphics.Color
import arc.graphics.g2d.{Fill, Lines}
import arc.scene.Element

class ElementArrow(val arrowLength: Float) extends Element {
  width = arrowLength
  height = 32
  sizeChanged()
  override def draw(): Unit = {
    Lines.stroke(16, Color.white)
    Lines.line(x - arrowLength / 2, y, x + arrowLength / 2, y)
    Fill.tri(x + arrowLength / 2 + 4, y,
      x - arrowLength / 2 - 4, y + 16,
      x - arrowLength / 2 - 4, y - 16)
  }
}
