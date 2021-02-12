package org.c64.attitude.Afterimage
package View

import ij.ImagePlus

import org.scalatest.freespec.AnyFreeSpec

import Colour.Palette
import File.File
import Mode.{HiRes, MultiColour}

class ImageSpec extends AnyFreeSpec {

  def setupEmptyTestMultiColourData() = {
    MultiColour(
      Array.fill(MultiColour.size("bitmap")){0x00.toByte},
      Array.fill(MultiColour.size("screen")){0x00.toByte},
      Array.fill(MultiColour.size("colors")){0x00.toByte},
      0x00.toByte,
      0x00.toByte
    )
  }

  "image create" in {
    val picture = setupEmptyTestMultiColourData()
    val palette = Palette("default")
    assert(Image(picture, palette).isInstanceOf[Image])
  }

  "image shower" in {
    val picture = setupEmptyTestMultiColourData()
    val palette = Palette("default")
    val image = Image(picture, palette).create()
    assert(image.isInstanceOf[ImagePlus])
  }

  "image hires shower view" ignore {

    val name = getClass.getResource("/images/niemanazwy-bimber.hpi").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    Image(picture.asInstanceOf[HiRes].invert(), palette).show()
  }

  "image multicolour shower view" ignore {

    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    Image(picture, palette).show()
  }

  "image hires slice shower view" ignore {

    val name = getClass.getResource("/images/desolate-deev.aas").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    val slice = picture.asInstanceOf[HiRes].slice(96, 72, 184, 88)
    Image(slice, palette).show()
  }

  "image multicolour slice shower view" ignore {

    val name = getClass.getResource("/images/frighthof83-yazoo.ocp").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    val slice = picture.asInstanceOf[MultiColour].slice(0, 7)
    Image(slice, palette).show()
  }
}
