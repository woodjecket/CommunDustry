package cd.struct.recipe

import arc.scene.ui.layout.Table
import cd.struct.recipe.CDCraft.CDConsume
import cd.util.SAMConversation.{lamdba2Boolp, lamdba2Cons}
import mindustry.`type`.ItemStack
import mindustry.gen.Building
import mindustry.ui.{ItemDisplay, ItemImage, ReqImage}
import mindustry.world.Block

class CDConsumeItems(val items: Array[ItemStack]) extends CDConsume {

  /** Only trigger once in a craft process */
  override def triggerOnce(build: Building): Unit = {
    build.items.remove(items)
  }

  /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
  override def triggerPerTick(build: Building, efficiency: Float): Unit = {}

  /** Return efficiency according to how this consume is supplied */
  override def efficiency(build: Building,efficiency:Float): Float = if (build.items.has(items)) 1f else 0f

  /** Return a scale for efficiency according to how this consume is supplied */
  override def efficiencyScale(build: Building): Float = 1f

  override def buildTable(build: Building, table: Table): Table = {
    table.table((c: Table) => {
      var i = 0
      for (stack <- items) {
        c.add(new ReqImage(new ItemImage(stack.item.uiIcon, Math.round(stack.amount)), () => build.items.has(stack.item, Math.round(stack.amount)))).padRight(8)
        if ( { i += 1; i } % 4 == 0) c.row
      }

    }).left.get
  }

  override def init(block: Block): Unit = {
    items.foreach(stack => block.itemFilter(stack.item.id) = true)
  }

  override def genReaction(): Table = {
    new Table((ta: Table) => {
      items.foreach(stack => ta.add(new ItemDisplay(stack.item, stack.amount)))
    })
  }
}
