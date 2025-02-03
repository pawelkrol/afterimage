package com.github.pawelkrol.Afterimage
package Util

object ArrayHelper {

  // The following code is taken from: https://github.com/scala/scala/blob/2.13.x/src/partest/scala/tools/partest/Util.scala
  def prettyArray(a: Array[?]): collection.IndexedSeq[Any] = new collection.AbstractSeq[Any] with collection.IndexedSeq[Any] {
    def length = a.length

    def apply(idx: Int): Any = a(idx) match {
      case x: AnyRef if x.getClass.isArray => prettyArray(x.asInstanceOf[Array[?]])
      case x => x
    }

    override def className = "Array"
  }

  def deep[T](a: Array[T]): collection.IndexedSeq[Any] = prettyArray(a)
}
