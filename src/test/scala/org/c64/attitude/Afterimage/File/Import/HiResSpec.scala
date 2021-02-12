package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus
import ij.process.ColorProcessor

import org.c64.attitude.Afterimage.Helper.Images.{ emptyImagePlus, testImagePlus }

import org.scalatest.funspec.AnyFunSpec

class HiResSpec extends AnyFunSpec {

  private def fileImportHiRes(backgroundColour: Option[Byte] = None) = File.Import.HiRes(backgroundColour)

  describe("HiRes mode file converter") {
    it("imports a black HiRes image") {

    val blackHiResImage = Mode.HiRes(
      bitmap = Array.fill[Byte](Mode.HiRes.size("bitmap"))(0x00),
      screen = Array.fill[Byte](Mode.HiRes.size("screen"))(0x00)
    )

      val convertedHiResImage = fileImportHiRes().convert(emptyImagePlus()).asInstanceOf[Mode.HiRes]
      assert(convertedHiResImage === blackHiResImage)
    }

    describe("Import without an arbitrary background colour") {
      it("assigns two most common colours to each screen character") {

        val black = 0x00
        val grey = 0x0f
        val screen = ((grey << 4) | black).toByte

        val convertedHiResImage = fileImportHiRes().convert(testImagePlus).asInstanceOf[Mode.HiRes]
        assert(convertedHiResImage.pixel(0,0) === grey)
        assert(convertedHiResImage.pixel(7,7) === black)
        assert(convertedHiResImage.screen.get(0, 0) == screen)
      }
    }

    describe("Import with an arbitrary background colour") {
      it("assigns one most common colour and an explicit background colour to each screen character") {

        val black = 0x00
        val white = 0x01
        val screen = ((black << 4) | white).toByte

        val convertedHiResImage = fileImportHiRes(Some(white.toByte)).convert(testImagePlus).asInstanceOf[Mode.HiRes]
        assert(convertedHiResImage.pixel(0,0) === white)
        assert(convertedHiResImage.pixel(7,7) === black)
        assert(convertedHiResImage.screen.get(0, 0) == screen)
      }
    }

    it("fails to import an image with an invalid width") {

      val width = Mode.CBM.width - 1
      val height = Mode.CBM.height

      val ip = new ColorProcessor(width, height)
      val img = new ImagePlus("HiRes Image Converter", ip)

      val error = intercept[RuntimeException] { fileImportHiRes().convert(img) }
      assert(error.getMessage === "Invalid image size encountered: 319x200 (expected image size was: 320x200)")
    }

    it("fails to import an image with an invalid height") {

      val width = Mode.CBM.width
      val height = Mode.CBM.height - 1

      val ip = new ColorProcessor(width, height)
      val img = new ImagePlus("HiRes Image Converter", ip)

      val error = intercept[RuntimeException] { fileImportHiRes().convert(img) }
      assert(error.getMessage === "Invalid image size encountered: 320x199 (expected image size was: 320x200)")
    }
  }
}
