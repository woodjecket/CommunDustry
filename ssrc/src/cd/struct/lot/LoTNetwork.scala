package cd.struct.lot

import cd.world.blocks.lot.LoTNetworkDevice

import scala.collection.mutable.ArrayBuffer

class LoTNetwork private(){
  var devices: Seq[LoTNetworkDevice] = ArrayBuffer[LoTNetworkDevice]()
  var pocketQueue = ArrayBuffer[LoTPocket]()
  
  def add(loTNetworkDevice: LoTNetworkDevice): Unit = {
    devices :+= loTNetworkDevice
    loTNetworkDevice.network = this
  }
  def postPocket(loTPocket: LoTPocket): Unit = {
    pocketQueue :+= loTPocket
  }
  def update(): Unit = {
    pocketQueue.foreach(p=>{
      p.respondedBy(devices.filter(_.responsePocket(p)))
    })
    pocketQueue.clear()
  }
}
object LoTNetwork{
  def apply() = new LoTNetwork()
}
