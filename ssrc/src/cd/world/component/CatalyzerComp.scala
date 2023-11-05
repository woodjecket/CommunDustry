package cd.world.component

import arc.math.Mathf
import cd.world.component.CDComp.{CDBlockComp, CDBuildingComp}
import cd.world.stat.CDStats
import mindustry.`type`.ItemStack
import mindustry.world.meta.StatValues;

trait CatalyzerComp extends CDBlockComp {

  /**
   * The catalyzer of the crafter. if exists, the efficiency will increase ,or it
   * can only work with catalyzer.
   */
  var catalyzer: Array[ItemStack] = null
  /**
   * Whether the crafter must work with catalyzer, if not, the catalyzers aur for
   * increasing the efficiency.
   */
  var catalyzerNecessity = false

  /** Base efficiency catalyzer for 100% */
  var baseCatalyzerScale = 1f
  /** Efficiency that catalyzers increases */
  var catalyzerScale: Array[Float] = Array(1f)
  /** Whether to add or multiply scale */
  var catalyzerCalculation = "mul"
  /** Maximum possible efficiency after catalyzer. */
  var maxEfficiency = 4f
  /** Chance to consume catalyzer. -1 for disabled */
  var catalyzerChance: Float = -1f

  override def setStats(): Unit = {
    super.setStats()
    stats.add(CDStats.catalyzer, StatValues.items(true, catalyzer: _*))
  }

  override def init(): Unit = {
    super.init()
    catalyzer.foreach(i => itemFilter(i.item.id) = true)
  }

  trait CatalyzerBuildingComp extends CDBuildingComp {
    //TODO Use Efficiency
    override def shouldConsume(): Boolean = {
      if (catalyzer != null && catalyzerNecessity) for (i <- catalyzer) {
        if (!items.has(i.item)) return false
      }
      super.shouldConsume()
    }

    override def updateTile(): Unit = {
      super.updateTile()
      if (Mathf.chanceDelta(catalyzerChance)) items.remove(catalyzer(Mathf.random(catalyzer.length - 1)))
    }

    override def efficiencyScale(): Float = {
      if (!catalyzerNecessity || catalyzerScale != null) {
        var result = baseCatalyzerScale
        for (i <- catalyzer.indices) {
          val stack = catalyzer(i)
          if (items.get(stack.item) >= stack.amount) catalyzerCalculation match {
            case "mul" => result *= catalyzerScale(i)
            case "add" => result += catalyzerScale(i)
          }
        }
        return Math.min(maxEfficiency, result)
      }
      baseCatalyzerScale
    }
  }


}
