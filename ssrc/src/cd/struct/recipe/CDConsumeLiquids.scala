package cd.struct.recipe

import arc.scene.ui.layout.Table
import cd.struct.recipe.CDCraft.CDConsume
import cd.util.SAMConversation.{lamdba2Boolp, lamdba2Cons}
import mindustry.`type`.LiquidStack
import mindustry.gen.Building
import mindustry.ui.{ItemImage, LiquidDisplay, ReqImage}
import mindustry.world.Block

class CDConsumeLiquids(val liquids: Array[LiquidStack]) extends CDConsume  {
  override def genReaction(): Table = {
    new Table((ta: Table) => {
      liquids.foreach(stack => ta.add(new LiquidDisplay(stack.liquid, stack.amount * 60, true)))
    })
  }
  /** Only trigger once in a craft process */
  override def triggerOnce(build: Building): Unit = {}
  
  /** Trigger each tick during a craft process. Generally, 1/tick == 60/s */
  override def triggerPerTick(build: Building, efficiency: Float): Unit = {
    for(stack <- liquids) {
      build.liquids.remove(stack.liquid, stack.amount * build.delta() * efficiency)
    }
  }
  
  /** Return efficiency according to how this consume is supplied */
  override def efficiency(build: Building, efficiency: Float): Float = {
    val ed = build.delta() * efficiency
    if(ed <= 0.00000001f) return 0f
    var min = 1f
    for(stack <- liquids) {
      min = Math.min(build.liquids.get(stack.liquid) / (stack.amount * ed), min)
    }
    min
  }
  
  /** Return a scale for efficiency according to how this consume is supplied */
  override def efficiencyScale(build: Building): Float = 1f
  
  override def init(block: Block): Unit = {
    block.hasLiquids = true
    for(stack <- liquids) {
      block.liquidFilter(stack.liquid.id) = true
    }
  }
  
  override def buildTable(build: Building, table: Table): Table = {
    table.table((c: Table) => {
      var i = 0
      for(stack <- liquids) {
        c.add(new ReqImage(new ItemImage(stack.liquid.uiIcon, Math.round(stack.amount)), () => build.liquids.get(stack.liquid) > Math.round(stack.amount))).padRight(8)
        if( { i += 1; i } % 4 == 0) c.row
      }
    
    }).left.get
  }
}
