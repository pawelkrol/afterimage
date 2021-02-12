package org.c64.attitude.Afterimage
package Format

import Memory.Address
import Mode.MultiColour

/** Koala Painter file format.
  *
  * @constructor create a new `KoalaPainter` image data
  * @param addr CBM file loading address
  * @param data array of file data bytes
  */
case class KoalaPainter(
  val addr: Address,
  val data: Array[Byte]
) extends Format {

  /** Memory configuration of the file format data. */
  val config = Config.KoalaPainter()

  /** Default file loading address. */
  val load = KoalaPainter.load

  /** Default length of file data bytes. */
  val size = KoalaPainter.size

  validate()

  /** States that objects of this class are never equal to objects of its superclass.
    *
    * @param that the value being probed for possible equality
    * @return `true` if `this` instance can possibly equal `that`, otherwise `false`
    */
  override def canEqual(that: Any) = that.isInstanceOf[KoalaPainter]
}

/** Factory for [[org.c64.attitude.Afterimage.Format.KoalaPainter]] instances. */
object KoalaPainter {

  /** Koala Painter file loading address. */
  val load: Address = 0x6000

  /** Koala Painter length of file data bytes. */
  val size = 0x2711

  /** Creates a new `KoalaPainter` file format from `MultiColour` image data.
    *
    * @param image `MultiColour` image data
    * @return a new `KoalaPainter` instance with the file data determined by the image data
    */
  def apply(image: MultiColour): KoalaPainter = {
    val data = image.bitmap.get() ++ image.screen.get() ++ image.colors.get() :+ image.bckgrd
    this(load, data)
  }
}
