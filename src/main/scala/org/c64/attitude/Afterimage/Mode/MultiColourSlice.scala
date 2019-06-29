package org.c64.attitude.Afterimage
package Mode

import Mode.Data.{Bitmap,Screen}

/** MultiColour image slice enables limiting amount of graphic data to a specified part of an original picture.
  *
  * @constructor create a new `MultiColourSlice` image data
  * @param bitmap plain hi-resolution bitmap data of an image
  * @param screen screen portion of colours data of an image
  * @param colors colors portion of colours data of an image
  * @param border optional single byte of image border colour
  * @param bckgrd single byte of image background colour
  * @param width total width of a slice in number of pixels
  * @param height total height of a slice in number of pixels
  */
class MultiColourSlice(
  bitmap: Bitmap,
  screen: Screen,
  colors: Screen,
  border: Option[Byte],
  bckgrd: Byte,
  override val width: Int,
  override val height: Int
) extends MultiColour(bitmap, screen, colors, border, bckgrd) {

  /** Validates consistency of an object instance data. */
  override def validate(): Unit = {

    val numBytes = width * 2 * height >> 3
    val numChars = (width * 2 >> 3) * (height >> 3)

    // Check if provided data arrays are not empty:
    require(
      bitmap.get().length > 0,
      "Invalid MultiColour image data slice: got %s, but expected between 8 and 8000 bitmap data bytes".format(
        bitmap.get().length.toString
      )
    )
    require(
      screen.get().length > 0,
      "Invalid MultiColour image data slice: got %s, but expected between 1 and 1000 screen data bytes".format(
        screen.get().length.toString
      )
    )
    require(
      colors.get().length > 0,
      "Invalid MultiColour image data slice: got %s, but expected between 1 and 1000 colour data bytes".format(
        colors.get().length.toString
      )
    )

    // Check if data slice dimensions are defined:
    require(
      width >= 4 && height >= 8 && width <= 160 && height <= 200,
      "Invalid MultiColour image data slice: got %s, but expected image dimensions between 8x8 and 160x200".format(
        "%sx%s".format(width, height)
      )
    )

    // Check if both width and height parameters can by divided by 8 without any reminder:
    require(
      width % 4 <= 0 && height % 8 <= 0,
      "Invalid MultiColour image data slice: got %s, but expected image dimensions to be numbers divisible by 8".format(
        "%sx%s".format(width, height)
      )
    )

    // Expected number of bytes in a "bitmap" array equals numBytes:
    require(
      bitmap.get().length == numBytes,
      "Invalid MultiColour image data slice: got %s, but expected %s bitmap data bytes".format(
        bitmap.get().length.toString, numBytes.toString
      )
    )

    // Expected number of bytes in a "screen" array equals numChars:
    require(
      screen.get().length == numChars,
      "Invalid MultiColour image data slice: got %s, but expected %s screen data bytes".format(
        screen.get().length.toString, numChars.toString
      )
    )

    // Expected number of bytes in a "colors" array equals numChars:
    require(
      colors.get().length == numChars,
      "Invalid MultiColour image data slice: got %s, but expected %s colour data bytes".format(
        colors.get().length.toString, numChars.toString
      )
    )

    // Check if amount of bitmap data exceeds maximum allowed value:
    require(
      bitmap.get().length <= MultiColour.size("bitmap"),
      "Invalid MultiColour image data slice: got %s, but expected %s bitmap data bytes".format(
        bitmap.get().length.toString, MultiColour.size("bitmap").toString
      )
    )

    // Check if amount of screen data exceeds maximum allowed value:
    require(
      screen.get().length <= MultiColour.size("screen"),
      "Invalid MultiColour image data slice: got %s, but expected %s screen data bytes".format(
        screen.get().length.toString, MultiColour.size("screen").toString
      )
    )

    // Check if amount of colors data exceeds maximum allowed value:
    require(
      colors.get().length <= MultiColour.size("colors"),
      "Invalid MultiColour image data slice: got %s, but expected %s colour data bytes".format(
        colors.get().length.toString, MultiColour.size("colors").toString
      )
    )
  }
}

/** Factory for [[org.c64.attitude.Afterimage.Mode.MultiColourSlice]] instances. */
object MultiColourSlice {

  /** Creates a new MultiColourSlice image section of a well-defined dimensions with a given bitmap data and optional screen colours.
    *
    * @param bitmap plain hi-resolution bitmap data of an image (it needs to be in line with given slice dimensions)
    * @param screen screen portion of colours data of an image (it needs to be in line with given slice dimensions)
    * @param colors colors portion of colours data of an image (it needs to be in line with given slice dimensions)
    * @param border optional single byte of image border colour
    * @param bckgrd single byte of image background colour
    * @param width total width of a slice in number of pixels
    * @param height total height of a slice in number of pixels
    */
  def apply(
    bitmap: Bitmap,
    screen: Screen,
    colors: Screen,
    border: Option[Byte],
    bckgrd: Byte,
    width: Int,
    height: Int
  ) = new MultiColourSlice(bitmap, screen, colors, border, bckgrd, width, height)
}
