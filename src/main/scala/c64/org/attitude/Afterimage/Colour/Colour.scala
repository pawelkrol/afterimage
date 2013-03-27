package org.c64.attitude.Afterimage
package Colour

import java.lang.Math.sqrt

/** Simple RGB colour abstraction with additional colour comparison methods.
  *
  * @constructor create a new RGB colour definition
  * @param red red component
  * @param green green component
  * @param blue blue component
  * @param name optional name
  */
case class Colour(red: Byte, green: Byte, blue: Byte, name: Option[String]) {

  private val vector = Vector(red, green, blue)

  private def diffVector(vec: Vector[Byte]) =
    this.vector.zip(vec).map(pair => pair._1 - pair._2)

  private def vectorLength(vec: Vector[Int]) =
    sqrt(vec.map(elem => elem * elem).sum)

  /** Calculates a vector distance between two colours.
    *
    * @param that colour which is used as a base for this calculation
    */
  def delta_to(that: Colour) = vectorLength(diffVector(that.vector))

  /** Returns ImageJ's pixel colour for an RGB colour. */
  val pixel: Int = 256 * 256 * red + 256 * green + blue
}

/** Factory for [[org.c64.attitude.Afterimage.Colour.Colour]] instances. */
object Colour {

  /** Creates a default (black) RGB colour definition. */
  def apply() = new Colour(0x00, 0x00, 0x00, Some("default"))

  /** Creates a new RGB colour definition from a map of key/value pairs.
    *
    * @param params map containig the following keys: red, green, blue, name
    */
  def apply(params: Map[String,Any]) = {
    val name = if (params.contains("name")) Some(params("name").toString) else None
    new Colour(
      params("red").toString.toDouble.round.toByte,
      params("green").toString.toDouble.round.toByte,
      params("blue").toString.toDouble.round.toByte,
      name
    )
  }
}
