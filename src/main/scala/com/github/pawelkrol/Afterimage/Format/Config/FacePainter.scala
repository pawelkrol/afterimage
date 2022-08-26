package com.github.pawelkrol.Afterimage
package Format.Config

/** Memory configuration of Face Painter file format.
  *
  * @constructor create a new `FacePainter` memory configuration
  */
case class FacePainter() extends MultiColour {

  /** Memory offset of the bitmap data bytes. */
  val bitmap = FacePainter.bitmap

  /** Memory offset of the screen data bytes. */
  val screen = FacePainter.screen

  /** Memory offset of the colors data bytes. */
  val colors = FacePainter.colors

  /** Memory offset of the border colour. */
  val border = FacePainter.border

  /** Memory offset of the background colour. */
  val bckgrd = FacePainter.bckgrd
}

/** Parameter values for [[com.github.pawelkrol.Afterimage.Format.Config.FacePainter]] memory configuration. */
object FacePainter {

  /** Memory offset of Face Painter bitmap data bytes. */
  val bitmap = 0x0000

  /** Memory offset of Face Painter screen data bytes. */
  val screen = 0x1f40

  /** Memory offset of Face Painter colors data bytes. */
  val colors = 0x2328

  /** Memory offset of Face Painter border colour. */
  val border = 0x2710

  /** Memory offset of Face Painter background colour. */
  val bckgrd = 0x2711
}
