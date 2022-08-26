package com.github.pawelkrol.Afterimage
package Format.Config

/** Memory configuration of Advanced Art Studio file format.
  *
  * @constructor create a new `AdvancedArtStudio` memory configuration
  */
case class AdvancedArtStudio() extends MultiColour {

  /** Memory offset of the bitmap data bytes. */
  val bitmap = AdvancedArtStudio.bitmap

  /** Memory offset of the screen data bytes. */
  val screen = AdvancedArtStudio.screen

  /** Memory offset of the colors data bytes. */
  val colors = AdvancedArtStudio.colors

  /** Memory offset of the background colour. */
  val bckgrd = AdvancedArtStudio.bckgrd
}

/** Parameter values for [[com.github.pawelkrol.Afterimage.Format.Config.AdvancedArtStudio]] memory configuration. */
object AdvancedArtStudio {

  /** Memory offset of Advanced Art Studio bitmap data bytes. */
  val bitmap = 0x0000

  /** Memory offset of Advanced Art Studio screen data bytes. */
  val screen = 0x1f40

  /** Memory offset of Advanced Art Studio colors data bytes. */
  val colors = 0x2338

  /** Memory offset of Advanced Art Studio background colour. */
  val bckgrd = 0x2329
}
