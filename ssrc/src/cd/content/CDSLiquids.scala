package cd.content

import arc.graphics.Color
import mindustry.`type`.Liquid

object CDSLiquids extends Applyable {
  val steam:Liquid = new Liquid("steam",Color.white){{
    gas = true;
    barColor = Color.valueOf("EEEEEE")
  }}
}
