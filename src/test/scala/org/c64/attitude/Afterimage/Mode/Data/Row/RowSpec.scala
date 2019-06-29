package org.c64.attitude.Afterimage
package Mode.Data.Row

import org.scalatest.FreeSpec

import Mode.MultiColour
import Util.ArrayHelper.deep

class RowSpec extends FreeSpec {

  "get bitmap from negative index row" in {
    intercept[IllegalArgumentException] {
      Row.getBitmapRow(-1, Array[Byte]())
    }
  }

  "get bitmap from exceeding index row" in {
    intercept[IllegalArgumentException] {
      Row.getBitmapRow(25, Array[Byte]())
    }
  }

  "get screen from negative index row" in {
    intercept[IllegalArgumentException] {
      Row.getScreenRow(-1, Array[Byte]())
    }
  }

  "get screen from exceeding index row" in {
    intercept[IllegalArgumentException] {
      Row.getScreenRow(25, Array[Byte]())
    }
  }

  "get colors from negative index row" in {
    intercept[IllegalArgumentException] {
      Row.getColorsRow(-1, Array[Byte]())
    }
  }

  "get colors from exceeding index row" in {
    intercept[IllegalArgumentException] {
      Row.getColorsRow(25, Array[Byte]())
    }
  }

  "get first bitmap row from bitmap data" in {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    assert(deep(Row.getBitmapRow(0, testBitmap)) == deep(Array.fill(Row.bitmapRowSize){0xff.toByte}))
  }

  "get second bitmap row from bitmap data" in {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    assert(deep(Row.getBitmapRow(1, testBitmap)) == deep(Array.fill(Row.bitmapRowSize){0x00.toByte}))
  }

  "get first screen row from screen data" in {
    val testScreen = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("screen") - Row.screenRowSize){0x00.toByte}
    assert(deep(Row.getScreenRow(0, testScreen)) == deep(Array.fill(Row.screenRowSize){0xff.toByte}))
  }

  "get second screen row from bitmap data" in {
    val testScreen = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("screen") - Row.screenRowSize){0x00.toByte}
    assert(deep(Row.getScreenRow(1, testScreen)) == deep(Array.fill(Row.screenRowSize){0x00.toByte}))
  }

  "get first colors row from colors data" in {
    val testColors = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("colors") - Row.colorsRowSize){0x00.toByte}
    assert(deep(Row.getColorsRow(0, testColors)) == deep(Array.fill(Row.colorsRowSize){0xff.toByte}))
  }

  "get second colors row from bitmap data" in {
    val testColors = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("colors") - Row.colorsRowSize){0x00.toByte}
    assert(deep(Row.getColorsRow(1, testColors)) == deep(Array.fill(Row.colorsRowSize){0x00.toByte}))
  }

  "get two bitmap rows from bitmap data" in {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(Row.bitmapRowSize){0x00.toByte}
    assert(deep(Row.getBitmapRows(0, 1, testBitmap)) == deep(expectedRows))
  }

  "get two bitmap rows inverted from bitmap data" in {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(Row.bitmapRowSize){0x00.toByte}
    assert(deep(Row.getBitmapRows(1, 0, testBitmap)) == deep(expectedRows))
  }

  "get two screen rows from screen data" in {
    val testScreen = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("screen") - Row.screenRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(Row.screenRowSize){0x00.toByte}
    assert(deep(Row.getScreenRows(0, 1, testScreen)) == deep(expectedRows))
  }

  "get two colors rows from colors data" in {
    val testColors = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("colors") - Row.colorsRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(Row.colorsRowSize){0x00.toByte}
    assert(deep(Row.getColorsRows(0, 1, testColors)) == deep(expectedRows))
  }

  "convert data to code single byte" in {
    val data = Array.fill(0x01){0x00.toByte}
    val got = Row.convertDataToCode(data)
    val expected = Array(
      "                .db $00",
      ""
    ).mkString("\n")
    assert(got == expected)
  }

  "convert data to code single row" in {
    val data = (1 to 16).map(_.toByte).toArray
    val got = Row.convertDataToCode(data)
    val expected = Array(
      "                .db $01,$02,$03,$04,$05,$06,$07,$08,$09,$0a,$0b,$0c,$0d,$0e,$0f,$10",
      ""
    ).mkString("\n")
    assert(got == expected)
  }

  "convert data to code two rows" in {
    val data = (1 to 17).map(_.toByte).toArray
    val got = Row.convertDataToCode(data)
    val expected = Array(
      "                .db $01,$02,$03,$04,$05,$06,$07,$08,$09,$0a,$0b,$0c,$0d,$0e,$0f,$10",
      "                .db $11",
      ""
    ).mkString("\n")
    assert(got == expected)
  }

  "convert data to code no indentation" in {
    val data = (1 to 5).map(_.toByte).toArray
    val got = Row.convertDataToCode(data, 4, 0)
    val expected = Array(
      ".db $01,$02,$03,$04",
      ".db $05",
      ""
    ).mkString("\n")
    assert(got == expected)
  }

  "convert data to code three rows" in {
    val data = (1 to 7).map(_.toByte).toArray
    val got = Row.convertDataToCode(data, 3, 4)
    val expected = Array(
      "    .db $01,$02,$03",
      "    .db $04,$05,$06",
      "    .db $07",
      ""
    ).mkString("\n")
    assert(got == expected)
  }
}
