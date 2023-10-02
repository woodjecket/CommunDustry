package cd.ui

import arc.scene.ui.layout.Table
import mindustry.gen.Building

trait Tablable {

  def buildTable(build: Building, table: Table): Table
}
