package org.c64.attitude.Afterimage
package File.Import

import scala.math.Ordering.Double.TotalOrdering

import Colour.{Colour, Palette}

/** Small image piece (8x8 pixels) abstraction.
  *
  * @constructor create an image data representation equivalent to a single CBM 8x8 char area
  * @param pixels a two-dimensional 8x8 array of pixel colours
  */
class Piece(
  pixels: Seq[Seq[Colour]]
) {

  /** Returns a two-dimensional 8x8 array of pixel colours that was used to initialize this object. */
  def getPixels = pixels

  private def getEverySecondPixels = pixels.grouped(2).toList.map(_.head)

  private def findTopNColours(palette: Palette, n: Int, backgroundColour: Option[Byte] = None): List[Byte] = {

    // For multicolour pictures (n == 4) fetch every second pixel from piece data:
    val analysedPixels =
      if (n == 4)
        getEverySecondPixels
      else
        getPixels

    // Find number of occurrences of each colour within this piece:
    val occurrences = analysedPixels.flatten.foldLeft[Map[Int,Int]](Map[Int,Int]())((ranking, colour) => {

      // Find the closest colour from the provided palette, as only they matter:
      val colourC64 = palette.get(colour)

      if (ranking.contains(colourC64)) {
        val count = ranking(colourC64) + 1
        ranking - (colourC64) + ((colourC64, count))
      }
      else
        ranking + ((colourC64, 1))
    })

    // Sort calculated occurrences of each colour within this piece:
    val sortedOccurrences = occurrences.toList.sortWith((a, b) => a._2 > b._2).map(_._1.toByte)

    // If background colour has been provided, put it into the front of a sorted list:
    val allSortedOccurrences =
      backgroundColour match {
        case Some(bckgrd) => {
          bckgrd +: sortedOccurrences.filter(colour => colour != bckgrd)
        }
        case None =>
          sortedOccurrences
      }

    // Return "n" most common colours from the ordered list (do it by duplicating the last
    // element "n" times in order to make sure that list contains at least "n" elements):
    (allSortedOccurrences ++ List.fill[Byte](n - 1)(allSortedOccurrences.last)).slice(0, n)
  }

  /** Converts piece data into HiRes image using provided colour palette and optional background colour.
    *
    * @param palette colour palette to be used during conversion process
    * @param backgroundColour enforce usage of a constant background colour to a target image data (0x00..0x0f)
    * @return a tuple of 8 bitmap and 1 screen data bytes
    */
  def toHiRes(palette: Palette, backgroundColour: Option[Byte] = None): Tuple2[Seq[Byte], Byte] = {

    val top2Colours = findTopNColours(palette, 2, backgroundColour)
    val (bckgrdC64, colourC64) = (top2Colours(0), top2Colours(1))

    val colour = palette(colourC64)
    val bckgrd = palette(bckgrdC64)

    // Map each colour from the 8x8 "pixels" array into the closest match from
    // the list of two top colours:
    val pixelsC64 = getPixels.map(column => {
      column.map(pixel => {
        if (colour.delta_to(pixel) <= bckgrd.delta_to(pixel))
          colourC64
        else
          bckgrdC64
      })
    })

    // Convert each row of pixels into an actual bitmap data of an 8x8 char:
    val bitmap = (0x00 until 0x08).map(x => {
      pixelsC64.foldLeft[Tuple2[Int, Int]](Tuple2[Int, Int](0x00, 0x80))((result, column) => {
        val (byte, bitMask) = result

        val newByte =
          if (column(x) == bckgrdC64)
            byte
          else
            byte | bitMask

        (newByte, bitMask >> 1)
      })._1.toByte
    })

    val screen = (colourC64 << 4) | bckgrdC64

    (bitmap, screen.toByte)
  }

  /** Converts piece data into MultiColour image using provided colour palette and background colour.
    *
    * @param palette colour palette to be used during conversion process
    * @param backgroundColour background colour of a target image data (0x00..0x0f)
    * @return a tuple of 8 bitmap, 1 screen and 1 colour data bytes
    */
  def toMultiColour(palette: Palette, backgroundColour: Byte): Tuple3[Seq[Byte], Byte, Byte] = {

    val top4Colours = findTopNColours(palette, 4, Some(backgroundColour))
    val (bckgrdC64, screenC64lo, screenC64hi, colourC64) = (top4Colours(0).toInt, top4Colours(1).toInt, top4Colours(2).toInt, top4Colours(3).toInt)

    val colour = palette(colourC64)
    val bckgrd = palette(bckgrdC64)
    val screenLo = palette(screenC64lo)
    val screenHi = palette(screenC64hi)

    // Image colours contain the list of piece colours, where each tuple consists of:
    // 1. C64 colour
    // 2. Colour object
    // 3. Bit values inside an entire byte (e.g., "01010101")
    val imageColours = List[Tuple3[Int, Colour, Int]](
      (bckgrdC64, bckgrd, 0x00),
      (screenC64hi, screenHi, 0x55),
      (screenC64lo, screenLo, 0xaa),
      (colourC64, colour, 0xff)
    )

    // Map each colour from the 8x8 "pixels" array into the closest match from
    // the list of four top colours:
    val pixelsColours = getEverySecondPixels.map(column => {
      column.map(pixel => {
        // Find which of the most popular colours (colour, bckgrd, screenLo,
        // screenHi) is the closest one to this currently analysed pixel:
        imageColours.minBy(_._2.delta_to(pixel))(TotalOrdering)
      })
    })

    // Convert each row of pixel colours into an actual bitmap data of an 8x8 char:
    val bitmap = (0x00 until 0x08).map(x => {
      pixelsColours.foldLeft[Tuple2[Int, Int]](Tuple2[Int, Int](0x00, 0xc0))((result, column) => {
        val (byte, bitMask) = result
        val pixelColour = column(x)
        val (colourC64, colour, bits) = pixelColour

        val bitsToSet = bits & bitMask

        val newByte = byte | bitsToSet

        (newByte, bitMask >> 2)
      })._1.toByte
    })

    val screen = (screenC64hi << 4) | screenC64lo
    val colors = colourC64

    (bitmap, screen.toByte, colors.toByte)
  }

  override def toString = {

    val colours = (0 until 8).map(x => {
      (0 until 8).map(y => {
        "[x=%d,y=%d] => %s".format(x, y, pixels(x)(y))
      }).mkString("\n  ")
    }).mkString("\n  ")

    "Piece(pixels = \n  %s\n)".format(colours)
  }

  def canEqual(that: Any) = that.isInstanceOf[Piece]

  override def equals(other: Any) = other match {
    case that: Piece =>
      (that canEqual this) && (this.getPixels == that.getPixels)
    case _ =>
      false
  }
}

/** Factory for [[org.c64.attitude.Afterimage.File.Import.Piece]] instances. */
object Piece {

  val emptyPixelArray =
    Seq.fill[Seq[Colour]](0x08)(
      Seq.fill[Colour](0x08)(
        Colour(0x00, 0x00, 0x00, None)
      )
    )

  /** Creates an empty `Piece` of 8x8 pixels image data. */
  def apply() = new Piece(emptyPixelArray)

  /** Creates a new `Piece` of 8x8 pixels image data provided a two-dimensional 8x8 colour array. */
  def apply(pixels: Seq[Seq[Colour]]) = new Piece(pixels)
}
