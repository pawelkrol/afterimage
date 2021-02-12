package org.c64.attitude.Afterimage
package Mode.Data.Row

import org.scalatest.freespec.AnyFreeSpec

import Mode.MultiColour
import Util.ArrayHelper.deep

class MultiColourRowSpec extends AnyFreeSpec {

  def setupMultiColourImage() = {
    val bitmap = Array.fill(MultiColour.size("bitmap")){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x27 // 00100111
        case 0x0001 => 0x09 // 00001001
        case 0x0002 => 0x03 // 00000011
        case 0x0148 => 0x39 // 00111001
        case 0x0149 => 0xe1 // 11100001
        case _ => data
      }
      value.toByte
    })

    val screen = Array.fill(MultiColour.size("screen")){0x12.toByte}

    val colors = Array.fill(MultiColour.size("colors")){0x03.toByte}

    val bckgrd = 0x00.toByte

    MultiColour(bitmap, screen, colors, bckgrd)
  }

  def setupMultiColourSlice() = setupMultiColourImage().slice(0, 1)

  "multicolour all rows" in {
    val rows = setupMultiColourImage().rows

    assert(rows.length == 25)

    val firstRow = rows(0).asInstanceOf[MultiColourRow]
    val secondRow = rows(1).asInstanceOf[MultiColourRow]

    assert(firstRow.width == 0x28)
    assert(firstRow.bitmap.length == 0x0140)
    assert(firstRow.screen.length == 0x28)
    assert(firstRow.colors.length == 0x28)

    assert(secondRow.width == 0x28)
    assert(secondRow.bitmap.length == 0x0140)
    assert(secondRow.screen.length == 0x28)
    assert(secondRow.colors.length == 0x28)

    val bitmapLine1 = Array.fill(0x0140){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x27 // 00100111
        case 0x0001 => 0x09 // 00001001
        case 0x0002 => 0x03 // 00000011
        case _ => data
      }
      value.toByte
    })

    val bitmapLine2 = Array.fill(0x0140){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0008 => 0x39 // 00111001
        case 0x0009 => 0xe1 // 11100001
        case _ => data
      }
      value.toByte
    })

    assert(deep(firstRow.bitmap) == deep(bitmapLine1))
    assert(deep(firstRow.screen) == deep(Array.fill(0x28){0x12.toByte}))
    assert(deep(firstRow.colors) == deep(Array.fill(0x28){0x03.toByte}))

    assert(deep(secondRow.bitmap) == deep(bitmapLine2))
    assert(deep(secondRow.screen) == deep(Array.fill(0x28){0x12.toByte}))
    assert(deep(secondRow.colors) == deep(Array.fill(0x28){0x03.toByte}))
  }

  "multicolour slice rows" in {
    val rows = setupMultiColourSlice().rows

    assert(rows.length == 0x02)

    val row = rows(0).asInstanceOf[MultiColourRow]

    assert(row.leftMargin == 0x00)
    assert(row.rightMargin == 0x27)
    assert(row.leftPixelMargin == 0x0000)
    assert(row.rightPixelMargin == 0x013f)

    assert(deep(row.bitmap) == deep(row.fullBitmap()))
    assert(deep(row.screen) == deep(row.fullScreen()))
    assert(deep(row.colors) == deep(row.fullColors()))

    assert(deep(row.bitmap) == deep(row.fullBitmap(0xd5.toByte)))
    assert(deep(row.screen) == deep(row.fullScreen(0xe6.toByte)))
    assert(deep(row.colors) == deep(row.fullColors(0xfb.toByte)))
  }

  "multicolour data to code" in {
    val rows = setupMultiColourSlice().rows

    val row = rows(0).asInstanceOf[MultiColourRow]

    val got = row.toCode(label = "multicolour_image_test", fullRow = false)
    val expected = Array(
      ";--------------------------------------------------------------------------------------------------",
      "multicolour_image_test",
      ";--------------------------------------------------------------------------------------------------",
      "                ; $00 - background colour (1 byte)",
      "                .db $00",
      "                ; $01 - screen data (40 bytes)",
      "                .db $12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12",
      "                .db $12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12,$12",
      "                ; $29 - colors data (40 bytes)",
      "                .db $03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03",
      "                .db $03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03,$03",
      "                ; $51 - bitmap data (320 bytes)",
      "                .db $27,$09,$03,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                ; [fixed length] $191 - end",
      ";--------------------------------------------------------------------------------------------------",
      ""
    ).mkString("\n")
    assert(got == expected)
  }

  "multicolour serialize row" in {
    val rows = setupMultiColourSlice().rows

    val row = rows(0).asInstanceOf[MultiColourRow]

    val got = row.serialize(fullRow = false)
    val expected = "280012121212121212121212121212121212121212121212121212121212121212121212121212121212030303030303030303030303030303030303030303030303030303030303030303030303030303032709030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
    assert(got == expected)
  }

  "multicolour data deserialization" in {
    val data = "280012121212121212121212121212121212121212121212121212121212121212121212121212121212030303030303030303030303030303030303030303030303030303030303030303030303030303032709030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"

    val got = MultiColourRow.inflate(data)

    val expected = setupMultiColourImage().rows(0).asInstanceOf[MultiColourRow]

    assert(got.bckgrd == expected.bckgrd)
    assert(deep(got.bitmap) == deep(expected.bitmap))
    assert(deep(got.screen) == deep(expected.screen))
    assert(deep(got.colors) == deep(expected.colors))
  }
}
