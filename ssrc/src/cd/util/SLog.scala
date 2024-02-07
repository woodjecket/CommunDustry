package cd.util

import arc.util.Log

object SLog {
  def info(xs : String ):Unit = {
    Log.info(xs.asInstanceOf[AnyRef])
  }
}
