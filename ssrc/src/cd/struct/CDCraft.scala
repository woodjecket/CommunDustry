package cd.struct

import arc.scene.ui.layout.Table
import cd.ui.Tablable
import mindustry.gen.Building
import mindustry.world.consumers.Consume

import scala.language.implicitConversions

object CDCraft {
  trait CDRecipePart extends Tablable{}

  trait CDProduce extends CDRecipePart {
    /** Only trigger once in a craft process */
    def triggerOnce(build:Building): Unit

    /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
    def triggerPerTick(build:Building): Unit

    /**   Whether the build can produce the things this produce   */
    def canProduce(build:Building): Boolean = build.shouldConsume()

  }


  trait CDCondition extends CDRecipePart {
    def sufficient(build: Building): Boolean

  }

  trait CDConsume extends CDRecipePart {
    /** Only trigger once in a craft process */
    def triggerOnce(build: Building): Unit

    /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
    def triggerPerTick(build: Building): Unit

    /** Return efficiency according to how this consume is supplied */
    def efficiency(build: Building): Unit

    /** Return a scale for efficiency according to how this consume is supplied */
    def efficiencyScale(build: Building): Float

  }

  object CDConsume {
    implicit def consume2CDC(c: Consume): CDConsume = new CDConsume {
      /** Only trigger once in a craft process */
      override def triggerOnce(build: Building): Unit = c.trigger(build)

      /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
      override def triggerPerTick(build: Building): Unit = c.update(build)

      /** Return efficiency according to how this consume is supplied */
      override def efficiency(build: Building): Unit = c.efficiency(build)

      /** Return a scale for efficiency according to how this consume is supplied */
      override def efficiencyScale(build: Building): Float = c.efficiencyMultiplier(build)

      override def buildTable(build: Building, table: Table): Table = {
        c.build(build, table)
        table
      }
    }
  }
}
