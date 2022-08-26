package com.github.pawelkrol.Afterimage
package View

import ij.ImagePlus
import ij.process.{ ColorProcessor, ImageProcessor }

import Colour.{Colour,Palette}
import Mode.{CBM,HiRes,MultiColour}

/** Image displayer based on the `ImageJ` library. */
trait Shower {

  /** Picture to be used as a source of data for generating image preview. */
  val picture: CBM

  /** Colour palette to be used when displaying a picture. */
  val palette: Palette

  private def scale(pic: CBM) = pic match {
    case multiColour: MultiColour => (4, 2)
    case hiRes: HiRes => (2, 2)
    case _ => throw new RuntimeException("Something went wrong...")
  }

  /** Generates the pixel data of the image.
   *
   * @param scaleFactor defines custom image scale factor to be used when rendering a picture (defaults to 1)
   * @param scaleOf defines an additional custom image scale factor to be used when rendering a picture as a function
   *                of picture type and possibly different on X and Y axes
   * @param postProcessHook an optional callback method enabling post-processing of a rendered image before upscaling operation is applied
   *
   * @return `ImagePlus` object which is capable of generating image preview
   */
  def create(
    scaleFactor: Int = 1,
    scaleOf: (CBM) => Tuple2[Int, Int] = scale,
    postProcessHook: (ImageProcessor, CBM, Palette, Int, Int) => Unit = (ip, picture, palette, xScale, yScale) => {}
  ): ImagePlus = {
    assert(scaleFactor > 0)

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

    // Scale the image viewport by the given factor determined by the picture mode:
    val (xScale, yScale) = scaleOf(picture)

    val scaledWidth = width * xScale
    val scaledHeight = height * yScale

    val ipResized = ip.resize(scaledWidth, scaledHeight)

    postProcessHook(ipResized, picture, palette, xScale, yScale)

    // Scale the image viewport by the additional (user-defined) factor (both axes):
    val targetWidth = scaledWidth * scaleFactor
    val targetHeight = scaledHeight * scaleFactor

    new ImagePlus("Afterimage Preview", ipResized.resize(targetWidth, targetHeight))
  }
}
