package org.c64.attitude.Afterimage
package Mode.Data.Row

import Mode.HiRes

/** Single hires row (counted as a number of 8x8 character columns) data abstraction providing direct access to the raw hires image bytes.
  *
  * @constructor create a new row of image data with up to 40 8x8 character columns
  * @param bitmap up to 320 raw bytes of image bitmap data
  * @param screen up to 40 raw bytes of image screen data
  * @param pixelWidth indicates how many bits of data count as a single pixel (for example, in hires mode 1 pixel, whereas in hires mode 2 pixels)
  */
class HiResRow(
  val bitmap: Array[Byte],
  val screen: Array[Byte]
) extends Row {

  /** Indicates how many bits of data count as a single pixel in hires mode (1 pixel). */
  def pixelSize: Byte = 1

  /** Specifies current (maximum) width of row data counted as a number of 8x8 character columns. */
  val width = screen.length

  private def pixelOffsetToCharOffset(pixelOffset: Int) = (pixelOffset * pixelSize & 0xfff8) >> 3

  /** Returns left margin column as a character offset of the first non-empty 8x8 bits area. */
  override def leftMargin = pixelOffsetToCharOffset(firstPixelOffset(bitmap))

  /** Returns left margin column as an offset of the first pixel set. */
  override def leftPixelMargin = firstPixelOffset(bitmap)

  /** Returns right margin column as a character offset of the last non-empty 8x8 bits area. */
  override def rightMargin = pixelOffsetToCharOffset(lastPixelOffset(bitmap))

  /** Returns right margin column as an offset of the last pixel set. */
  override def rightPixelMargin = lastPixelOffset(bitmap)

  /** Returns full bitmap data of the entire row (with padded values to achieve full-screen width).
    *
    * @param fill byte to fill in padded values with (defaults to 0x00)
    */
  def fullBitmap(fill: Byte = 0x00.toByte): Array[Byte] = bitmap.padTo(Row.bitmapRowSize, fill)

  /** Returns full screen data of the entire row (with padded values to achieve full-screen width).
    *
    * @param fill byte to fill in padded values with (defaults to 0xbc)
    */
  def fullScreen(fill: Byte = 0xbc.toByte): Array[Byte] = screen.padTo(Row.screenRowSize, fill)

  private def fullData(fullRow: Boolean) =
    if (fullRow)
      (fullBitmap(), fullScreen())
    else
      (bitmap, screen)

  /** Returns source code string which is ready to be compiled by "dreamass" and correctly recognized by a new "Attitude" diskmag engine.
   *
   * @param fullRow when flag is set the entire (full-width) row data will be included in the output
   */
  def toCode(label: String, fullRow: Boolean = false): String = {
    val indent = 16
    val numValues = 20

    val (bitmapData, screenData) = fullData(fullRow)

    val indentation = Array.fill(indent){" "}.mkString("")

    commentLine +
    label + "\n" +
    commentLine +
    indentation + "; $00 - width (1 byte)\n" +
    indentation + ".db $%02x\n".format(width) +
    indentation + "; $01 - screen data (%d bytes)\n".format(width) +
    Row.convertDataToCode(screenData, numValues, indent) +
    indentation + "; $%02x - bitmap data (%d bytes)\n".format(width + 0x01, width * 0x08) +
    Row.convertDataToCode(bitmapData, numValues, indent) +
    indentation + "; [variable length] $%02x - end\n".format(width * 0x09 + 0x01) +
    commentLine
  }

  /** Returns the new single hires row composed from the original row with a horizontally shifted content.
    *
    * @param offset value of horizontal data shift counted as a number of 8x8 character columns (positive for shifting to the right, and negative for shifting to the left)
    * @param fillScreen fill value of newly created empty screen bytes (defaults to 0x00)
    * @param fillBitmap fill value of newly created empty bitmap bytes (defaults to 0x00)
    */
  def shift(offset: Int, fillScreen: Byte = 0x00, fillBitmap: Byte = 0x00) = {
    val (newBitmap, newScreen) =
      if (offset < 0) {
        val newBMP = bitmap.takeRight((width + offset) * 0x08) ++ Array.fill((-offset * 0x08).min(width * 0x08)){fillBitmap}
        val newSCR = screen.takeRight(width + offset) ++ Array.fill((-offset).min(width)){fillScreen}
        (newBMP, newSCR)
      }
      else {
        val newBMP = Array.fill((offset * 0x08).min(width * 0x08)){fillBitmap} ++ bitmap.take((width - offset) * 0x08)
        val newSCR = Array.fill((offset).min(width)){fillScreen} ++ screen.take(width - offset)
        (newBMP, newSCR)
      }

    HiResRow(newBitmap, newScreen)
  }

  /** Returns serialized row data which can be exported and used conveniently by any external program.
   *
   * @param fullRow when flag is set the entire (full-width) row data will be included in the output
   */
  def serialize(fullRow: Boolean = true) = {

    val (bitmapData, screenData) = fullData(fullRow)

    val data: Array[Byte] = width.toByte +: (screenData ++ bitmapData)

    data.map(byte => "%02x".format(byte)).mkString("")
  }
}

/** Factory for [[org.c64.attitude.Afterimage.Mode.Data.Row.HiResRow]] instances. */
object HiResRow {

  /** Creates a new hires row from a HiRes image data.
    *
    * @param bitmap up to 320 raw bytes of image bitmap data
    * @param screen up to 40 raw bytes of image screen data
    */
  def apply(
    bitmap: Array[Byte],
    screen: Array[Byte]
  ) = new HiResRow(bitmap, screen)

  /** Deserializes row data imported from an external program.
    *
    * @param data previously exported serialized row data
    */
  def inflate(data: String) = {
    val bytes = data.split("(?<=\\G.{2})").map(Integer.parseInt(_, 16).toByte)
    val width = bytes(0)
    val bitmap = bytes.takeRight(width * 8)
    val screen = bytes.slice(1, width + 1)
    new HiResRow(bitmap, screen)
  }
}
