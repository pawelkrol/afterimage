package org.c64.attitude.Afterimage
package Mode.Data

/** Plain hi-resolution bitmap data of an image.
  *
  * @constructor create a new bitmap data
  * @param data an array of bytes
  * @param cols bitmap width counted as a number of 8x8 character columns
  * @param rows bitmap height counted as a number of 8x8 character rows
  */
case class Bitmap(data: Array[Byte], cols: Int, rows: Int) {

  require(
    cols >= 1 && cols <= Bitmap.maxCols && rows >= 1 && rows <= Bitmap.maxRows,
    "Invalid bitmap data size: got %s, but expected anything between 1x1 and 40x25 with row and column values counted as occurences of 8x8 character areas".format(
      "%dx%d".format(cols, rows)
    )
  )

  require(
    data.length == cols * rows * Bitmap.bytesPerChar,
    "Invalid bitmap data length: got %d, but expected %d bytes".format(
      data.length,
      cols * rows * Bitmap.bytesPerChar
    )
  )

  private lazy val pixels = {

    val width = cols * 8
    val height = rows * 8

    (0 to width - 1).map(x => {
      (0 to height - 1).map(y => {

        val bitmapOffset = (y & 0x07) + ((y & 0xf8) >> 3) * width + (x & 0xfff8)
        val bitMask = 0x80 >> (x & 0x0007)

        (data(bitmapOffset) & bitMask) != 0
      })
    })
  }

  /** Returns the entire bitmap data as an array of bytes. */
  def get() = data

  /** Returns a boolean flag indicating if a pixel at a given position is set or not.
    *
    * @param x X coordinate of the pixel
    * @param y Y coordinate of the pixel
    */
  def isPixelSet(x: Int, y: Int) = pixels(x)(y)

  /** Returns an indicated number of pixel values starting at a given position.
    *
    * @param x X coordinate of the first pixel
    * @param y Y coordinate of the first pixel
    * @param numBits number of up to 8 subsequent pixels to test, which are located on the samy Y line
    */
  def getPixels(x: Int, y: Int, numBits: Int) = {

    require(
      numBits >= 1 && numBits <= 8,
      "Invalid number of pixels requested: got %d, but expected an integer between 1 and 8".format(numBits)
    )

    (0 to numBits - 1).foldLeft(0x00)((pixels, bit) => {
      val pixel = if (isPixelSet(x + bit, y)) 0x01 else 0x00
      (pixels << 1) + pixel
    }).toByte
  }

  private def horizontalShift(x: Int, pixels: Seq[Seq[Boolean]], fill: Boolean) =
    if (x < 0)
      pixels.drop(-x) ++ Seq.fill((-x).min(cols * 8)){Seq.fill(rows * 8){fill}}
    else
      Seq.fill((x).min(cols * 8)){Seq.fill(rows * 8){fill}} ++ pixels.dropRight(x)

  private def verticalShift(y: Int, pixels: Seq[Seq[Boolean]], fill: Boolean) =
    pixels.map(col => {
      if (y < 0)
        col.drop(-y) ++ Seq.fill((-y).min(rows * 8)){fill}
      else
        Seq.fill(y.min(rows * 8)){fill} ++ col.dropRight(y)
    })

  private def getData(pixels: Seq[Seq[Boolean]], width: Int, height: Int, cols: Int, rows: Int) = {

    (0 to rows - 1).map(row => {
      (0 to cols - 1).map(col => {
        (0 to 7).map(byte => {
          (0 to 7).foldLeft(0x00)((bits, bit) => {
            val x = col * 8 + bit
            val y = row * 8 + byte
            val pixel = if (x >= width || y >= height)
              0x00
            else
              if (pixels(x)(y)) 0x01 else 0x00
            (bits << 1) + pixel
          }).toByte
        })
      }).flatten
    }).flatten.toArray
  }

  /** Returns the new bitmap data composed from the original image with a shifted content.
    *
    * @param x value of horizontal data shift (positive for shifting to the right, and negative for shifting to the left)
    * @param y value of vertical data shift (positive for shifting to the bottom, and negative for shifting to the top)
    * @param fill optional fill value of newly created empty bits (defaults to a cleared bit)
    */
  def shift(x: Int, y: Int, fill: Boolean = false) = {

    val horizontalShiftPixels = horizontalShift(x, pixels, fill)
    val verticalShiftPixels = verticalShift(y, horizontalShiftPixels, fill)

    val newData = getData(verticalShiftPixels, cols * 8, rows * 8, cols, rows)

    new Bitmap(newData, cols, rows)
  }

  /** Returns the new bitmap data composed from cutting out a slice of the original image.
   *
   * @param x X coordinate of the top-left corner of a rectangular selection area
   * @param y Y coordinate of the top-left corner of a rectangular selection area
   * @param newWidth total width of a rectangular selection area in number of pixels
   * @param newHeight total height of a rectangular selection area in number of pixels
   */
  def slice(x: Int, y: Int, newWidth: Int, newHeight: Int) = {

    val shiftedBitmap = shift(-x, -y)

    val cols = (newWidth.toDouble / 8).ceil.toInt
    val rows = (newHeight.toDouble / 8).ceil.toInt

    val data = getData(shiftedBitmap.pixels, newWidth, newHeight, cols, rows)

    new Bitmap(data, cols, rows)
  }

  def canEqual(that: Any) = that.isInstanceOf[Bitmap]

  override def equals(other: Any) = other match {
    case that: Bitmap =>
      (that canEqual this) && (this.data.toList == that.data.toList) && (this.cols == that.cols) && (this.rows == that.rows)
    case _ =>
      false
  }
}

/** Factory for [[org.c64.attitude.Afterimage.Mode.Data.Bitmap]] instances. */
object Bitmap {

  /** Maximum possible number of 8x8 character columns of a Bitmap image data. */
  val maxCols = 40

  /** Maximum possible number of 8x8 character rows of a Bitmap image data. */
  val maxRows = 25

  private val bytesPerChar = 8

  private val maxSize = maxCols * maxRows * bytesPerChar

  /** Creates a default (empty) bitmap of a maximum possible size. */
  def apply(): Bitmap =
    Bitmap(Array.fill(maxSize){0x00}, maxCols, maxRows)

  /** Creates a default (empty) bitmap of a given size. */
  def apply(cols: Int, rows: Int): Bitmap =
    Bitmap(Array.fill(cols * rows * bytesPerChar){0x00}, cols, rows)
}
