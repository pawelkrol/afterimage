package com.github.pawelkrol.Afterimage
package Mode.Data.Row

import Mode.Data.Bitmap
import Mode.MultiColour

/** Single multicolour row (counted as a number of 8x8 character columns) data abstraction providing direct access to the raw multicolour image bytes.
  *
  * @constructor create a new row of image data with up to 40 8x8 character columns
  * @param bitmap up to 320 raw bytes of image bitmap data
  * @param screen up to 40 raw bytes of image screen data
  * @param colors up to 40 raw bytes of image colors data
  * @param bckgrd single byte of background colour
  * @param pixelWidth indicates how many bits of data count as a single pixel (for example, in hires mode 1 pixel, whereas in multicolour mode 2 pixels)
  */
class MultiColourRow(
  val bitmap: Array[Byte],
  val screen: Array[Byte],
  val colors: Array[Byte],
  val bckgrd: Byte
) extends Row {

  /** Indicates how many bits of data count as a single pixel in multicolour mode (2 pixels). */
  def pixelSize: Byte = 2

  /** Specifies current (maximum) width of row data counted as a number of 8x8 character columns. */
  val width = screen.length

  /** Returns full bitmap data of the entire row (with padded values to achieve full-screen width).
    *
    * @param fill byte to fill in padded values with (defaults to 0x00)
    */
  def fullBitmap(fill: Byte = 0x00.toByte): Array[Byte] = bitmap.padTo(Row.bitmapRowSize, fill)

  /** Returns full screen data of the entire row (with padded values to achieve full-screen width).
    *
    * @param fill byte to fill in padded values with (defaults to row background colour)
    */
  def fullScreen(fill: Byte = bckgrd): Array[Byte] = screen.padTo(Row.screenRowSize, fill)

  /** Returns full colors data of the entire row (with padded values to achieve full-screen width).
    *
    * @param fill byte to fill in padded values with
    */
  def fullColors(fill: Byte = bckgrd): Array[Byte] = colors.padTo(Row.screenRowSize, fill)

  private def fullData(fullRow: Boolean) =
    if (fullRow)
      (fullBitmap(), fullScreen(), fullColors())
    else
      (bitmap, screen, colors)

  /** Returns source code string which is ready to be compiled by "dreamass" and correctly recognized by a new "Attitude" diskmag engine.
   *
   * @param fullRow when flag is set the entire (full-width) row data will be included in the output
   */
  def toCode(label: String, fullRow: Boolean = false): String = {

    val indent = 16
    val numValues = 20

    val (bitmapData, screenData, colorsData) = fullData(fullRow)

    val indentation = Array.fill(indent){" "}.mkString("")

    commentLine +
    label + "\n" +
    commentLine +
    indentation + "; $00 - background colour (1 byte)\n" +
    indentation + ".db $%02x\n".format(bckgrd) +
    indentation + "; $01 - screen data (40 bytes)\n" +
    Row.convertDataToCode(screenData, numValues, indent) +
    indentation + "; $29 - colors data (40 bytes)\n" +
    Row.convertDataToCode(colorsData, numValues, indent) +
    indentation + "; $51 - bitmap data (320 bytes)\n" +
    Row.convertDataToCode(bitmapData, numValues, indent) +
    indentation + "; [fixed length] $191 - end\n" +
    commentLine
  }

  /** Returns serialized row data which can be exported and used conveniently by any external program.
   *
   * @param fullRow when flag is set the entire (full-width) row data will be included in the output
   */
  def serialize(fullRow: Boolean = true) = {

    val (bitmapData, screenData, colorsData) = fullData(fullRow)

    val data: Array[Byte] = width.toByte +: (bckgrd +: (screenData ++ colorsData ++ bitmapData))

    data.map(byte => "%02x".format(byte)).mkString("")
  }
}

/** Factory for [[com.github.pawelkrol.Afterimage.Mode.Data.Row.MultiColourRow]] instances. */
object MultiColourRow {

  /** Creates a new multicolour row from a MultiColour image data.
    *
    * @param bitmap up to 320 raw bytes of image bitmap data
    * @param screen up to 40 raw bytes of image screen data
    * @param colors up to 40 raw bytes of image colors data
    * @param bckgrd single byte of background colour
    */
  def apply(bitmap: Array[Byte], screen: Array[Byte], colors: Array[Byte], bckgrd: Byte) =
    new MultiColourRow(bitmap, screen, colors, bckgrd)

  /** Deserializes row data imported from an external program.
    *
    * @param data previously exported serialized row data
    */
  def inflate(data: String) = {
    val bytes = data.split("(?<=\\G.{2})").map(Integer.parseInt(_, 16).toByte)
    val width = bytes(0)
    val bckgrd = bytes(1)
    val screen = bytes.slice(2, 42)
    val colors = bytes.slice(42, 82)
    val bitmap = bytes.takeRight(320)
    new MultiColourRow(bitmap, screen, colors, bckgrd)
  }
}
