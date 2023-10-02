package cd.world.component

import arc.Core
import arc.audio.Sound
import arc.math.geom.Geometry
import cd.content.CDFx
import cd.util.SAMConversation.{lamdba2Boolf, lamdba2Floatp, lamdba2Func, lamdba2Prov}
import cd.world.component.CDBaseComp.{CDBlockComp, CDBuildingComp}
import cd.world.component.PneuComp.standardPressure
import cd.world.stat.CDStats
import mindustry.Vars.{state, tilesize}
import mindustry.entities.{Damage, Effect}
import mindustry.gen.{Building, Sounds}
import mindustry.graphics.Pal
import mindustry.ui.Bar

trait PneuComp extends CDBlockComp {

  // For crafter

  /** Whether the building can provide pressure. */
  var canProvidePressure = false
  /** Pressure output per consume. */
  var outputPressure = 1f
  /** Whether the building can consume pressure. */
  var canConsumePressure = false
  /** Pressure consume per consume */
  var pressureConsume = 0.1f


  //for all block
  /** If pressure is lower than this point, the crafter will nor work. */
  var minOperatePressure = 2f
  /** When the pressure approach this point it will explode. */
  var explodePressure = 15f
  /** If pressure is over this point, the building will not work. */
  var maxOperatePressure = 10f

  //For explosion

  var explosionRadius = 3
  var explosionDamage = 50
  var explodeEffect: Effect = CDFx.pneuSmoke
  var explodeSound: Sound = Sounds.explosion
  var explosionShake = 6f

  var explosionShakeDuration = 16f

  override def setStats(): Unit = {
    super.setStats()
    stats.add(CDStats.pressureRange, Core.bundle.get("stat.pressure-range-format"), minOperatePressure.asInstanceOf[AnyRef], maxOperatePressure.asInstanceOf[AnyRef], explodePressure.asInstanceOf[AnyRef])
    if (canProvidePressure) stats.add(CDStats.pressureOutput, outputPressure, CDStats.perConsume)
    if (canConsumePressure) stats.add(CDStats.pressureConsume, pressureConsume, CDStats.perConsume)
  }

  override def setBars(): Unit = {
    addBar("pressure",
      (entity: PneuBuildingComp) => new Bar(() => Core.bundle.format("bar.pressure-amount", entity.pressure.asInstanceOf[AnyRef]).asInstanceOf[CharSequence], () => Pal.lightOrange, () => entity.pressure / explodePressure))
  }


  trait PneuBuildingComp extends CDBuildingComp {
    var pressure = 1f

    override def shouldConsume(): Boolean = {
      pressure < maxOperatePressure && (canProvidePressure || pressure > minOperatePressure) && (!canConsumePressure || pressure > pressureConsume)
    }

    override def placed(): Unit = {
      pressure = standardPressure
    }

    override def updateTile(): Unit = {
      calculatePressure()
    }

    private def calculatePressure(): Unit = {
      (if (block.rotate)
        proximity.select { (o: Building) => {
          Geometry.d4.apply(rotation).x == tileX - o.tileX &&
            Geometry.d4.apply(rotation).y == tileY - o.tileY
        }
        }
      else proximity)
        .items.collect {
        case other: PneuBuildingComp if pressure > other.pressure =>
          pressure = (pressure + other.pressure) / 2f
          other.pressure = (pressure + other.pressure) / 2f
        case _ =>
      }

      if (pressure > explodePressure) kill()
    }


    override def onDestroyed(): Unit = {
      //Why?
      if (state.rules.reactorExplosions) createExplosion()
    }

    private def createExplosion(): Unit = {
      if (pressure > explodePressure) {
        if (explosionDamage > 0) Damage.damage(x, y, explosionRadius * tilesize, explosionDamage)
        explodeEffect.at(this)
        explodeSound.at(this)
        if (explosionShake > 0) Effect.shake(explosionShake, explosionShakeDuration, this)
      }
    }
  }

  trait PneuCrafterComp extends CDCrafterComp {
    this: PneuBuildingComp=>
    override def craft(): Unit = {
      super.craft()
      if (canProvidePressure) pressure += outputPressure
      if (canConsumePressure) pressure -= pressureConsume
    }
  }

}


object PneuComp {
  /** The default pressure when building */
  val standardPressure = 1f
}
