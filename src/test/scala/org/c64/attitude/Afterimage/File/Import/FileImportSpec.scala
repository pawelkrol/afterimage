package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus
import ij.process.ImageConverter

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

import Colour.Palette
import View.Image

class FileImportSpec extends AnyFreeSpec with Matchers {

  private val name = getClass.getResource("/images/bandzior-cuc.png").toString.replace("file:", "")

  "convert to hires instance" in {

    val mode = HiRes()
    val picture = File.File.convert(name, mode)

    assert(picture.isInstanceOf[Mode.HiRes])

    picture.pixel(x = 0, y = 28) must equal (0x0f)
    picture.pixel(x = 0, y = 29) must equal (0x00)
    picture.pixel(x = 0, y = 42) must equal (0x00)
    picture.pixel(x = 0, y = 43) must equal (0x01)
  }

  "create CBM image from pieces" in {

    val img = new ImagePlus(name)
    val mode = HiRes()

    val imageConverter = new ImageConverter(img)
    imageConverter.convertToRGB()

    val pieces: Seq[Seq[Piece]] = mode.splitInto8x8Pieces(img)
    val picture = mode.createImage(from = pieces)

    picture.pixel(x = 0, y = 28) must equal (0x0f)
    picture.pixel(x = 0, y = 29) must equal (0x00)
    picture.pixel(x = 0, y = 42) must equal (0x00)
    picture.pixel(x = 0, y = 43) must equal (0x01)
  }

  "converted image hires shower view" ignore {

    val mode = HiRes()
    val picture = File.File.convert(name, mode)
    val palette = Palette("default")
    Image(picture.asInstanceOf[Mode.HiRes], palette).show()
    0 must equal (1)
  }
}
