package org.c64.attitude.Afterimage
package Helper

import ij.ImagePlus
import ij.plugin.CanvasResizer
import ij.process.ColorProcessor

import java.awt.Color
import java.awt.Color.{ black, white }

import org.c64.attitude.Afterimage.Colour.Colour

object Images {

  def emptyImagePlus(backgroundColour: Color = black) = {

    val width = Mode.CBM.width
    val height = Mode.CBM.height

    val ip = new ColorProcessor(width, height)
    ip.setColor(backgroundColour)
    ip.fill()
    new ImagePlus("", ip)
  }

  val testImagePlus = {

    val grey = Colour(0x95.toByte, 0x95.toByte, 0x95.toByte, Some("red"))
    val black = Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, Some("black"))

    val imagePlus = emptyImagePlus(white)

    val ip = imagePlus.getProcessor

    val pixels = Array(
      Array(grey,  grey,  grey,  grey,  grey,  grey,  grey,  black),
      Array(grey,  grey,  grey,  grey,  grey,  grey,  black, black),
      Array(grey,  grey,  grey,  grey,  grey,  black, black, black),
      Array(grey,  grey,  grey,  grey,  black, black, black, black),
      Array(grey,  grey,  grey,  black, black, black, black, black),
      Array(grey,  grey,  black, black, black, black, black, black),
      Array(grey,  black, black, black, black, black, black, black),
      Array(black, black, black, black, black, black, black, black)
    ).map(_.map(_.pixel))

    for (x <- 0 until 8)
      for (y <- 0 until 8)
        ip.putPixel(x, y, pixels(y)(x))

    imagePlus
  }

  private def preview(imagePlus: ImagePlus, width: Int, height: Int, scaleFactor: Int = 1): Unit = {

    val ip = resize(imagePlus, width, height).getProcessor

    val targetWidth = ip.getWidth * scaleFactor
    val targetHeight = ip.getHeight * scaleFactor

    (new ImagePlus("", ip.resize(targetWidth, targetHeight).getBufferedImage)).show
  }

  private def resize(imagePlus: ImagePlus, width: Int, height: Int) = {

    new ImagePlus("", (new CanvasResizer).expandImage(imagePlus.getProcessor, width, height, 0, 0).getBufferedImage)
  }
}
