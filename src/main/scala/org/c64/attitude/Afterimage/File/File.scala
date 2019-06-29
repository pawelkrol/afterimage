package org.c64.attitude.Afterimage
package File

import ij.ImagePlus
import ij.process.ImageConverter

import Format.{AdvancedArtStudio,ArtStudio,Config,FacePainter,HiResBitmap,KoalaPainter}
import Memory.Address
import Mode.CBM

/** Factory for instances derived from [[org.c64.attitude.Afterimage.Mode.CBM]]. */
object File {

  /** Loads image data from file.
    *
    * @param name CBM image file name
    * @return a new instance of a class derived from `Mode` with the image mode determined by the file data
    */
  def load(name: String): CBM = {

    val file = new java.io.File(name)
    val source = scala.io.Source.fromFile(file)(scala.io.Codec.ISO8859).toArray

    val load = source.take(2).map(_.toByte)
    val addr = Address(load.head, load.last);

    val data = source.drop(2).map(_.toByte)

    try {
      return FacePainter(addr, data).extractData()
    }
    catch {
      case _: Throwable =>
    }

    try {
      return KoalaPainter(addr, data).extractData()
    }
    catch {
      case _: Throwable =>
    }

    try {
      return AdvancedArtStudio(addr, data).extractData()
    }
    catch {
      case _: Throwable =>
    }

    try {
      return ArtStudio(addr, data).extractData()
    }
    catch {
      case _: Throwable =>
    }

    try {
      return HiResBitmap(addr, data).extractData()
    }
    catch {
      case _: Throwable =>
    }

    throw new IllegalArgumentException("Invalid image data: unrecognised graphics format")
  }

  /** Imports image data from file.
    *
    * @param name PC image file name
    * @param mode image converter to use while processing image data
    * @return a new instance of a class derived from `Mode` with the image mode determined by a converter used to import data
    */
  def convert(name: String, mode: Import.Mode): CBM = {

    // Construct an ImagePlus object from a file specified by a path:
    val imagePlus = new ImagePlus(name)

    // Convert an ImagePlus object to RGB:
    val imageConverter = new ImageConverter(imagePlus)
    imageConverter.convertToRGB()

    // Convert an ImagePlus object to one of Afterimage's CBM modes:
    mode.convert(imagePlus)
  }
}
