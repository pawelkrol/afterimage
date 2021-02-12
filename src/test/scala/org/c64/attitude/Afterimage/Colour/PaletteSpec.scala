package org.c64.attitude.Afterimage
package Colour

import java.io.File

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods.{compact, render}

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

import scala.io.Codec.UTF8
import scala.io.Source.fromFile

class PaletteSpec extends AnyFreeSpec with Matchers {

  private val palette = Palette("default")

  def getBlackColour =
    Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, Some("black"))

  def getRedColour =
    Colour(0x68.toByte, 0x37.toByte, 0x2b.toByte, Some("red"))

  def getBlueColour =
    Colour(0x35.toByte, 0x28.toByte, 0x79.toByte, Some("blue"))

  "palette create" in {
    val palette = new Palette(Array.fill(16){new Colour(0x00, 0x00, 0x00, None)})
    assert(palette.isInstanceOf[Palette])
  }

  "load default palette" in {
    assert(palette.isInstanceOf[Palette])
  }

  "check default palette" in {
    palette(0) must equal(getBlackColour)
  }

  "load invalid palette" in {
    intercept[IllegalArgumentException] {
      Palette("invalid")
    }
  }

  "direct load invalid palette" in {
    intercept[IllegalArgumentException] {
      Palette.fromTemplate("invalid")
    }
  }

  "get red colour index from default palette" in {
    palette.get(getRedColour) must equal(0x02)
  }

  "get blue colour index from default palette" in {
    palette.get(getBlueColour) must equal(0x06)
  }

  "get black colour index from default palette" in {
    palette.get(getBlackColour) must equal(0x00)
  }

  "get red colour value from default palette" in {
    palette(0x02) must equal(getRedColour)
  }

  "get blue colour value from default palette" in {
    palette(0x06) must equal(getBlueColour)
  }

  "get black colour value from default palette" in {
    palette(0x00) must equal(getBlackColour)
  }

  "get blue colour value by colour name" in {
    palette("blue") must equal(Some(getBlueColour))
  }

  "get HTML red colour index from default palette" in {
    val htmlRed = Colour(0xff.toByte, 0x00.toByte, 0x00.toByte, Some("red"))
    palette.get(htmlRed) must equal(0x02)
  }

  "get HTML blue colour index from default palette" in {
    val htmlBlue = Colour(0x00.toByte, 0x00.toByte, 0xff.toByte, Some("blue"))
    palette.get(htmlBlue) must equal(0x06)
  }

  "get HTML green colour index from default palette" in {
    val htmlGreen = Colour(0x00.toByte, 0xff.toByte, 0x00.toByte, Some("green"))
    palette.get(htmlGreen) must equal(0x05)
  }

  "get ImageJ red pixel colour from default palette" in {
    palette.pixel(0x02) must equal(0x0068372b)
  }

  "get ImageJ blue pixel colour from default palette" in {
    palette.pixel(0x06) must equal(0x00352879)
  }

  "load palette from file success" in {
    val fileName = "%s/src/test/resources/palettes/custom.json".format(new File(".").getAbsolutePath())
    assert(Palette(fileName).isInstanceOf[Palette])
  }

  "load palette from file failure" in {
    val fileName = "%s/src/test/resources/palettes/invalid.json".format(new File(".").getAbsolutePath())
    intercept[IllegalArgumentException] { Palette(fileName) }
  }

  "direct load palette from file failure" in {
    val fileName = "%s/src/test/resources/palettes/invalid.json".format(new File(".").getAbsolutePath())
    intercept[IllegalArgumentException] { Palette.fromFile(fileName) }
  }

  "json serialisation" in {
    compact(render(palette.toJson)) must startWith("""{"palette":[""")
  }

  "direct create palette from json string success" in {
    val fileName = "%s/src/test/resources/palettes/custom.json".format(new File(".").getAbsolutePath())
    val json = fromFile(fileName)(UTF8).mkString
    assert(Palette.fromJson(json).isInstanceOf[Palette])
  }

  "direct create palette from json string failure" in {
    val fileName = "%s/src/test/resources/palettes/custom.json".format(new File(".").getAbsolutePath())
    val json = fromFile(fileName)(UTF8).mkString + "malformed"
    intercept[IllegalArgumentException] { Palette.fromJson(json) }
  }
}
