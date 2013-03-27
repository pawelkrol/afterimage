package org.c64.attitude.Afterimage
package Colour

import org.scalatest.Suite

class PaletteTest extends Suite {

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
    val palette = Palette("default")
    assert(palette.isInstanceOf[Palette])
  }

  def testCheckDefaultPalette {
    val palette = Palette("default")
    assert(palette(0) == getBlackColour)
  }

  def testLoadInvalidPalette {
    intercept[InvalidColourPaletteTemplate] {
      Palette("invalid")
    }
  }

  def testGetRedColourIndexFromDefaultPalette {
    val palette = Palette("default")
    assert(palette.get(getRedColour) == 0x02)
  }

  def testGetBlueColourIndexFromDefaultPalette {
    val palette = Palette("default")
    assert(palette.get(getBlueColour) == 0x06)
  }

  def testGetBlackColourIndexFromDefaultPalette {
    val palette = Palette("default")
    assert(palette.get(getBlackColour) == 0x00)
  }

  def testGetRedColourValueFromDefaultPalette {
    val palette = Palette("default")
    assert(palette(0x02) == getRedColour)
  }

  def testGetBlueColourValueFromDefaultPalette {
    val palette = Palette("default")
    assert(palette(0x06) == getBlueColour)
  }

  def testGetBlackColourValueFromDefaultPalette {
    val palette = Palette("default")
    assert(palette(0x00) == getBlackColour)
  }

  def testGetHTMLRedColourIndexFromDefaultPalette {
    val palette = Palette("default")
    val htmlRed = Colour(0xff.toByte, 0x00.toByte, 0x00.toByte, Some("red"))
    assert(palette.get(htmlRed) == 0x00)
  }

  def testGetHTMLBlueColourIndexFromDefaultPalette {
    val palette = Palette("default")
    val htmlBlue = Colour(0x00.toByte, 0x00.toByte, 0xff.toByte, Some("blue"))
    assert(palette.get(htmlBlue) == 0x00)
  }

  def testGetHTMLGreenColourIndexFromDefaultPalette {
    val palette = Palette("default")
    val htmlGreen = Colour(0x00.toByte, 0xff.toByte, 0x00.toByte, Some("green"))
    assert(palette.get(htmlGreen) == 0x00)
  }

  def testGetImageJRedPixelColourFromDefaultPalette {
    val palette = Palette("default")
    assert(palette.pixel(0x02) == 0x0068372b)
  }

  def testGetImageJBluePixelColourFromDefaultPalette {
    val palette = Palette("default")
    assert(palette.pixel(0x06) == 0x00352879)
  }
}
