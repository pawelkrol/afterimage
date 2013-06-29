package org.c64.attitude.Afterimage
package View

import ij.ImagePlus
import ij.process.ColorProcessor

import Colour.{Colour,Palette}
import Mode.{CBM,HiRes,MultiColour}

/** Image displayer based on the `ImageJ` library. */
trait Shower {

  /** Picture to be used as a source of data for generating image preview. */
  val picture: CBM

  /** Colour palette to be used when displaying a picture. */
  val palette: Palette

  private val scale = picture match {
    case multiColour: MultiColour => (4, 2)
    case hiRes: HiRes => (2, 2)
    case _ => throw new RuntimeException("Something went wrong...")
  }

  /** Generates the pixel data of the image.
    *
    * @return `ImagePlus` object which is capable of generating image preview
    */
  def create(): ImagePlus = {
    val width = picture.width
    val height = picture.height

    val ip = new ColorProcessor(width, height)

    for (x <- 0 to width - 1) {
      for (y <- 0 to height - 1) {
        val colour = picture.pixel(x, y)
        val pixel = palette.pixel(colour)
        ip.putPixel(x, y, pixel)
      }
    }

    // Scale the image viewport by the given factor:
    val (xScale, yScale) = scale

    new ImagePlus("Afterimage Preview", ip.resize(width * xScale, height * yScale))
  }
}
