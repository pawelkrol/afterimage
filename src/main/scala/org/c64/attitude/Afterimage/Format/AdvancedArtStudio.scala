package org.c64.attitude.Afterimage
package Format

import Memory.Address
import Mode.MultiColour

/** Advanced Art Studio file format.
  *
  * @constructor create a new `AdvancedArtStudio` image data
  * @param addr CBM file loading address
  * @param data array of file data bytes
  */
case class AdvancedArtStudio(
  val addr: Address,
  val data: Array[Byte]
) extends Format {

  /** Memory configuration of the file format data. */
  val config = Config.AdvancedArtStudio()

  /** Default file loading address. */
  val load = AdvancedArtStudio.load

  /** Default length of file data bytes. */
  val size = AdvancedArtStudio.size

  validate()

  /** States that objects of this class are never equal to objects of its superclass.
    *
    * @param that the value being probed for possible equality
    * @return `true` if `this` instance can possibly equal `that`, otherwise `false`
    */
  override def canEqual(that: Any) = that.isInstanceOf[AdvancedArtStudio]
}

/** Factory for [[org.c64.attitude.Afterimage.Format.AdvancedArtStudio]] instances. */
object AdvancedArtStudio {

  /** Advanced Art Studio file loading address. */
  val load: Address = 0x2000

  /** Advanced Art Studio length of file data bytes. */
  val size = 0x2720

  /** Creates a new `AdvancedArtStudio` file format from `MultiColour` image data.
    *
    * @param image `MultiColour` image data
    * @return a new `AdvancedArtStudio` instance with the file data determined by the image data
    */
  def apply(image: MultiColour): AdvancedArtStudio = {
    val data = image.bitmap.get() ++ image.screen.get() ++ Array(0x00.toByte) ++ Array(image.bckgrd) ++ Array.fill(0x0e){0x00.toByte} ++ image.colors.get()
    this(load, data)
  }
}
