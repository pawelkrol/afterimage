package org.c64.attitude.Afterimage
package File.Type

import ij.ImagePlus
import ij.plugin.PNG_Writer
import java.io.File

import Mode.{CBM,HiRes,MultiColour}
import View.Image

/** HiRes/MultiColour image to PNG format converter.
  *
  * @constructor create a new PNG format converter
  * @param image Image object with embedded picture data and ImageJ displayer routine
  */
class PNG(
  image: Image
) {

  private def scaleOf(pic: CBM) = pic match {
    case multiColour: MultiColour => (2, 1)
    case hiRes: HiRes => (1, 1)
    case _ => throw new RuntimeException("Something went wrong...")
  }

  /** Save PNG image to file.
    *
    * @param name target file name
    * @param overwriteIfExists boolean flag indicating whether overwriting of an existing file should trigger no error
    * @param scaleFactor defines custom image scale factor to be used when rendering a PNG file (defaults to 1)
    */
  def save(name: String, overwriteIfExists: Boolean = false, scaleFactor: Int = 1): Unit = {

    PNG.writeImage(name, image.create(scaleFactor, scaleOf), overwriteIfExists)
  }
}

/** Factory for [[org.c64.attitude.Afterimage.File.Type.PNG]] instances. */
object PNG {

  private val writer = new PNG_Writer()

  /** Saves images directly in PNG format using the ImageIO classes.
    *
    * @param name target file name
    * @param imagePlus rendered image data
    * @param overwriteIfExists boolean flag indicating whether overwriting of an existing file should trigger no error
    */
  def writeImage(name: String, imagePlus: ImagePlus, overwriteIfExists: Boolean = false): Unit = {
    if (!overwriteIfExists)
      if ((new File(name)).exists())
        throw new FileAlreadyExistsException(name)

    writer.writeImage(imagePlus, name, 0)
  }

  /** Creates a new PNG format converter from a [[org.c64.attitude.Afterimage.View.Image]] picture instance.
    *
    * @param image Image object with embedded picture data and ImageJ displayer routine
    */
  def apply(image: Image) = new PNG(image)
}
