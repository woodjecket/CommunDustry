package cd.util

import arc.func._

import scala.language.implicitConversions

object SAMConversation {
  implicit def lamdba2Runnable(l: () => Unit): Runnable = new Runnable {
    override def run(): Unit = l.apply()
  }

  implicit def lamdba2Cons[T](l: T => Unit): Cons[T] = new Cons[T] {
    override def get(t: T): Unit = l.apply(t)
  }

  implicit def lamdba2Prov[T <: AnyRef](l: () => T): Prov[T] = new Prov[T] {
    override def get(): T = l.apply()
  }

  implicit def lamdba2Floatp(l: () => Float): Floatp = new Floatp {
    override def get(): Float = l.apply()
  }

  implicit def lamdba2Boolf[T](l: T => Boolean): Boolf[T] = new Boolf[T] {
    override def get(v1:T): Boolean = l.apply(v1)
  }

  implicit def lamdba2Func[T,U](l:T => U): Func[T,U] = new Func[T,U] {
    override def get(v1:T): U = l.apply(v1)
  }
}
