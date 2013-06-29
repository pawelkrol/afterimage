package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus

import Colour.Palette

/** MultiColour mode file converter.
  *
  * @constructor create a new MultiColour mode file converter
  * @param backgroundColour Background colour of a target image data (0x00..0x0f)
  * @param palette a default colour palette to be used during conversion process
  */
case class MultiColour(
  backgroundColour: Byte,
  palette: Palette = Palette("default")
) extends Mode {

  /** Converts image pieces into Afterimage's `MultiColour` Mode.
    *
    * @param from a two-dimensional array of 8x8 pixels large pieces of image data
    * @return a new instance of [[org.c64.attitude.Afterimage.Mode.MultiColour]] class
    */
  def createImage(from: Seq[Seq[Piece]]): Mode.CBM = {

    // Convert each piece into a 8x8 char data separately:
    val chars = from.map(pieces => {
      pieces.map(piece => {
        piece.toMultiColour(palette, backgroundColour)
      })
    })

    // Flatten chars array, so that it is first ordered by columns, and then by rows:
    val sortedChars = (0 until chars(0).length).flatMap(number => {
      chars.map(column => {
        column(number)
      })
    })

    val bitmapData = sortedChars.flatMap(_._1).toArray
    val screenData = sortedChars.map(_._2).toArray
    val colorsData = sortedChars.map(_._3).toArray

    Mode.MultiColour(
      bitmap = bitmapData,
      screen = screenData,
      colors = colorsData,
      bckgrd = backgroundColour
    )
  }

  def canEqual(that: Any) = that.isInstanceOf[MultiColour]

  override def equals(other: Any) = other match {
    case that: MultiColour =>
      (that canEqual this) && (this.backgroundColour == that.backgroundColour) && (this.palette == that.palette)
    case _ =>
      false
  }
}
