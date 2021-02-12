package org.c64.attitude.Afterimage
package Colour

import org.json4s.native.JsonMethods.{compact, render}

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class ColourSpec extends AnyFreeSpec with Matchers {

  private val palette = Palette("default")

  def getRedColour =
    Colour(0x68.toByte, 0x37.toByte, 0x2b.toByte, Some("red"))

  def getLightRedColour =
    Colour(0x9a.toByte, 0x67.toByte, 0x59.toByte, Some("light red"))

  def getBlueColour =
    Colour(0x35.toByte, 0x28.toByte, 0x79.toByte, Some("blue"))

  "colour create" in {
    assert(Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, None).isInstanceOf[Colour])
  }

  "colour create via map with integers" in {
    val params = Map("red" -> 0, "green" -> 0, "blue" -> 0, "name" -> "black")
    assert(Colour(params).isInstanceOf[Colour])
  }

  "colour create via map with doubles" in {
    val params = Map("red" -> 184.0, "green" -> 199.0, "blue" -> 111.0, "name" -> "yellow")
    assert(Colour(params).isInstanceOf[Colour])
  }

  "colour create via map with strings" in {
    val params = Map("red" -> "111", "green" -> "61", "blue" -> "134", "name" -> "purple")
    assert(Colour(params).isInstanceOf[Colour])
  }

  "distance between red and blue" in {
    val delta = getRedColour.delta_to(getBlueColour)
    (delta * 100).round * 0.01 must equal(94.39)
  }

  "distance between red and light red" in {
    val delta = getRedColour.delta_to(getLightRedColour)
    (delta * 100).round * 0.01 must equal(83.19)
  }

  "distance between bandzior red and palette red" in {
    val bandziorRed = Colour(0x89.toByte, 0x40.toByte, 0x36.toByte, Some("red"))
    val paletteRed = palette(0x02)
    val delta = bandziorRed.delta_to(paletteRed)
    (delta * 100).round * 0.01 must equal(35.93)
  }

  "distance between bandzior red and palette orange" in {
    val bandziorRed = Colour(0x89.toByte, 0x40.toByte, 0x36.toByte, Some("red"))
    val paletteOrange = palette(0x08)
    val delta = bandziorRed.delta_to(paletteOrange)
    (delta * 100).round * 0.01 must equal(34.50)
  }

  "default colour create" in {
    assert(Colour().isInstanceOf[Colour])
  }

  "get ImageJ red pixel colour" in {
    getRedColour.pixel must equal(0x0068372b)
  }

  "get ImageJ blue pixel colour" in {
    getBlueColour.pixel must equal(0x00352879)
  }

  "json serialisation" in {
    compact(render(getRedColour.toJson)) must equal("""{"red":104,"green":55,"blue":43,"name":"red"}""")
  }
}
