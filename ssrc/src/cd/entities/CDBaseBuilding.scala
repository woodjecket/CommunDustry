package cd.entities

import arc.util.io.Reads
import cd.world.blocks.CDBaseBlock
import cd.world.modules.{ProxyGroup, ProxyItemModule}
import mindustry.Vars
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.world.modules.{ItemModule, LiquidModule, PowerModule}

trait CDBaseBuilding extends Building {
  var useProxy = false
  var asMain = false
  
  override def readBase(read: Reads): Unit = {
    this.health = Math.min(read.f, this.block.health.toFloat)
    val rot = read.b
    this.team = Team.get(read.b)
    this.rotation = rot & 127
    var moduleBits = this.moduleBitmask
    var legacy = true
    var version = 0
    if((rot & 0x10000000) != 0) {
      version = read.b
      if(version >= 1) {
        val on = read.b
        this.enabled = on == 1
      }
      if(version >= 2) moduleBits = read.b
      legacy = false
    }
    var proxyModule = false
    if((moduleBits & 16) != 0) proxyModule = true
    
    if((moduleBits & 1) != 0) (if(this.items == null) (if(proxyModule) {
      read.s();
      read.s()
      ProxyItemModule(ProxyGroup(read.i()))
    } else new ItemModule)
                               else this.items).read(read, legacy)
    
    if((moduleBits & 2) != 0) (if(this.power == null) new PowerModule
                               else this.power).read(read, legacy)
    
    //TODO
    if((moduleBits & 4) != 0) (if(this.liquids == null) new LiquidModule
                               else this.liquids).read(read, legacy)
    
    if(version <= 2) read.bool
    
    if(version >= 3) {
      val f = read.ub.toFloat / 255.0F
      this.efficiency = f
      this.potentialEfficiency = f
      this.optionalEfficiency = read.ub.toFloat / 255.0F
    }
    
    if(version == 4) this.visibleFlags = read.l
    
  }
  
  private def proxyItems: ProxyItemModule = items match {
    case module: ProxyItemModule => module
    case _ => null
  }
  
  override def moduleBitmask: Int = (if(this.items != null) 1 else 0) |
    (if(this.power != null) 2 else 0) | (if(this.liquids != null) 4 else 0) | 8 | (if(useProxy) 16 else 0)
  
  private var tileChanged = -1
  
  protected def tileChange: Boolean = if(tileChanged != Vars.world.tileChanges) {tileChanged = Vars.world.tileChanges; true } else false
  
  override def updateTile(): Unit = {
    super.updateTile()
    if(tileChange) {
      val proxies: Array[CDBaseBuilding] = proximity.toArray().filter(_.isInstanceOf[CDBaseBuilding])
        .map(_.asInstanceOf[CDBaseBuilding])
      
      if(useProxy || asMain) {
        //Check if there is need to remove
        if(proxies.length == 0) {
          items = new ItemModule
        }
      } else {
        //Check if there is need to add
        val nonproxy = proxies.filter(p => {p.block.asInstanceOf[CDBaseBlock].useProxyModule && !p.useProxy && !p.asMain})
        val hasproxy = proxies.filter(p => {p.block.asInstanceOf[CDBaseBlock].useProxyModule && p.useProxy })
        if(nonproxy.length > 0) {
          val group = ProxyGroup(this.pos())
          for(elem <- nonproxy) {
            group + elem
          }
          asMain = true
        } else if(hasproxy.length > 0) {
          hasproxy(0).proxyItems.proxyGroup + this
          useProxy = true
        }
      }
    }
  }
  
}


