package cd

import arc._
import arc.util._
import cd.content._
import cd.content.test._
import cd.struct.meta.Meta
import mindustry._
import mindustry.mod._

object CDMod {
  def configure(): Unit = {
    ///PlanetDialog.debugSelect = true;
    //Log.level = LogLevel.debug;
    val meta = Vars.mods.getMod(classOf[CDMod]).meta
    meta.name = Core.bundle.get("mod.commumdustry.displayName")
    meta.description = Core.bundle.get("mod.commumdustry.description")
    meta.author = Core.bundle.get("mod.commumdustry.author")
  }
}

class CDMod extends Mod {
  Log.info("Loaded CDMod constructor.".asInstanceOf[AnyRef])
  var test = true

  override def loadContent(): Unit = {
    CDMod.configure()
    Log.info("Start loading content.".asInstanceOf[AnyRef])
    CDItems.load()
    CDLiquids.load()
    CDBlocks.load()
    CDUnitTypes.load()
    CDPlanets.load()
    if (test) {Meta.init()
      TestContent()
    }
  }
}