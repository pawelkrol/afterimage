package org.c64.attitude.Afterimage
package Sprite

trait SpriteProperties {

  val expandX: Boolean

  val expandY: Boolean

  protected val hasPriority: Boolean

  require(hasPriority == true, "Higher background to sprite display priority is currently not supported")

  def pixels(bits: Array[String]): Seq[Option[Tuple2[Int, Boolean]]]
}
