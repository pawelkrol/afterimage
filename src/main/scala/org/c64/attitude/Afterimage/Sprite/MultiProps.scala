package org.c64.attitude.Afterimage
package Sprite

/** MultiColour sprite properties.
  *
  * @constructor create a new `MultiProps` sprite data
  * @param colour sprite colour
  * @param multiColour0 sprite multicolour register 0
  * @param multiColour1 sprite multicolour register 1
  * @param expandX a boolean flag indicating to expand sprite 2x horizontally
  * @param expandY a boolean flag indicating to expand sprite 2x vertically
  * @param hasPriority a boolean flag determining higher sprite to background display priority
  */
class MultiProps(
  colour: Int,
  multiColour0: Int,
  multiColour1: Int,
  val expandX: Boolean = false,
  val expandY: Boolean = false,
  protected val hasPriority: Boolean = true
) extends SpriteProperties {

  /** Returns an array of pixel colours defined as tuples of an actual colour and its display priority. */
  def pixels(bits: Array[String]): Seq[Option[Tuple2[Int, Boolean]]] =
    bits.grouped(2).toSeq.map(_.reduce(_ + _)).map(bit => bit match {
      case "11" =>
        Some(multiColour1, hasPriority)
      case "10" =>
        Some(colour, hasPriority)
      case "01" =>
        Some(multiColour0, hasPriority)
      case "00" =>
        None
      case _ =>
        throw new RuntimeException("Illegal bit value: '%s'".format(bit))
    }).flatMap(pixel => List(pixel, pixel))
}

/** Factory for [[org.c64.attitude.Afterimage.Sprite.MultiProps]] instances. */
object MultiProps {

  /** Creates a new `MultiProps` sprite data with default properties.
    *
    * @param colour sprite colour
    * @param multiColour0 sprite multicolour register 0
    * @param multiColour1 sprite multicolour register 1
    * @return a new `MultiProps` sprite data instance
    */
  def apply(
    colour: Int,
    multiColour0: Int,
    multiColour1: Int
  ): MultiProps = new MultiProps(colour, multiColour0, multiColour1)

  /** Creates a new `MultiProps` sprite data.
    *
    * @param colour sprite colour
    * @param multiColour0 sprite multicolour register 0
    * @param multiColour1 sprite multicolour register 1
    * @param expandX a boolean flag indicating to expand sprite 2x horizontally
    * @param expandY a boolean flag indicating to expand sprite 2x vertically
    * @param hasPriority a boolean flag determining higher sprite to background display priority
    * @return a new `MultiProps` sprite data instance
    */
  def apply(
    colour: Int,
    multiColour0: Int,
    multiColour1: Int,
    expandX: Boolean,
    expandY: Boolean,
    hasPriority: Boolean
  ): MultiProps = new MultiProps(colour, multiColour0, multiColour1, expandX, expandY, hasPriority)
}
