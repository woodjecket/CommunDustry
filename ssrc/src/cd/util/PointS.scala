package cd.util

final case class PointS(x: Int, y: Int, rot: Int) extends Immutable {
  val serialVersionUID: Long = 764687L
  
  def toInnerPosition(isMainEven:Boolean,isStructureEven: Boolean): InnerPointS = {
    if(isMainEven == isStructureEven) InnerPointS(x,y,rot)
    else if (isMainEven) InnerPointS(x + 0.5f, y + 0.5f, rot)
    else InnerPointS(x - 0.5f, y - 0.5f, rot)
  }
  
  
  case class InnerPointS(x: Float, y: Float, rot: Int) extends Immutable{
    def toActualPosition(isEven: Boolean): PointS = if(x.isValidInt) PointS(x.toInt,y.toInt,rot) else
      (if(isEven) PointS((x - 0.5f).toInt, (y - 0.5f).toInt, rot) else PointS((x + 0.5f).toInt,( y + 0.5f).toInt, rot))
  
  
    def rotateCounterClockwise(): InnerPointS = InnerPointS(-y, x, (rot % 4) + 1)
  
    def flipHorizontal(): InnerPointS = rot match {
      case 1 => InnerPointS(-x, y, 3)
      case 3 => InnerPointS(-x, y, 1)
      case 2 => InnerPointS(-x, y, 2)
      case 4 => InnerPointS(-x, y, 4)
      case 0 => InnerPointS(-x, y, 0)
    }
  
    def flipVetical(): InnerPointS = rot match {
      case 1 => InnerPointS(x, -y, 1)
      case 3 => InnerPointS(x, -y, 3)
      case 2 => InnerPointS(x, -y, 4)
      case 4 => InnerPointS(x, -y, 2)
      case 0 => InnerPointS(x, -y, 0)
    }
  }
}

object PointS {
  def processPosition(array: Array[PointS],isMainEven:Boolean,size:Array[Boolean]): Array[Array[PointS]] = {
    val o = array.map{a=>a.toInnerPosition(isMainEven,size(array.toIndexedSeq.indexOf(a)))}
    val h = o.map(_.flipHorizontal())
    val v = o.map(_.flipVetical())
    val cs = v.map(_.flipHorizontal())
    
    val ccw = o.map(_.rotateCounterClockwise())
    val hccw = h.map(_.rotateCounterClockwise())
    val vccw = v.map(_.rotateCounterClockwise())
    val cw = cs.map(_.rotateCounterClockwise())
    
    Array(o, h, v, cs, ccw, hccw, vccw, cw).map{_.map{_.toActualPosition(isMainEven)}}
  }
}

