package org.c64.attitude.Afterimage
package Format

import Memory.Address
import Mode.MultiColour

/** Face Painter file format.
  *
  * @constructor create a new `FacePainter` image data
  * @param addr CBM file loading address
  * @param data array of file data bytes
  */
case class FacePainter(
  val addr: Address,
  val data: Array[Byte]
) extends Format {

  /** Memory configuration of the file format data. */
  val config = Config.FacePainter()

  /** Default file loading address. */
  val load = FacePainter.load

  /** Default length of file data bytes. */
  val size = FacePainter.size

  validate()

  /** States that objects of this class are never equal to objects of its superclass.
    *
    * @param that the value being probed for possible equality
    * @return `true` if `this` instance can possibly equal `that`, otherwise `false`
    */
  override def canEqual(that: Any) = that.isInstanceOf[FacePainter]
}

/** Factory for [[org.c64.attitude.Afterimage.Format.FacePainter]] instances. */
object FacePainter {

  /** Face Painter file loading address. */
  val load: Address = 0x4000

  /** Face Painter length of file data bytes. */
  val size = 0x2712

  /** Creates a new `FacePainter` file format from `MultiColour` image data.
    *
    * @param image `MultiColour` image data
    * @return a new `FacePainter` instance with the file data determined by the image data
    */
  def apply(image: MultiColour): FacePainter = {
    val border: Byte = image.border match {
      case Some(colour) => colour
      case None         => 0x00
    }
    val data = image.bitmap.get() ++ image.screen.get() ++ image.colors.get() :+ border :+ image.bckgrd
    this(load, data)
  }
}
