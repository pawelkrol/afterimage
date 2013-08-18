package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus
import ij.process.ColorProcessor

import java.awt.Color.black

import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

class HiResTest extends Suite with ShouldMatchers {

  private val fileImportHiRes = File.Import.HiRes()

  private val blackHiResImage = Mode.HiRes(
    bitmap = Array.fill[Byte](Mode.HiRes.size("bitmap"))(0x00.toByte),
    screen = Array.fill[Byte](Mode.HiRes.size("screen"))(0x00.toByte)
  )

  private val emptyImagePlus = {

    val width = Mode.CBM.width
    val height = Mode.CBM.height

    val ip = new ColorProcessor(width, height)
    ip.setColor(black)
    ip.fill()
    new ImagePlus("HiRes Image Converter", ip)
  }

  def testFileImportHiResInitialization {

    fileImportHiRes should equal (File.Import.HiRes())
  }

  def testFileImportHiResConversion {

    val convertedHiResImage = fileImportHiRes.convert(emptyImagePlus).asInstanceOf[Mode.HiRes]
    convertedHiResImage should equal (blackHiResImage)
  }

  def testFileImportWithInvalidWidth {

    val width = Mode.CBM.width - 1
    val height = Mode.CBM.height

    val ip = new ColorProcessor(width, height)
    val img = new ImagePlus("HiRes Image Converter", ip)

    intercept[RuntimeException] {
      fileImportHiRes.convert(img)
    }
  }

  def testFileImportWithInvalidHeight {

    val width = Mode.CBM.width
    val height = Mode.CBM.height - 1

    val ip = new ColorProcessor(width, height)
    val img = new ImagePlus("HiRes Image Converter", ip)

    intercept[RuntimeException] {
      fileImportHiRes.convert(img)
    }
  }
}
