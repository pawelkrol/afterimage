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
        require(
          bitmap.get().length > 0,
          "Invalid HiRes image data slice: got %s, but expected between 8 and 8000 bitmap data bytes".format(
            bitmap.get().length.toString
          )
        )
        require(
          scr.get().length > 0,
          "Invalid HiRes image data slice: got %s, but expected between 1 and 1000 screen data bytes".format(
            scr.get().length.toString
          )
        )

        // Check if data slice dimensions are defined:
        require(
          width >= 8 && height >= 8 && width <= 320 && height <= 200,
          "Invalid HiRes image data slice: got %s, but expected image dimensions between 8x8 and 320x200".format(
            "%sx%s".format(width, height)
          )
        )

        // Check if both width and height parameters can by divided by 8 without any reminder:
        require(
          width % 8 == 0 && height % 8 == 0,
          "Invalid HiRes image data slice: got %s, but expected image dimensions to be numbers divisible by 8".format(
            "%sx%s".format(width, height)
          )
        )

        // Expected number of bytes in a "bitmap" array equals numBytes:
        require(
          bitmap.get().length == numBytes,
          "Invalid HiRes image data slice: got %s, but expected %s bitmap data bytes".format(
            bitmap.get().length.toString, numBytes.toString
          )
        )

        // Expected number of bytes in a "screen" array equals numChars:
        require(
          scr.get().length == numChars,
          "Invalid HiRes image data slice: got %s, but expected %s screen data bytes".format(
            scr.get().length.toString, numChars.toString
          )
        )

        // Check if amount of bitmap data exceeds maximum allowed value:
        require(
          bitmap.get().length <= HiRes.size("bitmap"),
          "Invalid HiRes image data slice: got %s, but expected %s bitmap data bytes".format(
            bitmap.get().length.toString, HiRes.size("bitmap").toString
          )
        )

        // Check if amount of screen data exceeds maximum allowed value:
        require(
          scr.get().length <= HiRes.size("screen"),
          "Invalid HiRes image data slice: got %s, but expected %s screen data bytes".format(
            scr.get().length.toString, HiRes.size("screen").toString
          )
        )
      }
      case None => {

        val numBytes = (widthRounded * heightRounded >> 3)

        // Check if provided data array is not empty:
        require(
          bitmap.get().length > 0,
          "Invalid HiRes image data slice: got %s, but expected between 1 and 8000 bitmap data bytes".format(
            bitmap.get().length.toString
          )
        )

        // Check if data slice dimensions are defined:
        require(
          width >= 1 && height >= 1 && width <= 320 && height <= 200,
          "Invalid HiRes image data slice: got %s, but expected image dimensions between 1x1 and 320x200".format(
            "%sx%s".format(width, height)
          )
        )

        // Expected number of bytes in a "bitmap" array equals numBytes:
        require(
          bitmap.get().length == numBytes,
          "Invalid HiRes image data slice: got %s, but expected %s bitmap data bytes".format(
            bitmap.get().length.toString, numBytes.toString
          )
        )

        // Check if amount of bitmap data exceeds maximum allowed value:
        require(
          bitmap.get().length <= HiRes.size("bitmap"),
          "Invalid HiRes image data slice: got %s, but expected %s bitmap data bytes".format(
            bitmap.get().length.toString, HiRes.size("bitmap").toString
          )
        )
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
