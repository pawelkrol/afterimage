package org.c64.attitude.Afterimage
package Format.Config

/** Memory configuration of Art Studio file format.
  *
  * @constructor create a new `ArtStudio` memory configuration
  */
case class ArtStudio() extends HiRes {

  /** Memory offset of the bitmap data bytes. */
  val bitmap = ArtStudio.bitmap

  /** Memory offset of the screen data bytes. */
  val screen = ArtStudio.screen
}

/** Parameter values for [[org.c64.attitude.Afterimage.Format.Config.ArtStudio]] memory configuration. */
object ArtStudio {

  /** Memory offset of Art Studio bitmap data bytes. */
  val bitmap = 0x0000

  /** Memory offset of Art Studio screen data bytes. */
  val screen = 0x1f40
}
