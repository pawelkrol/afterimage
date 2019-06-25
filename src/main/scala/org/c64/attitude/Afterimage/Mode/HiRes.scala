package org.c64.attitude.Afterimage
package Mode

import Mode.Data.{Bitmap,Screen}
import Mode.Data.Row.{HiResRow,Row}
import Util.Util

/** HiRes image data abstraction providing convenient access to the bitmap and screen colours.
  *
  * @constructor create a new `HiRes` image data
  * @param bitmap plain hi-resolution bitmap data of an image
  * @param screen optional screen colours data of an image
  * @param border optional single byte of image border colour
  */
case class HiRes(
  val bitmap: Bitmap,
  val screen: Option[Screen],
  val border: Option[Byte]
) extends CBM {

  /** An actual pixel width of this HiRes image. */
  val width = HiRes.maxWidth

  /** An actual pixel height of this HiRes image. */
  val height = HiRes.maxHeight

  /** Image pixel width rounded up to the right margin of an 8x8 character. */
  val widthRounded = Util.roundSizeToChar(width)

  /** Image pixel height rounded up to the bottom margin of an 8x8 character. */
  val heightRounded = Util.roundSizeToChar(height)

  /** An actual char width of this HiRes image. */
  val numCharCols = widthRounded / 8

  /** An actual char height of this HiRes image. */
  val numCharRows = heightRounded / 8

  validate()

  /** Validates consistency of an object instance data. */
  def validate(): Unit = {
    if (bitmap.get().length != HiRes.size("bitmap"))
      throw new InvalidImageModeDataException(imageMode)

    screen match {
      case Some(scr) =>
        if (scr.get().length != HiRes.size("screen"))
          throw new InvalidImageModeDataException(imageMode)
      case None =>
    }
  }

  private def colour(x: Int, y: Int, isPixelSet: Boolean): Byte = {
    val colour = screen match {
      case Some(data) =>
        if (isPixelSet) (data(x, y) & 0xf0) >> 4 else data(x, y) & 0x0f
      case None =>
        if (isPixelSet) HiRes.defaultColour else HiRes.defaultBackground
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

    val isPixelSet: Boolean = bitmap.isPixelSet(x, y)

    val (screenX, screenY) = pixelCoordinatesToScreen(x, y)
    colour(screenX, screenY, isPixelSet)
  }

  /** Returns a new HiRes image with all pixels of bitmap data bits inverted. */
  def invert() = {
    val inverted = bitmap.get.map(byte => (byte ^ 0xff).toByte)

    new HiRes(Bitmap(inverted, numCharCols, numCharRows), screen, border)
  }

  private def sliceOffsetsToScreen(x: Int, y: Int, width: Int, height: Int): Tuple4[Int, Int, Int, Int] = {

    val screenX = (x & 0xfff8) >> 3
    val screenY = (y & 0xf8) >> 3
    val screenWidth = (width & 0xfff8) >> 3
    val screenHeight = (height & 0xfff8) >> 3

    (screenX, screenY, screenWidth, screenHeight)
  }

  /** Returns a new HiResSlice instance with truncated contents of the image.
   *
   * @param x X coordinate of the top-left corner of a rectangular selection area
   * @param y Y coordinate of the top-left corner of a rectangular selection area
   * @param newWidth total width of a rectangular selection area in number of pixels
   * @param newHeight total height of a rectangular selection area in number of pixels
   */
  def slice(x: Int, y: Int, newWidth: Int, newHeight: Int) = {

    // Check that top-left corner of a slice is within an image data area:
    validatePixelCoordinates(x, y)

    // Check that bottom-right corner of a slice is within an image data area:
    validatePixelCoordinates(x + newWidth - 1, y + newHeight - 1)

    screen match {
      case Some(scr) => {
        // Validate start and end position alignment to char boundaries:
        if (x % 8 > 0 || y % 8 > 0)
          throw new InvalidSliceCoordinatesException("[%d,%d]".format(x, y), "values aligned with a 8x8 char boundaries")

        // Validate newWidth and newHeight values:
        if (x + newWidth > width || y + newHeight > height)
          throw new InvalidSliceDimensionsException("[%d,%d]".format(newWidth, newHeight), "[%d,%d]".format(width, height))

        val newBitmap = bitmap.slice(x, y, newWidth, newHeight)
        val newScreen = screen match {
          case Some(data) => {
            val (scrX, scrY, scrNewWidth, scrNewHeight) = sliceOffsetsToScreen(x, y, newWidth, newHeight)
            Some(data.slice(scrX, scrY, scrNewWidth, scrNewHeight))
          }
          case None => None
        }

        HiResSlice(newBitmap, newScreen, border, newWidth, newHeight)
      }
      case None => {
        val newBitmap = bitmap.slice(x, y, newWidth, newHeight)

        HiResSlice(
          newBitmap,
          None,
          border,
          Util.roundSizeToChar(newWidth),
          Util.roundSizeToChar(newHeight)
        )
      }
    }
  }

  /** A fallback method returning default screen colour values (1000 bytes filled with 0xbc). */
  val emptyScreen = Array.fill(numCharRows * numCharCols){HiRes.defaultScreenValue}

  /** Returns image data as an array of hires rows ([[org.c64.attitude.Afterimage.Mode.Data.Row.HiResRow]] objects).
    *
    * Note that fetching rows data from an image slice will also always return an array of full rows with 40 columns length each!
    */
  def rows = {
    val screenData = screen match {
      case Some(scr) => scr.get()
      case None => emptyScreen
    }

    (0 to numCharRows - 1).map(row =>
      HiResRow(
        Row.getBitmapRow(row, bitmap.get(), widthRounded),
        Row.getScreenRow(row, screenData, numCharCols)
      )
    ).toArray
  }

  def canEqual(that: Any) = that.isInstanceOf[HiRes]

  override def equals(other: Any) = other match {
    case that: HiRes =>
      (that canEqual this) && (this.bitmap == that.bitmap) && (this.screen == that.screen) && (this.border == that.border)
    case _ =>
      false
  }
}

/** Factory for [[org.c64.attitude.Afterimage.Mode.HiRes]] instances. */
object HiRes {

  /** Maximum possible pixel width of a HiRes image. */
  val maxWidth = 320

  /** Maximum possible pixel height of a HiRes image. */
  val maxHeight = 200

  /** Default size of raw byte arrays comprising a HiRes image. */
  val size = Map[String,Int](
    "bitmap" -> 0x1f40,
    "screen" -> 0x03e8
  )

  /** Default colour of a drawn pixel. */
  val defaultColour = 0x0b

  /** Default colour of a blank pixel. */
  val defaultBackground = 0x0c

  /** Default byte value of a HiRes screen data. */
  val defaultScreenValue = ((defaultColour << 4) | defaultBackground).toByte

  /** Creates an empty HiRes image. */
  def apply(): HiRes =
    HiRes(
      bitmap = Array.fill(HiRes.size("bitmap")){0x00}
    )

  /** Creates a new HiRes image with a given bitmap data.
    *
    * @param bitmap array of 8000 raw bytes with image bitmap data
    */
  def apply(bitmap: Array[Byte]): HiRes =
    HiRes(
      Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows),
      None,
      None
    )

  /** Creates a new HiRes image with a given bitmap data and border colour.
    *
    * @param bitmap array of 8000 raw bytes with image bitmap data
    * @param border single byte of image border colour
    */
  def apply(bitmap: Array[Byte], border: Byte): HiRes =
    HiRes(
      Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows),
      None,
      Some(border)
    )

  /** Creates a new HiRes image with a given bitmap data and screen and border colours.
    *
    * @param bitmap array of 8000 raw bytes with image bitmap data
    * @param screen array of 1000 raw bytes with image screen data
    * @param border single byte of image border colour
    */
  def apply(bitmap: Array[Byte], screen: Array[Byte], border: Byte): HiRes =
    HiRes(
      Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows),
      Some(Screen(screen, Screen.maxCols, Screen.maxRows)),
      Some(border)
    )

  /** Creates a new HiRes image with a given bitmap data and screen colours.
    *
    * @param bitmap array of 8000 raw bytes with image bitmap data
    * @param screen array of 1000 raw bytes with image screen data
    */
  def apply(bitmap: Array[Byte], screen: Array[Byte]): HiRes =
    HiRes(
      Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows),
      Some(Screen(screen, Screen.maxCols, Screen.maxRows)),
      None
    )
}
