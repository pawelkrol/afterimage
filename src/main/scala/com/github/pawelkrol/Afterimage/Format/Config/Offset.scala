package com.github.pawelkrol.Afterimage
package Format.Config

/** Memory configuration of any file format used to extract image data from file data bytes. */
trait Offset {

  /** Memory offset of the bitmap data bytes. */
  val bitmap: Int
}
