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
    if (bitmap.get().length == 0)
      throw new InvalidImageDataSliceException("MultiColour", bitmap.get().length.toString, "between 8 and 8000", "bitmap data bytes")
    if (screen.get().length == 0)
      throw new InvalidImageDataSliceException("MultiColour", screen.get().length.toString, "between 1 and 1000", "screen data bytes")
    if (colors.get().length == 0)
      throw new InvalidImageDataSliceException("MultiColour", colors.get().length.toString, "between 1 and 1000", "colour data bytes")

    // Check if data slice dimensions are defined:
    if (width < 4 || height < 8 || width > 160 || height > 200)
      throw new InvalidImageDataSliceException("MultiColour", "%sx%s".format(width, height), "image dimensions", "between 8x8 and 160x200")

    // Check if both width and height parameters can by divided by 8 without any reminder:
    if (width % 4 > 0 || height % 8 > 0)
      throw new InvalidImageDataSliceException("MultiColour", "%sx%s".format(width, height), "image dimensions", "to be numbers divisible by 8")

    // Expected number of bytes in a "bitmap" array equals numBytes:
    if (bitmap.get().length != numBytes)
      throw new InvalidImageDataSliceException("MultiColour", bitmap.get().length.toString, numBytes.toString, "bitmap data bytes")

    // Expected number of bytes in a "screen" array equals numChars:
    if (screen.get().length != numChars)
      throw new InvalidImageDataSliceException("MultiColour", screen.get().length.toString, numChars.toString, "screen data bytes")

    // Expected number of bytes in a "colors" array equals numChars:
    if (colors.get().length != numChars)
      throw new InvalidImageDataSliceException("MultiColour", colors.get().length.toString, numChars.toString, "colour data bytes")

    // Check if amount of bitmap data exceeds maximum allowed value:
    if (bitmap.get().length > MultiColour.size("bitmap"))
      throw new InvalidImageDataSliceException("MultiColour", bitmap.get().length.toString, MultiColour.size("bitmap").toString, "bitmap data bytes")

    // Check if amount of screen data exceeds maximum allowed value:
    if (screen.get().length > MultiColour.size("screen"))
      throw new InvalidImageDataSliceException("MultiColour", screen.get().length.toString, MultiColour.size("screen").toString, "screen data bytes")

    // Check if amount of colors data exceeds maximum allowed value:
    if (colors.get().length > MultiColour.size("colors"))
      throw new InvalidImageDataSliceException("MultiColour", colors.get().length.toString, MultiColour.size("colors").toString, "colour data bytes")
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
