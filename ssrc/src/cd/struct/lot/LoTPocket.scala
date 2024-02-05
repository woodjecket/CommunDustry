package cd.struct.lot

import cd.struct.lot.LoTPocketStatementDef.{LoTPocketStatement, ok}
import cd.world.blocks.lot.LoTNetworkDevice

trait LoTPocket {
  /** Set the responders of this pocket and then set them up. There is no need to abstract all the I/O ports
   * into the whole ones, since those blocks may not be together.*/
  def respondedBy(devices: Seq[LoTNetworkDevice]): Unit
  
  var statement: LoTPocketStatement = ok
}

object LoTPocketStatementDef extends Enumeration {
  type LoTPocketStatement = Value
  val ok = Value("OK")
  val noResponse = Value("No Response")
}
