package org.c64.attitude.Afterimage
package Mode

import org.scalatest.freespec.AnyFreeSpec

class MultiColourSpec extends AnyFreeSpec {

  "multicolour config bitmap size" in {
    assert(MultiColour.size("bitmap") == 0x1f40)
  }

  "multicolour config screen size" in {
    assert(MultiColour.size("screen") == 0x03e8)
  }

  "multicolour config colors size" in {
    assert(MultiColour.size("colors") == 0x03e8)
  }

  def setupTestData() = {
    Tuple5[Array[Byte], Array[Byte], Array[Byte], Byte, Byte](
      Array.fill(MultiColour.size("bitmap")){0x00},
      Array.fill(MultiColour.size("screen")){0x00},
      Array.fill(MultiColour.size("colors")){0x00},
      0x00,
      0x00
    )
  }

  "multicolour create with test data" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    assert(MultiColour(bitmap, screen, colors, bckgrd, border).isInstanceOf[MultiColour])
  }

  "multicolour create with empty bitmap" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(Array[Byte](), screen, colors, bckgrd, border)
    }
  }

  "multicolour create with too large bitmap" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap :+ 0x00.toByte, screen, colors, bckgrd, border)
    }
  }

  "multicolour create with too small bitmap" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap.init, screen, colors, bckgrd, border)
    }
  }

  "multicolour create with empty screen" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap, Array[Byte](), colors, bckgrd, border)
    }
  }

  "multicolour create with too large screen" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap, screen :+ 0x00.toByte, colors, bckgrd, border)
    }
  }

  "multicolour create with too small screen" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap, screen.init, colors, bckgrd, border)
    }
  }

  "multicolour create with empty colors" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap, screen, Array[Byte](), bckgrd, border)
    }
  }

  "multicolour create with too large colors" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap, screen, colors :+ 0x00.toByte, bckgrd, border)
    }
  }

  "multicolour create with too small colors" in {
    val (bitmap, screen, colors, border, bckgrd) = setupTestData()
    intercept[IllegalArgumentException] {
      MultiColour(bitmap, screen, colors.init, bckgrd, border)
    }
  }

  "get pixel" in {
    val bitmap = Array.fill(MultiColour.size("bitmap")){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0xe4 // 11100100
        case 0x0007 => 0x93 // 10010011
        case 0x000f => 0x4e // 01001110
        case 0x0140 => 0x39 // 00111001
        case _ => data
      }
      value.toByte
    })

    val screen = Array.fill(MultiColour.size("screen")){0x00.toByte}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x00 => 0x12
        case 0x01 => 0x34
        case 0x02 => 0xab
        case 0x28 => 0x56
        case _    => data
      }
      value.toByte
    })

    val colors = Array.fill(MultiColour.size("colors")){0x00.toByte}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x00 => 0x07
        case 0x01 => 0x08
        case 0x02 => 0x0c
        case 0x28 => 0x09
        case _    => data
      }
      value.toByte
    })

    val bckgrd = 0x00.toByte

    val multiColourImage = MultiColour(bitmap, screen, colors, bckgrd)

    assert(multiColourImage.pixel(0, 0) == 0x07)
    assert(multiColourImage.pixel(1, 0) == 0x02)
    assert(multiColourImage.pixel(2, 0) == 0x01)
    assert(multiColourImage.pixel(3, 0) == 0x00)

    assert(multiColourImage.pixel(0, 7) == 0x02)
    assert(multiColourImage.pixel(1, 7) == 0x01)
    assert(multiColourImage.pixel(2, 7) == 0x00)
    assert(multiColourImage.pixel(3, 7) == 0x07)

    assert(multiColourImage.pixel(4, 7) == 0x03)
    assert(multiColourImage.pixel(5, 7) == 0x00)
    assert(multiColourImage.pixel(6, 7) == 0x08)
    assert(multiColourImage.pixel(7, 7) == 0x04)

    assert(multiColourImage.pixel(0, 8) == 0x00)
    assert(multiColourImage.pixel(1, 8) == 0x09)
    assert(multiColourImage.pixel(2, 8) == 0x06)
    assert(multiColourImage.pixel(3, 8) == 0x05)

    assert(multiColourImage.pixel(8, 0) == 0x00)
    assert(multiColourImage.pixel(11, 7) == 0x00)
  }

  "empty multicolour image" in {

    val multiColourImage = MultiColour()
    assert(multiColourImage.isInstanceOf[MultiColour])
  }
}
