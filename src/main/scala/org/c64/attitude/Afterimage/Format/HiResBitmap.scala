package org.c64.attitude.Afterimage
package Format

import Memory.Address
import Mode.HiRes

/** HiRes Bitmap file format.
  *
  * @constructor create a new `HiResBitmap` image data
  * @param addr CBM file loading address
  * @param data array of file data bytes
  */
case class HiResBitmap(
  val addr: Address,
  val data: Array[Byte]
) extends Format {

  /** Memory configuration of the file format data. */
  val config = Config.HiResBitmap()

  /** Default file loading address. */
  val load = HiResBitmap.load

  /** Default length of file data bytes. */
  val size = HiResBitmap.size

  override def validate(): Unit = {
    require(
      data.length == size,
      "Invalid image data: Not a %s format".format(this.getClass.getName.split("\\.").last)
    )
  }

  validate()

  /** States that objects of this class are never equal to objects of its superclass.
    *
    * @param that the value being probed for possible equality
    * @return `true` if `this` instance can possibly equal `that`, otherwise `false`
    */
  override def canEqual(that: Any) = that.isInstanceOf[HiResBitmap]
}

/** Factory for [[org.c64.attitude.Afterimage.Format.HiResBitmap]] instances. */
object HiResBitmap {

  /** HiRes Bitmap file loading address (irrelevant for hires bitmap file format). */
  val load: Address = 0x0000

  /** HiRes Bitmap length of file data bytes. */
  val size = 0x1f40

  /** Creates a new `HiResBitmap` file format from `HiRes` image data.
    *
    * @param image `HiRes` image data
    * @return a new `HiResBitmap` instance with the file data determined by the image data
    */
  def apply(image: HiRes): HiResBitmap = {
    val data = image.bitmap.get()
    this(load, data)
  }
}
