package org.c64.attitude.Afterimage
package Format

import Memory.Address
import Mode.HiRes

/** Art Studio file format.
  *
  * @constructor create a new `ArtStudio` image data
  * @param addr CBM file loading address
  * @param data array of file data bytes
  */
case class ArtStudio(
  val addr: Address,
  val data: Array[Byte]
) extends Format {

  /** Memory configuration of the file format data. */
  val config = Config.ArtStudio()

  /** Default file loading address. */
  val load = ArtStudio.load

  /** Default length of file data bytes. */
  val size = ArtStudio.size

  validate()

  /** States that objects of this class are never equal to objects of its superclass.
    *
    * @param that the value being probed for possible equality
    * @return `true` if `this` instance can possibly equal `that`, otherwise `false`
    */
  override def canEqual(that: Any) = that.isInstanceOf[ArtStudio]
}

/** Factory for [[org.c64.attitude.Afterimage.Format.ArtStudio]] instances. */
object ArtStudio {

  /** Art Studio file loading address. */
  val load: Address = 0x2000

  /** Art Studio length of file data bytes. */
  val size = 0x2328

  /** Creates a new `ArtStudio` file format from `HiRes` image data.
    *
    * @param image `HiRes` image data
    * @return a new `ArtStudio` instance with the file data determined by the image data
    */
  def apply(image: HiRes): ArtStudio = {
    val screen = image.screen match {
      case Some(scr) => scr.get()
      case None => image.emptyScreen
    }
    val data = image.bitmap.get() ++ screen
    this(load, data)
  }
}
