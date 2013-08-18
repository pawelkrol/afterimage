package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus
import ij.process.ImageConverter

import org.scalatest.Ignore
import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

import Colour.Palette
import View.Image

class FileImport extends Suite with ShouldMatchers {

  private val name = getClass.getResource("/images/bandzior-cuc.png").toString.replace("file:", "")

  def testConvertToHiResInstance {

    val mode = HiRes()
    val picture = File.File.convert(name, mode)

    assert(picture.isInstanceOf[Mode.HiRes])

    picture.pixel(x = 0, y = 28) should equal (0x0f)
    picture.pixel(x = 0, y = 29) should equal (0x00)
    picture.pixel(x = 0, y = 42) should equal (0x00)
    picture.pixel(x = 0, y = 43) should equal (0x01)
  }

  def testCreateCBMImageFromPieces {

    val img = new ImagePlus(name)
    val mode = HiRes()

    val imageConverter = new ImageConverter(img)
    imageConverter.convertToRGB()

    val pieces: Seq[Seq[Piece]] = mode.splitInto8x8Pieces(img)
    val picture = mode.createImage(from = pieces)

    picture.pixel(x = 0, y = 28) should equal (0x0f)
    picture.pixel(x = 0, y = 29) should equal (0x00)
    picture.pixel(x = 0, y = 42) should equal (0x00)
    picture.pixel(x = 0, y = 43) should equal (0x01)
  }

  @Ignore
  def testConvertedImageHiResShowerView {

    val mode = HiRes()
    val picture = File.File.convert(name, mode)
    val palette = Palette("default")
    Image(picture.asInstanceOf[Mode.HiRes], palette).show()
  }
}
