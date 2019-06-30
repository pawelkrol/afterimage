package org.c64.attitude.Afterimage
package Format.Config

/** Memory configuration of Amica Paint file format.
  *
  * @constructor create a new `AmicaPaint` memory configuration
  */
case class AmicaPaint() extends MultiColour {

  /** Memory offset of the bitmap data bytes. */
  val bitmap = AmicaPaint.bitmap

  /** Memory offset of the screen data bytes. */
  val screen = AmicaPaint.screen

  /** Memory offset of the colors data bytes. */
  val colors = AmicaPaint.colors

  /** Memory offset of the background colour. */
  val bckgrd = AmicaPaint.bckgrd

  /** Memory offset of the color rotation table bytes. */
  val colorRotationTable = AmicaPaint.colorRotationTable
}

/** Parameter values for [[org.c64.attitude.Afterimage.Format.Config.AmicaPaint]] memory configuration. */
object AmicaPaint {

  /** Memory offset of Amica Paint bitmap data bytes. */
  val bitmap = 0x0000

  /** Memory offset of Amica Paint screen data bytes. */
  val screen = 0x1f40

  /** Memory offset of Amica Paint colors data bytes. */
  val colors = 0x2328

  /** Memory offset of Amica Paint background colour. */
  val bckgrd = 0x2710

  /** Memory offset of Amica Paint colour rotation table bytes. */
  val colorRotationTable = 0x2711

  /** Default size of extra raw byte arrays comprising an Amica Paint image. */
  val size = Map[String,Int](
    "colorRotationTable" -> 0x0100
  )
}
