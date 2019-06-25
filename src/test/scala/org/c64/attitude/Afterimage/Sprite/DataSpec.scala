package org.c64.attitude.Afterimage
package Sprite

import ij.ImagePlus
import ij.process.ImageProcessor

import org.scalatest.FreeSpec

import Colour.{ Colour, Palette }
import File.File
import Helper.Resources.{ testResourceData, testResourcePath }
import Mode.CBM
import View.Image

class DataSpec extends FreeSpec {

  private val palette = Palette("default")

  private def multiColourImage =
    Image(File.load(testResourcePath("/images/frighthof83-yazoo.fcp")), palette)

  private def hiResImage =
    Image(File.load(testResourcePath("/images/desolate-deev.aas")), palette)

  private def spriteData(offset: Int) =
    testResourceData("/images/dizzy-sprites.prg").map(_.toByte).drop(offset).take(0x3f)

  private def hiResSprite(colour: Int = 0x00, stretched: Boolean = false) =
    Data(
      data  = spriteData(0x00),
      props = HiResProps(colour, stretched, stretched, true)
    )

  private def multiColourSprite(stretched: Boolean = false) =
    Data(
      data  = spriteData(0x40),
      props = MultiProps(0x00, 0x01, 0x02, stretched, stretched, true)
    )

  private def assertPixels(image: ImagePlus, pixels: Seq[Tuple3[Int, Int, Int]]): Unit = {
    pixels.foreach({ case (x, y, expectedColour) => {
      val pixel = image.getPixel(x, y).map(_.toByte)
      val (red, green, blue, alpha) = (pixel(0), pixel(1), pixel(2), pixel(3))
      val colour = Colour(red, green, blue, None)
      assert(expectedColour === palette.get(colour))
    } })
  }

  private def rendered(image: ((ImageProcessor, CBM, Palette, Int, Int) => Unit) => ImagePlus) = image((_, _, _, _, _) => {})

  private val hiresRawData = """
    000000000000000000000000
    000000000111110000000000
    000000001000001000000000
    000000010000000100000000
    000000100000000010000000
    000000100110110010000000
    000001001110111001000000
    000001001010101001000000
    000001001110111001000000
    001110000000000000111000
    110010000000000000100110
    100010001000001000100010
    100011000111110001100010
    111101000001000001011110
    000000110000000110000000
    000000001111111000000000
    000000011100011100000000
    000000100010100010000000
    000000101010101010000000
    000001000101000101000000
    000001000001000001000000
  """

  private val multiColourRawData = """
    000000000000000000000000
    000000000000000000000000
    000000000101010000000000
    000000000101010100000000
    000000010101010100000000
    000000010101000100000000
    000000010001000101000000
    000000010101010101000000
    000000010001000101000000
    000001010101010101000000
    001101010101010101111100
    111101010101010101111100
    111100010100000101111100
    000000010101010101000000
    000000000101010100000000
    000000000000000000000000
    000000000000000000000000
    000000111100111100000000
    000000111100111100000000
    000000111111111111000000
    000000111111111111000000
  """

  "determine image pixels for hires sprite data" in {
    val sprite = hiResSprite()
    val expectedPixels = hiresRawData.split("\n").map(_.trim).filterNot(_.isEmpty).map(
      _.split("").toList.map(_ match {
        case "1" => Some(0x00, true)
        case "0" => None
      })
    )
    assert(sprite.pixels === expectedPixels)
  }

  "determine image pixels for multicolour sprite data" in {
    val sprite = multiColourSprite()
    val expectedPixels = multiColourRawData.split("\n").map(_.trim).filterNot(_.isEmpty).map(
      _.split("").grouped(2).toSeq.map(_.reduce(_ + _) match {
        case "11" => Some(0x02, true)
        case "10" => Some(0x00, true)
        case "01" => Some(0x01, true)
        case "00" => None
      }).flatMap(pixel => List(pixel, pixel))
    )
    assert(sprite.pixels === expectedPixels)
  }

  "render hires sprite data over an empty bitmap image" in {
    val image = hiResSprite(0x00).render(
      x = 0,
      y = 0
    )
    assertPixels(rendered(image), Seq(
      (0x08, 0x01, 0x06),
      (0x09, 0x01, 0x00),
      (0x0d, 0x01, 0x00),
      (0x0e, 0x01, 0x06),
      (0x0f, 0x01, 0x06)
    ))
  }

  "render multicolour sprite data over an empty bitmap image" in {
    val image = multiColourSprite().render(
      x = 0,
      y = 0
    )
    assertPixels(rendered(image), Seq(
      (0x00, 0x0a, 0x06),
      (0x01, 0x0a, 0x06),
      (0x02, 0x0a, 0x02),
      (0x03, 0x0a, 0x02),
      (0x04, 0x0a, 0x01)
    ))
  }

  "render multicolour sprite data overlayed by hires sprite data over an empty bitmap image" in {
    val image1 = multiColourSprite().render(
      x = 0,
      y = 0
    )
    val image2 = hiResSprite().renderNext(
      x = 0,
      y = 0,
      targetImage = image1
    )
    val image3 = multiColourSprite().renderNext(
      x = 24,
      y = 0,
      targetImage = image2
    )
    val image4 = hiResSprite().renderNext(
      x = 24,
      y = 0,
      targetImage = image3
    )
    assertPixels(rendered(image4), Seq(
      (0x01, 0x0a, 0x00),
      (0x02, 0x0a, 0x02),
      (0x03, 0x0a, 0x02),
      (0x04, 0x0a, 0x00),
      (0x05, 0x0a, 0x01),
      (0x16, 0x0a, 0x00),
      (0x17, 0x0a, 0x06),
      (0x18, 0x0a, 0x00),
      (0x19, 0x0a, 0x00),
      (0x1a, 0x0a, 0x02),
      (0x1b, 0x0a, 0x02),
      (0x1c, 0x0a, 0x00),
      (0x1d, 0x0a, 0x01)
    ))
  }

  "render hires sprite data over a hires bitmap image" in {
    val image = hiResSprite().render(
      x = 0,
      y = 0,
      targetImage = _ => hiResImage
    )
    assertPixels(rendered(image), Seq(
      (0x08, 0x01, 0x09),
      (0x09, 0x01, 0x00),
      (0x0d, 0x01, 0x00),
      (0x0e, 0x01, 0x09),
      (0x0f, 0x01, 0x08)
    ))
  }

  "render stretched hires sprite data over a multicolour bitmap image" in {
    val image = hiResSprite(0x01, true).render(
      x = 0,
      y = 0,
      targetImage = _ => multiColourImage
    )
    assertPixels(rendered(image), Seq(
      (0x07, 0x02, 0x00),
      (0x08, 0x02, 0x00),
      (0x09, 0x02, 0x00),
      (0x0e, 0x02, 0x00),
      (0x0f, 0x02, 0x00),
      (0x10, 0x02, 0x06),
      (0x11, 0x02, 0x06),
      (0x12, 0x02, 0x01),
      (0x0f, 0x04, 0x00),
      (0x10, 0x04, 0x01),
      (0x11, 0x04, 0x01),
      (0x12, 0x04, 0x00),
      (0x1b, 0x04, 0x0a),
      (0x1c, 0x04, 0x01),
      (0x1d, 0x04, 0x01),
      (0x1e, 0x04, 0x04)
    ))
  }

  "render multicolour sprite data over a hires bitmap image" in {
    val image = multiColourSprite().render(
      x = 0,
      y = 0,
      targetImage = _ => hiResImage
    )
    assertPixels(rendered(image), Seq(
      (0x01, 0x0a, 0x08),
      (0x02, 0x0a, 0x02),
      (0x03, 0x0a, 0x02),
      (0x04, 0x0a, 0x01),
      (0x15, 0x0a, 0x02),
      (0x16, 0x0a, 0x03)
    ))
  }

  "render stretched multicolour sprite data over a multicolour bitmap image" in {
    val image = multiColourSprite(true).render(
      x = 0,
      y = 0,
      targetImage = _ => multiColourImage
    )
    assertPixels(rendered(image), Seq(
      (0x01, 0x0a, 0x00),
      (0x02, 0x0a, 0x00),
      (0x03, 0x0a, 0x00),
      (0x04, 0x0a, 0x00),
      (0x15, 0x0a, 0x01),
      (0x16, 0x0a, 0x01),
      (0x03, 0x14, 0x00),
      (0x04, 0x14, 0x02),
      (0x07, 0x14, 0x02),
      (0x08, 0x14, 0x01),
      (0x2b, 0x14, 0x02),
      (0x2c, 0x14, 0x04),
      (0x2b, 0x15, 0x02),
      (0x2c, 0x15, 0x0a)
    ))
  }

  "render multicolour sprite data overlayed by hires sprite data over a multicolour bitmap image" in {
    val image1 = multiColourSprite().render(
      x = 0,
      y = 0,
      targetImage = _ => multiColourImage
    )
    val image2 = hiResSprite().renderNext(
      x = 0,
      y = 0,
      targetImage = image1
    )
    val image3 = multiColourSprite().renderNext(
      x = 24,
      y = 0,
      targetImage = image2
    )
    val image4 = hiResSprite().renderNext(
      x = 24,
      y = 0,
      targetImage = image3
    )
    assertPixels(rendered(image4), Seq(
      (0x00, 0x0a, 0x00),
      (0x01, 0x0a, 0x00),
      (0x02, 0x0a, 0x02),
      (0x03, 0x0a, 0x02),
      (0x04, 0x0a, 0x00),
      (0x05, 0x0a, 0x01),
      (0x14, 0x0a, 0x02),
      (0x15, 0x0a, 0x00),
      (0x16, 0x0a, 0x00),
      (0x17, 0x0a, 0x06),
      (0x18, 0x0a, 0x00),
      (0x19, 0x0a, 0x00),
      (0x1a, 0x0a, 0x02),
      (0x1b, 0x0a, 0x02),
      (0x1c, 0x0a, 0x00),
      (0x1d, 0x0a, 0x01),
      (0x2e, 0x0a, 0x00),
      (0x2f, 0x0a, 0x0a),
      (0x30, 0x0a, 0x04)
    ))
    // new ImagePlus("Sprite Preview", rendered(image4).getProcessor.resize(640, 400)).show()
  }
}
