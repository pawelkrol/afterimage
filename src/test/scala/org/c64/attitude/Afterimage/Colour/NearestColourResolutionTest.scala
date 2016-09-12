package org.c64.attitude.Afterimage
package Colour

import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

class NearestColourResolutionTest extends Suite with ShouldMatchers {

  private val palette = Palette("default")

  def testGetHTMLBrownColourIndexFromDefaultPalette {
    val htmlBrown = Colour(0xa5.toByte, 0x2a.toByte, 0x2a.toByte, Some("brown"))
    palette.get(htmlBrown) should equal (0x02)
  }

  def testGetHTMLOrangeColourIndexFromDefaultPalette {
    val htmlOrange = Colour(0xff.toByte, 0xa5.toByte, 0x00.toByte, Some("orange"))
    palette.get(htmlOrange) should equal (0x07)
  }

  def testGetLightGreyPaletteColourFromDefaultPalette {
    val paletteLightGrey = Colour(0x95.toByte, 0x95.toByte, 0x95.toByte, Some("light grey"))
    palette.get(paletteLightGrey) should equal (0x0f)
  }

  def testGetBandziorRedColourFromDefaultPalette {
    val bandziorRed = Colour(0x89.toByte, 0x40.toByte, 0x36.toByte, Some("red"))
    palette.get(bandziorRed) should equal (0x08)
  }
}
