package org.c64.attitude.Afterimage
package Mode

import Data.Row
import Mode.Data.Bitmap
import Mode.Data.Row.Row

/** Generic CBM image data. */
trait CBM {

  /** Plain hi-resolution bitmap data of the image. */
  val bitmap: Bitmap

  /** Optional single byte of image border colour. */
  val border: Option[Byte]

  /** An actual pixel width of the image. */
  val width: Int

  /** An actual pixel height of the image. */
  val height: Int

  /** Image pixel width rounded up to the right margin of an 8x8 character. */
  val widthRounded: Int

  /** Image pixel height rounded up to the bottom margin of an 8x8 character. */
  val heightRounded: Int

  /** An actual char width of the image. */
  val numCharRows: Int

  /** An actual char height of the image. */
  val numCharCols: Int

  /** An actual image mode. */
  val imageMode = this.getClass.getName.split("\\.").last

  /** Validates consistency of an object instance data. */
  def validate(): Unit

  /** Validates whether given pixel coordinates are located within the image.
    *
    * @param x X coordinate of the pixel
    * @param y Y coordinate of the pixel
    */
  def validatePixelCoordinates(x: Int, y: Int): Unit = {
    if (x < 0 || x >= width || y < 0 || y >= height)
      throw new InvalidPixelCoordinates(
        "[%s,%s]".format(x, y),
        "[%s,%s]".format(width, height)
      )
  }

  /** Returns the C64 colour of the pixel at [x,y].
    *
    * @param x X coordinate of a requested pixel
    * @param y Y coordinate of a requested pixel
    */
  def pixel(x: Int, y: Int): Byte

  /** Converts given pixel coordinates into screen coordinates.
    *
    * @param x X coordinate of a requested pixel
    * @param y Y coordinate of a requested pixel
    * @return tuple with a pair of calculated screen coordinates
    */
  def pixelCoordinatesToScreen(x: Int, y: Int): Tuple2[Int, Int] = {
    val screenX = (x & 0xfff8) >> 3
    val screenY = (y & 0xf8) >> 3
    (screenX, screenY)
  }
}

/** Generic CBM image properties. */
object CBM {

  /** Total pixel width of a CBM image. */
  val width = 320

  /** Total pixel height of a CBM image. */
  val height = 200
}
