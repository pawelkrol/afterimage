package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus
import ij.process.ColorProcessor

import java.awt.Color.black

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class MultiColourSpec extends AnyFreeSpec with Matchers {

  private val fileImportMultiColour = File.Import.MultiColour(backgroundColour = 0x00)

  private val blackMultiColourImage = Mode.MultiColour(
    bitmap = Array.fill[Byte](Mode.MultiColour.size("bitmap"))(0x00.toByte),
    screen = Array.fill[Byte](Mode.MultiColour.size("screen"))(0x00.toByte),
    colors = Array.fill[Byte](Mode.MultiColour.size("colors"))(0x00.toByte),
    bckgrd = 0x00.toByte
  )

  private val emptyImagePlus = {

    val width = Mode.CBM.width
    val height = Mode.CBM.height

    val ip = new ColorProcessor(width, height)
    ip.setColor(black)
    ip.fill()
    new ImagePlus("MultiColour Image Converter", ip)
  }

  "file import multicolour initialization" in {

    fileImportMultiColour must equal (File.Import.MultiColour(backgroundColour = 0x00))
  }

  "file import multicolour conversion" in {

    val convertedMultiColourImage = fileImportMultiColour.convert(emptyImagePlus).asInstanceOf[Mode.MultiColour]
    convertedMultiColourImage must equal (blackMultiColourImage)
  }
}
