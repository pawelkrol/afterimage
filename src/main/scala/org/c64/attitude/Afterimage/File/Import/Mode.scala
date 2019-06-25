package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus

import Colour.{Colour, Palette}

/** PC image to CBM mode file converter. */
trait Mode {

  /** Converts image pieces into concrete Afterimage's `Mode`.
    *
    * @param from a two-dimensional array of 8x8 pixels large pieces of image data
    * @return a new instance of a class derived from `Mode`
    */
  def createImage(from: Seq[Seq[Piece]]): Mode.CBM

  /** Splits picture into 8x8 bit pieces of image data.
    *
    * @param img ImagePlus object
    * @return a two-dimensional array of 8x8 pixels large pieces of image data
    */
  def splitInto8x8Pieces(img: ImagePlus): Seq[Seq[Piece]] =

    (0x00 until Mode.CBM.width / 0x08).map(x => {
      (0x00 until Mode.CBM.height / 0x08).map(y => {

        val colours =
          (0x00 until 0x08).map(inX => {
            (0x00 until 0x08).map(inY => {

              val totalX = x * 0x08 + inX
              val totalY = y * 0x08 + inY

              val pixel = img.getPixel(totalX, totalY).map(_.toByte)
              val (red, green, blue, alpha) = (pixel(0), pixel(1), pixel(2), pixel(3))

              Colour(red, green, blue, None)
            })
          })

        Piece(colours)
      })
    })

  /** Validates and converts ImagePlus object into one of Afterimage's `Mode`.
    *
    * @param img ImagePlus object
    * @return a new instance of a class derived from `Mode`
    */
  def convert(img: ImagePlus): Mode.CBM = {

    validate(img)

    val pieces = splitInto8x8Pieces(img)

    createImage(from = pieces)
  }

  private def validate(img: ImagePlus): Unit = {

    if (img.getWidth != Mode.CBM.width || img.getHeight != Mode.CBM.height)
      throw new RuntimeException("Invalid image size encountered: %dx%d (expected image size was: %dx%d)".format(
        img.getWidth, img.getHeight, Mode.CBM.width, Mode.CBM.height
      ))
  }
}
