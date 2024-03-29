package com.github.pawelkrol.Afterimage
package Format.Config

/** Memory configuration of HiRes Bitmap file format.
  *
  * @constructor create a new `HiResBitmap` memory configuration
  */
case class HiResBitmap() extends HiRes {

  /** Memory offset of the bitmap data bytes. */
  val bitmap = HiResBitmap.bitmap
}

/** Parameter values for [[com.github.pawelkrol.Afterimage.Format.Config.HiResBitmap]] memory configuration. */
object HiResBitmap {

  /** Memory offset of HiRes Bitmap bitmap data bytes. */
  val bitmap = 0x0000
}
