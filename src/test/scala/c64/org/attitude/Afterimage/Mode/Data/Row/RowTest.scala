package org.c64.attitude.Afterimage
package Mode.Data.Row

import org.scalatest.Suite

import Mode.MultiColour

class RowTest extends Suite {

  def testGetBitmapFromNegativeIndexRow {
    intercept[InvalidDataRowIndexException] {
      Row.getBitmapRow(-1, Array[Byte]())
    }
  }

  def testGetBitmapFromExceedingIndexRow {
    intercept[InvalidDataRowIndexException] {
      Row.getBitmapRow(25, Array[Byte]())
    }
  }

  def testGetScreenFromNegativeIndexRow {
    intercept[InvalidDataRowIndexException] {
      Row.getScreenRow(-1, Array[Byte]())
    }
  }

  def testGetScreenFromExceedingIndexRow {
    intercept[InvalidDataRowIndexException] {
      Row.getScreenRow(25, Array[Byte]())
    }
  }

  def testGetColorsFromNegativeIndexRow {
    intercept[InvalidDataRowIndexException] {
      Row.getColorsRow(-1, Array[Byte]())
    }
  }

  def testGetColorsFromExceedingIndexRow {
    intercept[InvalidDataRowIndexException] {
      Row.getColorsRow(25, Array[Byte]())
    }
  }

  def testGetFirstBitmapRowFromBitmapData {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    assert(Row.getBitmapRow(0, testBitmap).deep == Array.fill(Row.bitmapRowSize){0xff.toByte}.deep)
  }

  def testGetSecondBitmapRowFromBitmapData {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    assert(Row.getBitmapRow(1, testBitmap).deep == Array.fill(Row.bitmapRowSize){0x00.toByte}.deep)
  }

  def testGetFirstScreenRowFromScreenData {
    val testScreen = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("screen") - Row.screenRowSize){0x00.toByte}
    assert(Row.getScreenRow(0, testScreen).deep == Array.fill(Row.screenRowSize){0xff.toByte}.deep)
  }

  def testGetSecondScreenRowFromBitmapData {
    val testScreen = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("screen") - Row.screenRowSize){0x00.toByte}
    assert(Row.getScreenRow(1, testScreen).deep == Array.fill(Row.screenRowSize){0x00.toByte}.deep)
  }

  def testGetFirstColorsRowFromColorsData {
    val testColors = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("colors") - Row.colorsRowSize){0x00.toByte}
    assert(Row.getColorsRow(0, testColors).deep == Array.fill(Row.colorsRowSize){0xff.toByte}.deep)
  }

  def testGetSecondColorsRowFromBitmapData {
    val testColors = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("colors") - Row.colorsRowSize){0x00.toByte}
    assert(Row.getColorsRow(1, testColors).deep == Array.fill(Row.colorsRowSize){0x00.toByte}.deep)
  }

  def testGetTwoBitmapRowsFromBitmapData {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(Row.bitmapRowSize){0x00.toByte}
    assert(Row.getBitmapRows(0, 1, testBitmap).deep == expectedRows.deep)
  }

  def testGetTwoBitmapRowsInvertedFromBitmapData {
    val testBitmap = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("bitmap") - Row.bitmapRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.bitmapRowSize){0xff.toByte} ++ Array.fill(Row.bitmapRowSize){0x00.toByte}
    assert(Row.getBitmapRows(1, 0, testBitmap).deep == expectedRows.deep)
  }

  def testGetTwoScreenRowsFromScreenData {
    val testScreen = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("screen") - Row.screenRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.screenRowSize){0xff.toByte} ++ Array.fill(Row.screenRowSize){0x00.toByte}
    assert(Row.getScreenRows(0, 1, testScreen).deep == expectedRows.deep)
  }

  def testGetTwoColorsRowsFromColorsData {
    val testColors = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(MultiColour.size("colors") - Row.colorsRowSize){0x00.toByte}
    val expectedRows = Array.fill(Row.colorsRowSize){0xff.toByte} ++ Array.fill(Row.colorsRowSize){0x00.toByte}
    assert(Row.getColorsRows(0, 1, testColors).deep == expectedRows.deep)
  }

  def testConvertDataToCodeSingleByte {
    val data = Array.fill(0x01){0x00.toByte}
    val got = Row.convertDataToCode(data)
    val expected = Array(
      "                .db $00",
      ""
    ).mkString("\n")
    assert(got == expected)
  }
  def testConvertDataToCodeSingleRow {
    val data = (1 to 16).map(_.toByte).toArray
    val got = Row.convertDataToCode(data)
    val expected = Array(
      "                .db $01,$02,$03,$04,$05,$06,$07,$08,$09,$0a,$0b,$0c,$0d,$0e,$0f,$10",
      ""
    ).mkString("\n")
    assert(got == expected)
  }
  def testConvertDataToCodeTwoRows {
    val data = (1 to 17).map(_.toByte).toArray
    val got = Row.convertDataToCode(data)
    val expected = Array(
      "                .db $01,$02,$03,$04,$05,$06,$07,$08,$09,$0a,$0b,$0c,$0d,$0e,$0f,$10",
      "                .db $11",
      ""
    ).mkString("\n")
    assert(got == expected)
  }
  def testConvertDataToCodeNoIndentation {
    val data = (1 to 5).map(_.toByte).toArray
    val got = Row.convertDataToCode(data, 4, 0)
    val expected = Array(
      ".db $01,$02,$03,$04",
      ".db $05",
      ""
    ).mkString("\n")
    assert(got == expected)
  }
  def testConvertDataToCodeThreeRows {
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
