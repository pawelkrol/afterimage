package com.github.pawelkrol.Afterimage
package File.Import

import ij.ImagePlus

import Colour.Palette

/** HiRes mode file converter.
  *
  * @constructor create a new HiRes mode file converter
  * @param backgroundColour an arbitrary background colour to appear accross an entire target image data (0x00..0x0f)
  * @param palette a default colour palette to be used during conversion process
  */
case class HiRes(
  backgroundColour: Option[Byte] = None,
  palette: Palette = Palette("default")
) extends Mode {

  /** Converts image pieces into Afterimage's `HiRes` Mode.
    *
    * @param from a two-dimensional array of 8x8 pixels large pieces of image data
    * @return a new instance of [[com.github.pawelkrol.Afterimage.Mode.HiRes]] class
    */
  def createImage(from: Seq[Seq[Piece]]): Mode.CBM = {

    // Convert each piece into a 8x8 char data separately:
    val chars = from.map(pieces => {
      pieces.map(piece => {
        piece.toHiRes(palette, backgroundColour)
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

    Mode.HiRes(
      bitmap = bitmapData,
      screen = screenData
    )
  }

  def canEqual(that: Any) = that.isInstanceOf[HiRes]

  override def equals(other: Any) = other match {
    case that: HiRes =>
      (that canEqual this) && (this.palette == that.palette)
    case _ =>
      false
  }
}
