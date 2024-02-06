package cd.world

import arc.math.Mathf
import arc.scene.style.TextureRegionDrawable
import arc.scene.ui.layout.Table
import arc.scene.ui.{ButtonGroup, ImageButton, ScrollPane, TextField}
import arc.util.Nullable
import cd.util.SAMConversation.{lamdba2Cons, lamdba2Prov, lamdba2Runnable}
import mindustry.Vars
import mindustry.Vars.control
import mindustry.`type`.Item
import mindustry.ctype.UnlockableContent
import mindustry.gen.{Icon, Tex}
import mindustry.ui.Styles
import mindustry.world.Block

import scala.collection.mutable.ArrayBuffer

object ContentSelection {
  private var search: TextField = null
  private var rowCount = 0
  
  /*  def buildTable[T <: UnlockableContent](table: Table, items: ArrayBuffer[T], holder: () => T,
                                           consumer: T => () , rows: Int = 5, columns: Int = 4,
                                           closeSelect: Boolean = true, @Nullable block: Block = null): Unit = {
      val group = new ButtonGroup[ImageButton]
      group.setMinCheckCount(0)
      val cont = new Table().top
      cont.defaults.size(40)
      
      if(search != null) search.clearText()
    
      val rebuild = () => {
        group.clear()
        cont.clearChildren()
      
        val text = if(search != null) search.getText else ""
        var i = 0
        rowCount = 0
      
        val list = items.filter((u: T) => text.isEmpty || u.localizedName.toLowerCase.contains(text.toLowerCase))
          .filter(item => {!item.unlockedNow() || item.isInstanceOf[Item] && Vars.state.rules.hiddenBuildItems.contains(item.asInstanceOf[Item]) || item.isHidden })
        list.foreach(item => {
        
          val button = cont.button(Tex.whiteui, Styles.clearNoneTogglei, Mathf.clamp(item.selectionSize, 0f, 40f), () => {
            if(closeSelect) control.input.config.hideConfig()
          
          }).tooltip(item.localizedName).group(group).get
          button.changed(() => consumer.get(if(button.isChecked) item
          else null.asInstanceOf[T]))
          button.getStyle.imageUp = new TextureRegionDrawable(item.uiIcon)
          button.update(() => button.setChecked(holder.get eq item))
        
          if( { i += 1; i - 1 } % columns == (columns - 1)) {
            cont.row
            rowCount += 1
          }
        })
      }
      rebuild()
      
      val main = new Table().background(Styles.black6)
      if(rowCount > rows * 1.5f) main.table((s: Table) => {
        s.image(Icon.zoom).padLeft(4f)
        search = s.field(null, (_: String) => rebuild.run()).padBottom(4).left.growX.get
        search.setMessageText("@players.search")
        
      }).fillX.row()
      
      val pane = new ScrollPane(cont, Styles.smallPane)
      pane.setScrollingDisabled(true, false)
      
      if(block != null) {
        pane.setScrollYForce(block.selectScroll)
        pane.update(() => {
          block.selectScroll = pane.getScrollY
          
        })
      }
      
      pane.setOverscroll(false, false)
      main.add(pane).maxHeight(40 * rows)
      table.top.add(main)
    }*/
  
  def buildTables[T <: UnlockableContent, A <: ArrayBuffer[T]](table: Table, items: Array[T], holder: () => A,
                                                               consumer: T => Unit, rows: Int = 5, columns: Int = 4,
                                                               closeSelect: Boolean = true, @Nullable block: Block = null): Unit = {
    val group = new ButtonGroup[ImageButton]
    group.setMinCheckCount(0)
    val cont = new Table().top
    cont.defaults.size(40)
    
    if(search != null) search.clearText()
    
    val rebuild = () => {
      group.clear()
      cont.clearChildren()
      
      val text = if(search != null) search.getText else ""
      var i = 0
      rowCount = 0
      
      val list = items.filter((u: T) => text.isEmpty || u.localizedName.toLowerCase.contains(text.toLowerCase))
        .filterNot(item => {!item.unlockedNow() || item.isInstanceOf[Item] && Vars.state.rules.hiddenBuildItems.contains(item.asInstanceOf[Item]) || item.isHidden })
      list.foreach(item => {
        
        val button = cont.button(Tex.whiteui, Styles.clearNoneTogglei, Mathf.clamp(item.selectionSize, 0f, 40f), () => {
          if(closeSelect) control.input.config.hideConfig()
          
        }).tooltip(item.localizedName).group(group).get
        button.changed(() => consumer.get(if(button.isChecked) item
        else null.asInstanceOf[T]))
        button.getStyle.imageUp = new TextureRegionDrawable(item.uiIcon)
        button.update(() => button.setChecked(holder.get.contains(item)))
        
        if( { i += 1; i - 1 } % columns == (columns - 1)) {
          cont.row
          rowCount += 1
        }
      })
    }
    rebuild()
    
    val main = new Table().background(Styles.black6)
    if(rowCount > rows * 1.5f) main.table((s: Table) => {
      s.image(Icon.zoom).padLeft(4f)
      search = s.field(null, (_: String) => rebuild.run()).padBottom(4).left.growX.get
      search.setMessageText("@players.search")
      
    }).fillX.row()
    
    val pane = new ScrollPane(cont, Styles.smallPane)
    pane.setScrollingDisabled(true, false)
    
    if(block != null) {
      pane.setScrollYForce(block.selectScroll)
      pane.update(() => {
        block.selectScroll = pane.getScrollY
        
      })
    }
    
    pane.setOverscroll(false, false)
    main.add(pane).maxHeight(40 * rows)
    table.top.add(main)
    ()
  }
  
}