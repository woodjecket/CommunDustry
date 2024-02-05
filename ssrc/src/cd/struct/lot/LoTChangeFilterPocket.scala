package cd.struct.lot
import cd.world.blocks.lot.LoTNetworkDevice
import mindustry.ctype.UnlockableContent

import scala.collection.mutable

class LoTChangeFilterPocket(val filters: mutable.Map[UnlockableContent, Boolean]) extends LoTPocket{
  /** Set the responders of this pocket. There is no need to abstract all the I/O ports into the whole ones,
   * since those blocks may not be together.  */
  override def respondedBy(devices: Seq[LoTNetworkDevice]): Unit = {
    devices.foreach(d=>d.filters += filters)
  }
}
