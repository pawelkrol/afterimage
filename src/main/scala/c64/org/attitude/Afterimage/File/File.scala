package org.c64.attitude.Afterimage
package File

import Format.{AdvancedArtStudio,ArtStudio,Config,FacePainter,HiResBitmap,KoalaPainter}
import Memory.Address
import Mode.Mode

/** Factory for instances derived from [[org.c64.attitude.Afterimage.Mode.Mode]]. */
object File {

  /** Loads image data from file.
    *
    * @param name CBM image file name
    * @return a new instance of any class derived from `Mode` with the image mode determined by the file data
    */
  def load(name: String): Mode = {

    val file = new java.io.File(name)
    val source = scala.io.Source.fromFile(file)(scala.io.Codec.ISO8859).toArray

    val load = source.take(2).map(_.toByte)
    val addr = Address(load.head, load.last);

    val data = source.drop(2).map(_.toByte)

    try {
      return FacePainter(addr, data).extractData()
    }
    catch {
      case _ =>
    }

    try {
      return KoalaPainter(addr, data).extractData()
    }
    catch {
      case _ =>
    }

    try {
      return AdvancedArtStudio(addr, data).extractData()
    }
    catch {
      case _ =>
    }

    try {
      return ArtStudio(addr, data).extractData()
    }
    catch {
      case _ =>
    }

    try {
      return HiResBitmap(addr, data).extractData()
    }
    catch {
      case _ =>
    }

    throw new InvalidImageDataException("recognized graphics")
  }
}
