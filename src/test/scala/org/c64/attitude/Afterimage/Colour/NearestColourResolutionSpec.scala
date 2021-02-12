package org.c64.attitude.Afterimage
package Colour

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class NearestColourResolutionSpec extends AnyFreeSpec with Matchers {

  private val palette = Palette("default")

  "get HTML brown colour index from default palette" in {
    val htmlBrown = Colour(0xa5.toByte, 0x2a.toByte, 0x2a.toByte, Some("brown"))
    palette.get(htmlBrown) must equal (0x02)
  }

  "get HTML orange colour index from default palette" in {
    val htmlOrange = Colour(0xff.toByte, 0xa5.toByte, 0x00.toByte, Some("orange"))
    palette.get(htmlOrange) must equal (0x07)
  }

  "get light grey palette colour from default palette" in {
    val paletteLightGrey = Colour(0x95.toByte, 0x95.toByte, 0x95.toByte, Some("light grey"))
    palette.get(paletteLightGrey) must equal (0x0f)
  }

  "get bandzior red colour from default palette" in {
    val bandziorRed = Colour(0x89.toByte, 0x40.toByte, 0x36.toByte, Some("red"))
    palette.get(bandziorRed) must equal (0x08)
  }
}
