package org.c64.attitude.Afterimage
package Colour

import java.io.File

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods.{compact, render}

import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

class PaletteTest extends Suite with ShouldMatchers {

  private val palette = Palette("default")

  def getBlackColour =
    Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, Some("black"))

  def getRedColour =
    Colour(0x68.toByte, 0x37.toByte, 0x2b.toByte, Some("red"))

  def getBlueColour =
    Colour(0x35.toByte, 0x28.toByte, 0x79.toByte, Some("blue"))

  def testPaletteCreate {
    val palette = new Palette(Array.fill(16){new Colour(0x00, 0x00, 0x00, None)})
    assert(palette.isInstanceOf[Palette])
  }

  def testLoadDefaultPalette {
    assert(palette.isInstanceOf[Palette])
  }

  def testCheckDefaultPalette {
    palette(0) should equal(getBlackColour)
  }

  def testLoadInvalidPalette {
    intercept[InvalidColourPalette] {
      Palette("invalid")
    }
  }

  def testDirectLoadInvalidPalette {
    intercept[InvalidColourPaletteTemplate] {
      Palette.fromTemplate("invalid")
    }
  }

  def testGetRedColourIndexFromDefaultPalette {
    palette.get(getRedColour) should equal(0x02)
  }

  def testGetBlueColourIndexFromDefaultPalette {
    palette.get(getBlueColour) should equal(0x06)
  }

  def testGetBlackColourIndexFromDefaultPalette {
    palette.get(getBlackColour) should equal(0x00)
  }

  def testGetRedColourValueFromDefaultPalette {
    palette(0x02) should equal(getRedColour)
  }

  def testGetBlueColourValueFromDefaultPalette {
    palette(0x06) should equal(getBlueColour)
  }

  def testGetBlackColourValueFromDefaultPalette {
    palette(0x00) should equal(getBlackColour)
  }

  def testGetBlueColourValueByColourName {
    palette("blue") should equal(Some(getBlueColour))
  }

  def testGetHTMLRedColourIndexFromDefaultPalette {
    val htmlRed = Colour(0xff.toByte, 0x00.toByte, 0x00.toByte, Some("red"))
    palette.get(htmlRed) should equal(0x02)
  }

  def testGetHTMLBlueColourIndexFromDefaultPalette {
    val htmlBlue = Colour(0x00.toByte, 0x00.toByte, 0xff.toByte, Some("blue"))
    palette.get(htmlBlue) should equal(0x06)
  }

  def testGetHTMLGreenColourIndexFromDefaultPalette {
    val htmlGreen = Colour(0x00.toByte, 0xff.toByte, 0x00.toByte, Some("green"))
    palette.get(htmlGreen) should equal(0x05)
  }

  def testGetImageJRedPixelColourFromDefaultPalette {
    palette.pixel(0x02) should equal(0x0068372b)
  }

  def testGetImageJBluePixelColourFromDefaultPalette {
    palette.pixel(0x06) should equal(0x00352879)
  }

  def testLoadPaletteFromFileSuccess {
    val fileName = "%s/src/test/resources/palettes/custom.json".format(new File(".").getAbsolutePath())
    assert(Palette(fileName).isInstanceOf[Palette])
  }

  def testLoadPaletteFromFileFailure {
    val fileName = "%s/src/test/resources/palettes/invalid.json".format(new File(".").getAbsolutePath())
    intercept[InvalidColourPalette] { Palette(fileName) }
  }

  def testDirectLoadPaletteFromFileFailure {
    val fileName = "%s/src/test/resources/palettes/invalid.json".format(new File(".").getAbsolutePath())
    intercept[InvalidColourPaletteFilename] { Palette.fromFile(fileName) }
  }

  def testJsonSerialisation {
    compact(render(palette.toJson)) should startWith("""{"palette":[""")
  }
}
