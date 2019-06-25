package org.c64.attitude.Afterimage
package Mode

import Mode.Data.{Bitmap,Screen}
import Util.Util

/** HiRes image slice enables limiting amount of graphic data to a specified part of an original picture.
  *
  * @constructor create a new `HiResSlice` image data
  * @param bitmap plain hi-resolution bitmap data of an image
  * @param screen optional screen colours data of an image
  * @param border optional single byte of image border colour
  * @param width total width of a slice in number of pixels
  * @param height total height of a slice in number of pixels
  */
class HiResSlice(
  bitmap: Bitmap,
  screen: Option[Screen],
  border: Option[Byte],
  override val width: Int,
  override val height: Int
) extends HiRes(bitmap, screen, border) {

  /** Validates consistency of an object instance data. */
  override def validate(): Unit = {

    screen match {
      case Some(scr) => {

        val numBytes = width * height >> 3
        val numChars = (width >> 3) * (height >> 3)

        // Check if provided data arrays are not empty:
        if (bitmap.get().length == 0)
          throw new InvalidImageDataSliceException("HiRes", bitmap.get().length.toString, "between 8 and 8000", "bitmap data bytes")
        if (scr.get().length == 0)
          throw new InvalidImageDataSliceException("HiRes", scr.get().length.toString, "between 1 and 1000", "screen data bytes")

        // Check if data slice dimensions are defined:
        if (width < 8 || height < 8 || width > 320 || height > 200)
          throw new InvalidImageDataSliceException("HiRes", "%sx%s".format(width, height), "image dimensions", "between 8x8 and 320x200")

        // Check if both width and height parameters can by divided by 8 without any reminder:
        if (width % 8 > 0 || height % 8 > 0)
          throw new InvalidImageDataSliceException("HiRes", "%sx%s".format(width, height), "image dimensions", "to be numbers divisible by 8")

        // Expected number of bytes in a "bitmap" array equals numBytes:
        if (bitmap.get().length != numBytes)
          throw new InvalidImageDataSliceException("HiRes", bitmap.get().length.toString, numBytes.toString, "bitmap data bytes")

        // Expected number of bytes in a "screen" array equals numChars:
        if (scr.get().length != numChars)
          throw new InvalidImageDataSliceException("HiRes", scr.get().length.toString, numChars.toString, "screen data bytes")

        // Check if amount of bitmap data exceeds maximum allowed value:
        if (bitmap.get().length > HiRes.size("bitmap"))
          throw new InvalidImageDataSliceException("HiRes", bitmap.get().length.toString, HiRes.size("bitmap").toString, "bitmap data bytes")

        // Check if amount of screen data exceeds maximum allowed value:
        if (scr.get().length > HiRes.size("screen"))
          throw new InvalidImageDataSliceException("HiRes", scr.get().length.toString, HiRes.size("screen").toString, "screen data bytes")
      }
      case None => {

        val numBytes = (widthRounded * heightRounded >> 3)

        // Check if provided data array is not empty:
        if (bitmap.get().length == 0)
          throw new InvalidImageDataSliceException("HiRes", bitmap.get().length.toString, "between 1 and 8000", "bitmap data bytes")

        // Check if data slice dimensions are defined:
        if (width < 1 || height < 1 || width > 320 || height > 200)
          throw new InvalidImageDataSliceException("HiRes", "%sx%s".format(width, height), "image dimensions", "between 1x1 and 320x200")

        // Expected number of bytes in a "bitmap" array equals numBytes:
        if (bitmap.get().length != numBytes)
          throw new InvalidImageDataSliceException("HiRes", bitmap.get().length.toString, numBytes.toString, "bitmap data bytes")

        // Check if amount of bitmap data exceeds maximum allowed value:
        if (bitmap.get().length > HiRes.size("bitmap"))
          throw new InvalidImageDataSliceException("HiRes", bitmap.get().length.toString, HiRes.size("bitmap").toString, "bitmap data bytes")
      }
    }
  }
}

/** Factory for [[org.c64.attitude.Afterimage.Mode.HiResSlice]] instances. */
object HiResSlice {

  /** Creates a new HiResSlice image section of a well-defined dimensions with a given bitmap data and optional screen colours.
    *
    * @param bitmap plain hi-resolution bitmap data of an image (it needs to be in line with given slice dimensions)
    * @param screen optional screen portion of colours data of an image (it needs to be in line with given slice dimensions)
    * @param border optional single byte of image border colour
    * @param width total width of a slice in number of pixels
    * @param height total height of a slice in number of pixels
    */
  def apply(
    bitmap: Bitmap,
    screen: Option[Screen],
    border: Option[Byte],
    width: Int,
    height: Int
  ) = new HiResSlice(bitmap, screen, border, width, height)
}
