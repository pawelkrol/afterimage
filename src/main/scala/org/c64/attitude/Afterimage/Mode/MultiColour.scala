package org.c64.attitude.Afterimage
package Mode

import Mode.Data.{Bitmap,Screen}
import Mode.Data.Row.{MultiColourRow,Row}

/** MultiColour image data abstraction providing convenient access to the bitmap and screen colours.
  *
  * @constructor create a new `MultiColour` image data
  * @param bitmap plain hi-resolution bitmap data of an image
  * @param screen screen portion of colours data of an image
  * @param colors colors portion of colours data of an image
  * @param border optional single byte of image border colour
  * @param bckgrd single byte of image background colour
  */
case class MultiColour(
  val bitmap: Bitmap,
  val screen: Screen,
  val colors: Screen,
  val border: Option[Byte],
  val bckgrd: Byte
) extends CBM {

  /** An actual pixel width of this MultiColour image. */
  val width = MultiColour.maxWidth

  /** An actual pixel height of this MultiColour image. */
  val height = MultiColour.maxHeight

  /** Image pixel width rounded up to the right margin of an 8x8 character. */
  val widthRounded = {
    val addition = if ((width & 0x03) != 0) 0x04 else 0x00
    (width & 0xfc) + addition
  }

  /** Image pixel height rounded up to the bottom margin of an 8x8 character. */
  val heightRounded = {
    val addition = if ((height & 0x07) != 0) 0x08 else 0x00
    (height & 0xf8) + addition
  }

  /** An actual char width of this MultiColour image. */
  val numCharCols = widthRounded / 4

  /** An actual char height of this MultiColour image. */
  val numCharRows = heightRounded / 8

  validate()

  /** Validates consistency of an object instance data. */
  def validate(): Unit = {
    require(bitmap.get().length == MultiColour.size("bitmap"), "Invalid %s image data".format(imageMode))
    require(screen.get().length == MultiColour.size("screen"), "Invalid %s image data".format(imageMode))
    require(colors.get().length == MultiColour.size("colors"), "Invalid %s image data".format(imageMode))
  }

  private def pixelBits(x: Int, y: Int) = bitmap.getPixels(2 * x, y, 2)

  private def colour(x: Int, y: Int, bits: Byte): Byte = {
    val colour: Int = bits match {
      case 0x00 => bckgrd
      case 0x01 => (screen(x, y) & 0xf0) >> 0x04
      case 0x02 => screen(x, y) & 0x0f
      case 0x03 => colors(x, y) & 0x0f
      case _ => throw new RuntimeException("Something went wrong...")
    }
    colour.toByte
  }

  /** Returns the C64 colour of the pixel at [x,y].
    *
    * @param x X coordinate of a requested pixel
    * @param y Y coordinate of a requested pixel
    */
  def pixel(x: Int, y: Int) = {
    validatePixelCoordinates(x, y)

    val bits = pixelBits(x, y)

    val (screenX, screenY) = pixelCoordinatesToScreen(x * 0x02, y)
    colour(screenX, screenY, bits)
  }

  /** Returns a new MultiColourSlice instance with truncated contents of the image.
   *
   * @param fromY first screen row of a rectangular selection area considered as a number of 8x8 character blocks
   * @param toY last screen row a rectangular selection area (inclusive) considered as a number of 8x8 character blocks
   */
  def slice(fromY: Int, toY: Int) = {

    val minY = 0
    val maxY = numCharRows - 1

    require(
      fromY >= minY && toY <= maxY,
      "Invalid slice selection area in MultiColour mode requested: got %s, but expected rows between 0 and %s".format(
        "[%d,%d]".format(fromY, toY),
        maxY
      )
    )

    val screenHeight = toY - fromY + 1

    val newWidth = widthRounded * 0x02
    val newHeight = screenHeight * 0x08

    val y = fromY * 0x08

    val newBitmap = bitmap.slice(0, y, newWidth, newHeight)
    val newScreen = screen.slice(0, fromY, numCharCols, screenHeight)
    val newColors = colors.slice(0, fromY, numCharCols, screenHeight)

    MultiColourSlice(newBitmap, newScreen, newColors, border, bckgrd, widthRounded, newHeight)
  }

  /** Returns image data as an array of multicolour rows ([[org.c64.attitude.Afterimage.Mode.Data.Row.MultiColourRow]] objects).
    *
    * Note that fetching rows data from an image slice will also always return an array of full rows with 40 columns length each!
    */
  def rows = (0 to numCharRows - 1).map(row =>
    MultiColourRow(
      Row.getBitmapRow(row, bitmap.get(), widthRounded * 0x02),
      Row.getScreenRow(row, screen.get(), numCharCols),
      Row.getColorsRow(row, colors.get(), numCharCols),
      bckgrd
    )
  ).toArray

  def canEqual(that: Any) = that.isInstanceOf[MultiColour]

  override def equals(other: Any) = other match {
    case that: MultiColour =>
      (that canEqual this) && (this.bitmap == that.bitmap) && (this.screen == that.screen) && (this.colors == that.colors) && (this.border == that.border) && (this.bckgrd == that.bckgrd)
    case _ =>
      false
  }
}

/** Factory for [[org.c64.attitude.Afterimage.Mode.MultiColour]] instances. */
object MultiColour {

  /** Maximum possible pixel width of a MultiColour image. */
  val maxWidth = 160

  /** Maximum possible pixel height of a MultiColour image. */
  val maxHeight = 200

  /** Default size of raw byte arrays comprising a MultiColour image. */
  val size = Map[String,Int](
    "bitmap" -> 0x1f40,
    "screen" -> 0x03e8,
    "colors" -> 0x03e8
  )

  /** Creates an empty MultiColour image. */
  def apply(): MultiColour =
    MultiColour(
      bitmap = Array.fill(MultiColour.size("bitmap")){0x00},
      screen = Array.fill(MultiColour.size("screen")){0x00},
      colors = Array.fill(MultiColour.size("colors")){0x00},
      bckgrd = 0x00
    )

  /** Creates a new MultiColour image with a given bitmap data and screen, background and border colours.
    *
    * @param bitmap array of 8000 raw bytes with image bitmap data
    * @param screen array of 1000 raw bytes with image screen data
    * @param colors array of 1000 raw bytes with image colors data
    * @param bckgrd single byte of image background colour
    * @param border single byte of image border colour
    */
  def apply(bitmap: Array[Byte], screen: Array[Byte], colors: Array[Byte], bckgrd: Byte, border: Byte): MultiColour =
    MultiColour(
      Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows),
      Screen(screen, Screen.maxCols, Screen.maxRows),
      Screen(colors, Screen.maxCols, Screen.maxRows),
      Some(border),
      bckgrd
    )

  /** Creates a new MultiColour image with a given bitmap data and screen and background colours.
    *
    * @param bitmap array of 8000 raw bytes with image bitmap data
    * @param screen array of 1000 raw bytes with image screen data
    * @param colors array of 1000 raw bytes with image colors data
    * @param bckgrd single byte of image background colour
    */
  def apply(bitmap: Array[Byte], screen: Array[Byte], colors: Array[Byte], bckgrd: Byte): MultiColour =
    MultiColour(
      Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows),
      Screen(screen, Screen.maxCols, Screen.maxRows),
      Screen(colors, Screen.maxCols, Screen.maxRows),
      None,
      bckgrd
    )
}

