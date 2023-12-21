package cd.struct.recipe

import arc.scene.Element
import arc.scene.ui.layout.Table
import cd.ui.Tablable
import cd.util.SAMConversation.lamdba2Prov
import mindustry.gen.Building
import mindustry.world.Block

import scala.language.implicitConversions

object CDCraft {
  trait CDRecipePart extends Tablable {
    def init(block: Block):Unit
  }

  trait CDProduce extends CDRecipePart {
    def genReaction():Table
    /** Only trigger once in a craft process */
    def triggerOnce(build: Building): Unit

    /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
    def triggerPerTick(build: Building, efficiency:Float): Unit

    /** Whether the build can produce the things this produce */
    def canProduce(building: Building): Boolean

  }


  trait CDCondition extends CDRecipePart {
    def genReaction():Array[Element]
    def sufficient(building: Building): Boolean

  }

  trait CDConsume extends CDRecipePart {

    def genReaction():Table
    def sufficient(building: Building): Boolean = efficiency(building) != 0f && efficiencyScale(building) != 0f

    /** Only trigger once in a craft process */
    def triggerOnce(build: Building): Unit

    /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
    def triggerPerTick(build: Building, efficiency:Float): Unit

    /** Return efficiency according to how this consume is supplied */
    def efficiency(build: Building): Float

    /** Return a scale for efficiency according to how this consume is supplied */
    def efficiencyScale(build: Building): Float

  }

  object CDConsume {
    /*implicit def consume2CDC(c: Consume): CDConsume = new CDConsume {
      /** Only trigger once in a craft process */
      override def triggerOnce(build: Building): Unit = c.trigger(build)

      /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
      override def triggerPerTick(building: Building, efficiency:Float): Unit = {
        c.update(building)
        c.multiplier = (_:Building) => efficiency
      }

      /** Return efficiency according to how this consume is supplied */
      override def efficiency(build: Building): Float = c.efficiency(build)

      /** Return a scale for efficiency according to how this consume is supplied */
      override def efficiencyScale(build: Building): Float = c.efficiencyMultiplier(build)

      override def buildTable(build: Building, table: Table): Table = {
        c.build(build, table)
        table
      }

      override def init(block: Block): Unit = {
        c.apply(block)
      }
    }*/
  }
}
