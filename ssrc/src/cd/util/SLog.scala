package cd.util

import arc.util.Log

object SLog {
  def info(xs : AnyRef* ):Unit = {
    Log.info(xs)
  }
}
