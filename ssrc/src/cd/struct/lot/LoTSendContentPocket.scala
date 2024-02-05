package cd.struct.lot
import cd.world.blocks.lot.LoTNetworkDevice

class LoTSendContentPocket(val stack: ContentStack) extends LoTPocket {
  /** Set the responders of this pocket and then set them up. There is no need to abstract all the I/O ports
   * into the whole ones, since those blocks may not be together.  */
  override def respondedBy(devices: Seq[LoTNetworkDevice]): Unit = {
    devices.foreach(d => {
      if(d.require(stack)) d.obtain(stack)
    }) }
}
