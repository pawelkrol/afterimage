package org.c64.attitude.Afterimage
package Sprite

/** HiRes sprite properties.
  *
  * @constructor create a new `HiResProps` sprite data
  * @param colour sprite colour
  * @param expandX a boolean flag indicating to expand sprite 2x horizontally
  * @param expandY a boolean flag indicating to expand sprite 2x vertically
  * @param hasPriority a boolean flag determining higher sprite to background display priority
  */
class HiResProps(
  colour: Int,
  val expandX: Boolean = false,
  val expandY: Boolean = false,
  protected val hasPriority: Boolean = true
) extends SpriteProperties {

  /** Returns an array of pixel colours defined as tuples of an actual colour and its display priority. */
  def pixels(bits: Array[String]): Seq[Option[Tuple2[Int, Boolean]]] =
    bits.toSeq.map(bit => bit match {
      case "1" =>
        Some((colour, hasPriority))
      case "0" =>
        None
      case _ =>
        throw new RuntimeException("Illegal bit value: '%s'".format(bit))
    })
}

/** Factory for [[org.c64.attitude.Afterimage.Sprite.HiResProps]] instances. */
object HiResProps {

  /** Creates a new `HiResProps` sprite data with default properties.
    *
    * @param colour sprite colour
    * @return a new `HiResProps` sprite data instance
    */
  def apply(
    colour: Int
  ): HiResProps = new HiResProps(colour)

  /** Creates a new `HiResProps` sprite data.
    *
    * @param colour sprite colour
    * @param expandX a boolean flag indicating to expand sprite 2x horizontally
    * @param expandY a boolean flag indicating to expand sprite 2x vertically
    * @param hasPriority a boolean flag determining higher sprite to background display priority
    * @return a new `HiResProps` sprite data instance
    */
  def apply(
    colour: Int,
    expandX: Boolean,
    expandY: Boolean,
    hasPriority: Boolean
  ): HiResProps = new HiResProps(colour, expandX, expandY, hasPriority)
}
