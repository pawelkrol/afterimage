package org.c64.attitude.Afterimage
package Format.Config

/** Memory configuration of Koala Painter file format.
  *
  * @constructor create a new `KoalaPainter` memory configuration
  */
case class KoalaPainter() extends MultiColour {

  /** Memory offset of the bitmap data bytes. */
  val bitmap = KoalaPainter.bitmap

  /** Memory offset of the screen data bytes. */
  val screen = KoalaPainter.screen

  /** Memory offset of the colors data bytes. */
  val colors = KoalaPainter.colors

  /** Memory offset of the background colour. */
  val bckgrd = KoalaPainter.bckgrd
}

/** Parameter values for [[org.c64.attitude.Afterimage.Format.Config.KoalaPainter]] memory configuration. */
object KoalaPainter {

  /** Memory offset of Koala Painter bitmap data bytes. */
  val bitmap = 0x0000

  /** Memory offset of Koala Painter screen data bytes. */
  val screen = 0x1f40

  /** Memory offset of Koala Painter colors data bytes. */
  val colors = 0x2328

  /** Memory offset of Koala Painter background colour. */
  val bckgrd = 0x2710
}
