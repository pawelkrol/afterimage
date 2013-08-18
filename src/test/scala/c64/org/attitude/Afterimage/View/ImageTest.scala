package org.c64.attitude.Afterimage
package View

import ij.ImagePlus
import org.scalatest.{Ignore,Suite}

import Colour.Palette
import File.File
import Mode.{HiRes,MultiColour}

class ImageTest extends Suite {

  def setupEmptyTestMultiColourData() = {
    MultiColour(
      Array.fill(MultiColour.size("bitmap")){0x00.toByte},
      Array.fill(MultiColour.size("screen")){0x00.toByte},
      Array.fill(MultiColour.size("colors")){0x00.toByte},
      0x00.toByte,
      0x00.toByte
    )
  }

  def testImageCreate {
    val picture = setupEmptyTestMultiColourData()
    val palette = Palette("default")
    assert(Image(picture, palette).isInstanceOf[Image])
  }

  def testImageShower {
    val picture = setupEmptyTestMultiColourData()
    val palette = Palette("default")
    val image = Image(picture, palette).create()
    assert(image.isInstanceOf[ImagePlus])
  }

  @Ignore
  def testImageHiResShowerView {

    val name = getClass.getResource("/images/niemanazwy-bimber.hpi").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    Image(picture.asInstanceOf[HiRes].invert, palette).show()
  }

  @Ignore
  def testImageMultiColourShowerView {

    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    Image(picture, palette).show()
  }

  @Ignore
  def testImageHiResSliceShowerView {

    val name = getClass.getResource("/images/desolate-deev.aas").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    val slice = picture.asInstanceOf[HiRes].slice(96, 72, 184, 88)
    Image(slice, palette).show()
  }

  @Ignore
  def testImageMultiColourSliceShowerView {

    val name = getClass.getResource("/images/frighthof83-yazoo.ocp").toString.replace("file:", "")
    val picture = File.load(name)
    val palette = Palette("default")
    val slice = picture.asInstanceOf[MultiColour].slice(0, 7)
    Image(slice, palette).show()
  }
}
