package org.c64.attitude.Afterimage
package Colour

import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

class ColourTest extends Suite with ShouldMatchers {

  def getRedColour =
    Colour(0x68.toByte, 0x37.toByte, 0x2b.toByte, Some("red"))

  def getLightRedColour =
    Colour(0x9a.toByte, 0x67.toByte, 0x59.toByte, Some("light red"))

  def getBlueColour =
    Colour(0x35.toByte, 0x28.toByte, 0x79.toByte, Some("blue"))

  def testColourCreate {
    assert(Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, None).isInstanceOf[Colour])
  }

  def testColourCreateViaMapWithIntegers {
    val params = Map("red" -> 0, "green" -> 0, "blue" -> 0, "name" -> "black")
    assert(Colour(params).isInstanceOf[Colour])
  }

  def testColourCreateViaMapWithDoubles {
    val params = Map("red" -> 184.0, "green" -> 199.0, "blue" -> 111.0, "name" -> "yellow")
    assert(Colour(params).isInstanceOf[Colour])
  }

  def testColourCreateViaMapWithStrings {
    val params = Map("red" -> "111", "green" -> "61", "blue" -> "134", "name" -> "purple")
    assert(Colour(params).isInstanceOf[Colour])
  }

  def testDistanceBetweenRedAndBlue {
    val delta = getRedColour.delta_to(getBlueColour)
    (delta * 100).round * 0.01 should equal(94.39)
  }

  def testDistanceBetweenRedAndLightRed {
    val delta = getRedColour.delta_to(getLightRedColour)
    (delta * 100).round * 0.01 should equal(83.19)
  }

  def testDefaultColourCreate {
    assert(Colour().isInstanceOf[Colour])
  }

  def testGetImageJRedPixelColour {
    getRedColour.pixel should equal(0x0068372b)
  }

  def testGetImageJBluePixelColour {
    getBlueColour.pixel should equal(0x00352879)
  }
}
