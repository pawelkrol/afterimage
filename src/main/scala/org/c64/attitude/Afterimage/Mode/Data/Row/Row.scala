package org.c64.attitude.Afterimage
package Mode.Data.Row

import Util.Util.getOrderedNumbers

/** Single image row data abstraction providing direct access to the bitmap and screen colours. */
trait Row {

  /** Indicates how many bits of data count as a single pixel (for example, in hires mode 1 pixel, whereas in multicolour mode 2 pixels) */
  def pixelSize: Byte

  /** Defines pixel mask covering a single bitmap data pixel which is required by some pixel-level calculations. */
  private def pixelMask = (1 to pixelSize).foldLeft(0x00)((mask, bit) => (mask >> 1) | 0x80)

  /** Specifies current (maximum) width of row data counted as a number of 8x8 character columns. */
  val width: Int

  private def pixels(bitmapData: Array[Byte], pixelLine: Int) =
    (0 to width * 0x08 / pixelSize - 1).map(pixel => {
      val bitmapOffset = (pixelLine & 0x07) + (pixel * pixelSize & 0xfff8)
      val bitMask = pixelMask >> (pixel * pixelSize & 0x0007)

      (bitmapData(bitmapOffset) & bitMask)
    })

  private def firstPixelOffset(bitmapData: Array[Byte], pixelLine: Int): Int =
    pixels(bitmapData, pixelLine).indexWhere(pixel => pixel > 0)

  private def lastPixelOffset(bitmapData: Array[Byte], pixelLine: Int): Int =
    width * 0x08 / pixelSize - 1 - pixels(bitmapData, pixelLine).reverse.indexWhere(pixel => pixel > 0)

  /** Returns offset of the first pixel set in a given bitmap single row data.
    *
    * @param bitmapData up to 320 (divisible by 8) raw bytes of bitmap data
    */
  def firstPixelOffset(bitmapData: Array[Byte]): Int =
    (0 to 7).map(pixelLine => firstPixelOffset(bitmapData, pixelLine)).reduce(
      (a, b) => if (a == -1) b else if (b == -1) a else a.min(b)
    )

  /** Returns offset of the last pixel set in a given bitmap single row data.
    *
    * @param bitmapData up to 320 (divisible by 8) raw bytes of bitmap data
    */
  def lastPixelOffset(bitmapData: Array[Byte]): Int = {
    val totalNumberOfPixels = width * 0x08 / pixelSize

    (0 to 7).map(pixelLine => lastPixelOffset(bitmapData, pixelLine)).reduce(
      (a, b) => if (a == totalNumberOfPixels) b else if (b == totalNumberOfPixels) a else a.max(b)
    )
  }

  /** Returns left margin column as a character offset of the first non-empty 8x8 bits area. */
  def leftMargin = 0x00

  /** Returns left margin column as an offset of the first pixel set. */
  def leftPixelMargin = 0x0000

  /** Returns right margin column as a character offset of the last non-empty 8x8 bits area. */
  def rightMargin = 0x27

  /** Returns right margin column as an offset of the last pixel set. */
  def rightPixelMargin = 0x013f

  /** Returns number of 8x8 characters in the entire row. */
  def charWidth = rightMargin - leftMargin + 1

  /** Returns number of pixels (with defined "width")  in the entire row. */
  def pixelWidth =  rightPixelMargin - leftPixelMargin + 1

  /** Returns full bitmap data of the entire row (with padded values to achieve full-screen width).
    *
    * @param fill byte to fill in padded values with
    */
  def fullBitmap(fill: Byte): Array[Byte]

  /** Returns full screen data of the entire row (with padded values to achieve full-screen width).
    *
    * @param fill byte to fill in padded values with
    */
  def fullScreen(fill: Byte): Array[Byte]

  /** Returns a single separating comment line for use in methods generating source code strings. */
  val commentLine = ";" + Array.fill(98){"-"}.mkString("") + "\n"

  /** Returns source code string which is ready to be compiled by "dreamass" and correctly recognized by a new "Attitude" diskmag engine.
   *
   * @param fullRow when flag is set the entire (full-width) row data will be included in the output
   */
  def toCode(label: String, fullRow: Boolean): String

  /** Returns serialized row data which can be exported and used conveniently by any external program.
   *
   * @param fullRow when flag is set the entire (full-width) row data will be included in the output
   */
  def serialize(fullRow: Boolean): String
}

/** Static methods for cutting out image rows of a given size from the original bitmaps/screens/colours. */
object Row {

  /** Default length of a single image row bitmap data. */
  val bitmapRowSize = 0x0140

  /** Default length of a single image row screen data. */
  val screenRowSize = 0x28

  /** Default length of a single image row colours data. */
  val colorsRowSize = 0x28

  private def validateRowIndex(index: Int): Unit = {
    require(
      index >= 0 && index <= 24,
      "Invalid data row index requested: got %d, but expected an integer between 0 and 24".format(index)
    )
  }

  /** Returns the new bitmap row data composed from cutting out selected row of a given size from the original image.
    *
    * @param index 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param bitmap array of bytes used as a source of data for constructing the new bitmap row
    * @param rowSize data size of a single bitmap row in the original image
    */
  def getBitmapRow(index: Int, bitmap: Array[Byte], rowSize: Int = bitmapRowSize): Array[Byte] = {
    validateRowIndex(index)
    bitmap.slice(index * rowSize, (index + 1) * rowSize)
  }

  /** Returns the new screen row data composed from cutting out selected row of a given size from the original image.
    *
    * @param index 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param screen array of bytes used as a source of data for constructing the new screen row
    * @param rowSize data size of a single screen row in the original image
    */
  def getScreenRow(index: Int, screen: Array[Byte], rowSize: Int = screenRowSize): Array[Byte] = {
    validateRowIndex(index)
    screen.slice(index * rowSize, (index + 1) * rowSize)
  }

  /** Returns the new colors row data composed from cutting out selected row of a given size from the original image.
    *
    * @param index 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param colors array of bytes used as a source of data for constructing the new colors row
    * @param rowSize data size of a single colors row in the original image
    */
  def getColorsRow(index: Int, colors: Array[Byte], rowSize: Int = colorsRowSize): Array[Byte] = {
    validateRowIndex(index)
    colors.slice(index * rowSize, (index + 1) * rowSize)
  }

  /** Returns the new bitmap rows data composed from cutting out selected rows of a given size from the original image.
    *
    * @param from initial 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param to closing 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param bitmap array of bytes used as a source of data for constructing the new bitmap rows
    * @param rowSize data size of a single bitmap row in the original image
    */
  def getBitmapRows(from: Int, to: Int, bitmap: Array[Byte], rowSize: Int = bitmapRowSize): Array[Byte] = {
    validateRowIndex(from)
    validateRowIndex(to)
    val (indexFrom, indexTo) = getOrderedNumbers(from, to)
    bitmap.slice(indexFrom * rowSize, (indexTo + 1) * rowSize)
  }

  /** Returns the new screen rows data composed from cutting out selected rows of a given size from the original image.
    *
    * @param from initial 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param to closing 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param screen array of bytes used as a source of data for constructing the new screen rows
    * @param rowSize data size of a single screen row in the original image
    */
  def getScreenRows(from: Int, to: Int, screen: Array[Byte], rowSize: Int = screenRowSize): Array[Byte] = {
    validateRowIndex(from)
    validateRowIndex(to)
    val (indexFrom, indexTo) = getOrderedNumbers(from, to)
    screen.slice(indexFrom * rowSize, (indexTo + 1) * rowSize)
  }

  /** Returns the new colors rows data composed from cutting out selected rows of a given size from the original image.
    *
    * @param from initial 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param to closing 8x8 character row index of an image (expected to be an integer between 0 and 24)
    * @param colors array of bytes used as a source of data for constructing the new colors rows
    * @param rowSize data size of a single colors row in the original image
    */
  def getColorsRows(from: Int, to: Int, colors: Array[Byte], rowSize: Int = colorsRowSize): Array[Byte] = {
    validateRowIndex(from)
    validateRowIndex(to)
    val (indexFrom, indexTo) = getOrderedNumbers(from, to)
    colors.slice(indexFrom * rowSize, (indexTo + 1) * rowSize)
  }

  /** Converts data to source code string which is ready to be compiled by "dreamass".
    *
    * @param data an array of bytes to be converted
    * @param numValues maximum number of byte values included in a single source code row (defaults to 16)
    * @param indent initial indentation for each row of source code data (defaults to 16)
    */
  def convertDataToCode(
    data: Array[Byte],
    numValues: Int = 16,
    indent: Int = 16
  ) = {
    require(data.length > 0, "No data has been provided: cannot process empty data")

    val dataLengthRounded = ((data.length - 1).toFloat / numValues).toInt * numValues + numValues
    val numLines = dataLengthRounded / numValues

    val indentation = Array.fill(indent){" "}.mkString("")

    (0 to numLines - 1).map(line => {
      val from = line * numValues
      val until = ((line + 1) * numValues).min(data.length)

      indentation + ".db " + data.slice(from, until).map(byte => "$%02x".format(byte)).mkString(",")
    }).mkString("\n") + "\n"
  }
}
