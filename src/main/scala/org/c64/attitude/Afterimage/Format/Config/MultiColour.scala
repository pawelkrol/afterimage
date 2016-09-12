package org.c64.attitude.Afterimage
package Format.Config

/** Memory configuration of multicolour file format used to extract image data from file data bytes. */
trait MultiColour extends Offset {

  /** Memory offset of the screen data bytes. */
  val screen: Int

  /** Memory offset of the colors data bytes. */
  val colors: Int

  /** Memory offset of the background colour. */
  val bckgrd: Int
}
