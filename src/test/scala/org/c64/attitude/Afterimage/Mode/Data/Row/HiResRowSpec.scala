package org.c64.attitude.Afterimage
package Mode.Data.Row

import org.scalatest.FreeSpec

import Mode.HiRes
import Util.ArrayHelper.deep

class HiResRowSpec extends FreeSpec {

  def setupHiResImage() = {
    val bitmap = Array.fill(HiRes.size("bitmap")){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x0a
        case 0x0002 => 0x04
        case 0x0003 => 0x0c
        case 0x007e => 0xc0
        case 0x007f => 0x80
        case 0x0148 => 0x02
        case 0x0150 => 0x80
        case _ => data
      }
      value.toByte
    })

    val screen = Array.fill(HiRes.size("screen")){0xbc.toByte}

    HiRes(bitmap, screen)
  }

  def setupHiResSlice() = setupHiResImage().slice(0x00, 0x00, 0x80, 0x10)

  "hires all rows" in {
    val rows = setupHiResImage().rows

    assert(rows.length == 25)

    val firstRow = rows(0).asInstanceOf[HiResRow]
    val secondRow = rows(1).asInstanceOf[HiResRow]

    assert(firstRow.width == 0x28)
    assert(firstRow.bitmap.length == 0x0140)
    assert(firstRow.screen.length == 0x28)

    assert(secondRow.width == 0x28)
    assert(secondRow.bitmap.length == 0x0140)
    assert(secondRow.screen.length == 0x28)

    val bitmapLine1 = Array.fill(0x0140){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x0a
        case 0x0002 => 0x04
        case 0x0003 => 0x0c
        case 0x007e => 0xc0
        case 0x007f => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapLine2 = Array.fill(0x0140){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0008 => 0x02
        case 0x0010 => 0x80
        case _ => data
      }
      value.toByte
    })

    assert(deep(firstRow.bitmap) == deep(bitmapLine1))
    assert(deep(firstRow.screen) == deep(Array.fill(0x28){0xbc.toByte}))

    assert(deep(secondRow.bitmap) == deep(bitmapLine2))
    assert(deep(secondRow.screen) == deep(Array.fill(0x28){0xbc.toByte}))
  }

  "hires pixel offsets" in {
    val emptyScreen = Array.fill(0x28){0xbc.toByte}
    val emptyBitmap = Array.fill(0x0140){0x00.toByte}

    val bitmap = emptyBitmap.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0087 => 0x80
        case _ => data
      }
      value.toByte
    })

    val hiResImageRow = HiResRow(emptyBitmap, emptyScreen)

    assert(hiResImageRow.firstPixelOffset(bitmap) == 0x07)
    assert(hiResImageRow.lastPixelOffset(bitmap) == 0x80)
  }

  "hires char margins" in {
    val emptyScreen = Array.fill(0x28){0xbc.toByte}

    val bitmap = Array.fill(0x0140){0x00.toByte}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0010 => 0x18
        case 0x00cc => 0x42
        case _ => data
      }
      value.toByte
    })

    val hiResImageRow = HiResRow(bitmap, emptyScreen)

    assert(hiResImageRow.leftMargin == 0x02)
    assert(hiResImageRow.rightMargin == 0x19)
  }

  "hires slice rows" in {
    val rows = setupHiResSlice().rows

    assert(rows.length == 2)

    val firstRow = rows(0).asInstanceOf[HiResRow]
    val secondRow = rows(1).asInstanceOf[HiResRow]

    assert(firstRow.leftMargin == 0x00)
    assert(firstRow.rightMargin == 0x0f)
    assert(firstRow.leftPixelMargin == 0x0004)
    assert(firstRow.rightPixelMargin == 0x0079)

    assert(secondRow.leftMargin == 0x01)
    assert(secondRow.rightMargin == 0x02)
    assert(secondRow.leftPixelMargin == 0x000e)
    assert(secondRow.rightPixelMargin == 0x0010)

    assert(firstRow.width == 0x10)
    assert(firstRow.charWidth == 0x10)
    assert(firstRow.pixelWidth == 0x76)
    assert(firstRow.bitmap.length == 0x0080)
    assert(firstRow.screen.length == 0x10)

    assert(secondRow.width == 0x10)
    assert(secondRow.charWidth == 0x02)
    assert(secondRow.pixelWidth == 0x03)
    assert(secondRow.bitmap.length == 0x0080)
    assert(secondRow.screen.length == 0x10)

    val bitmapLine1 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x0a
        case 0x0002 => 0x04
        case 0x0003 => 0x0c
        case 0x007e => 0xc0
        case 0x007f => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapLine2 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0008 => 0x02
        case 0x0010 => 0x80
        case _ => data
      }
      value.toByte
    })

    assert(deep(firstRow.bitmap) == deep(bitmapLine1))
    assert(deep(firstRow.screen) == deep(Array.fill(0x10){0xbc.toByte}))

    assert(deep(secondRow.bitmap) == deep(bitmapLine2))
    assert(deep(secondRow.screen) == deep(Array.fill(0x10){0xbc.toByte}))
  }

  "hires image slice data to code" in {
    val rows = setupHiResSlice().rows

    val row = rows(0).asInstanceOf[HiResRow]

    val got = row.toCode(label = "hires_image_test", fullRow = false)
    val expected = Array(
      ";--------------------------------------------------------------------------------------------------",
      "hires_image_test",
      ";--------------------------------------------------------------------------------------------------",
      "                ; $00 - width (1 byte)",
      "                .db $10",
      "                ; $01 - screen data (16 bytes)",
      "                .db $bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc",
      "                ; $11 - bitmap data (128 bytes)",
      "                .db $01,$0a,$04,$0c,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$c0,$80",
      "                ; [variable length] $91 - end",
      ";--------------------------------------------------------------------------------------------------",
      ""
    ).mkString("\n")
    assert(got == expected)
  }

  "hires full width data to code" in {
    val rows = setupHiResImage().rows

    val row = rows(0).asInstanceOf[HiResRow]

    val got = row.toCode(label = "hires_image_test", fullRow = false)
    val expected = Array(
      ";--------------------------------------------------------------------------------------------------",
      "hires_image_test",
      ";--------------------------------------------------------------------------------------------------",
      "                ; $00 - width (1 byte)",
      "                .db $28",
      "                ; $01 - screen data (40 bytes)",
      "                .db $bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc",
      "                .db $bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc,$bc",
      "                ; $29 - bitmap data (320 bytes)",
      "                .db $01,$0a,$04,$0c,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$c0,$80,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                .db $00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00",
      "                ; [variable length] $169 - end",
      ";--------------------------------------------------------------------------------------------------",
      ""
    ).mkString("\n")
    assert(got == expected)
  }

  "hires serialize image slice row" in {
    val rows = setupHiResSlice().rows

    val row = rows(0).asInstanceOf[HiResRow]

    val got = row.serialize(fullRow = false)
    val expected = "10bcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbc010a040c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000c080"
    assert(got == expected)
  }

  "hires serialize full width row" in {
    val rows = setupHiResImage().rows

    val row = rows(0).asInstanceOf[HiResRow]

    val got = row.serialize(fullRow = false)
    val expected = "28bcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbcbc010a040c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000c080000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
    assert(got == expected)
  }

  "hires data deserialization" in {
    val data = "02bcbc0102030405060708090a0b0c0d0e0f10"

    val got = HiResRow.inflate(data)

    val expected = HiResRow(
      List(0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10).map(_.toByte).toArray,
      List(0xbc, 0xbc).map(_.toByte).toArray
    )

    assert(deep(got.bitmap) == deep(expected.bitmap))
    assert(deep(got.screen) == deep(expected.screen))
  }

  "hires row shift" in {
    val rows = setupHiResSlice().rows

    val row = rows(0).asInstanceOf[HiResRow]

    val bitmapLineLeft1 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0076 => 0xc0
        case 0x0077 => 0x80
        case _ => data
      }
      value.toByte
    })

    val screenLineLeft1 = Array.fill(0x0010){0xbc}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x000f => 0x00
        case _ => data
      }
      value.toByte
    })

    val bitmapLineLeft2 = bitmapLineLeft1

    val screenLineLeft2 = Array.fill(0x0010){0xbc.toByte}

    val bitmapLineLeft3 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0076 => 0xc0
        case 0x0077 => 0x80
        case 0x0078 => 0xff
        case 0x0079 => 0xff
        case 0x007a => 0xff
        case 0x007b => 0xff
        case 0x007c => 0xff
        case 0x007d => 0xff
        case 0x007e => 0xff
        case 0x007f => 0xff
        case _ => data
      }
      value.toByte
    })

    val screenLineLeft3 = Array.fill(0x0010){0xbc}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x000f => 0x01
        case _ => data
      }
      value.toByte
    })

    val bitmapLineLeft4 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0008 => 0x01
        case 0x0009 => 0x0a
        case 0x000a => 0x04
        case 0x000b => 0x0c
        case _ => data
      }
      value.toByte
    })

    val screenLineLeft4 = Array.fill(0x0010){0xbc}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x00
        case _ => data
      }
      value.toByte
    })

    val bitmapLineLeft5 = bitmapLineLeft4

    val screenLineLeft5 = Array.fill(0x0010){0xbc.toByte}

    val bitmapLineLeft6 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0xff
        case 0x0001 => 0xff
        case 0x0002 => 0xff
        case 0x0003 => 0xff
        case 0x0004 => 0xff
        case 0x0005 => 0xff
        case 0x0006 => 0xff
        case 0x0007 => 0xff
        case 0x0008 => 0x01
        case 0x0009 => 0x0a
        case 0x000a => 0x04
        case 0x000b => 0x0c
        case _ => data
      }
      value.toByte
    })

    val screenLineLeft6 = Array.fill(0x0010){0xbc}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case _ => data
      }
      value.toByte
    })

    val bitmapLineLeft7 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0006 => 0xc0
        case 0x0007 => 0x80
        case _ => data
      }
      value.toByte
    })

    val screenLineLeft7 = Array.fill(0x0010){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0xbc
        case _ => data
      }
      value.toByte
    })

    val bitmapLineLeft8 = Array.fill(0x0080){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0078 => 0x01
        case 0x0079 => 0x0a
        case 0x007a => 0x04
        case 0x007b => 0x0c
        case _ => data
      }
      value.toByte
    })

    val screenLineLeft8 = Array.fill(0x0010){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x000f => 0xbc
        case _ => data
      }
      value.toByte
    })

    val bitmapLineLeft9 = Array.fill(0x0080){0x00}

    val screenLineLeft9 = Array.fill(0x0010){0x00}

    assert(deep(row.shift(-1).bitmap) == deep(bitmapLineLeft1))
    assert(deep(row.shift(-1).screen) == deep(screenLineLeft1))

    assert(deep(row.shift(-1, 0xbc.toByte).bitmap) == deep(bitmapLineLeft2))
    assert(deep(row.shift(-1, 0xbc.toByte).screen) == deep(screenLineLeft2))

    assert(deep(row.shift(-1, 0x01.toByte, 0xff.toByte).bitmap) == deep(bitmapLineLeft3))
    assert(deep(row.shift(-1, 0x01.toByte, 0xff.toByte).screen) == deep(screenLineLeft3))

//throw new RuntimeException(">>>\nBEFORE:\n%s\nGOT:\n%s\nEXPECTED:\n%s\n<<<".format(row.screen.mkString("-"), row.shift(1, 0xbc.toByte).screen.mkString("-"), screenLineLeft4.mkString("-")))

    assert(deep(row.shift(1).bitmap) == deep(bitmapLineLeft4))
    assert(deep(row.shift(1).screen) == deep(screenLineLeft4))

    assert(deep(row.shift(1, 0xbc.toByte).bitmap) == deep(bitmapLineLeft5))
    assert(deep(row.shift(1, 0xbc.toByte).screen) == deep(screenLineLeft5))

    assert(deep(row.shift(1, 0x01.toByte, 0xff.toByte).bitmap) == deep(bitmapLineLeft6))
    assert(deep(row.shift(1, 0x01.toByte, 0xff.toByte).screen) == deep(screenLineLeft6))

    assert(deep(row.shift(-15).bitmap) == deep(bitmapLineLeft7))
    assert(deep(row.shift(-15).screen) == deep(screenLineLeft7))

    assert(deep(row.shift(15).bitmap) == deep(bitmapLineLeft8))
    assert(deep(row.shift(15).screen) == deep(screenLineLeft8))

    assert(deep(row.shift(-16).bitmap) == deep(bitmapLineLeft9))
    assert(deep(row.shift(-16).screen) == deep(screenLineLeft9))

    assert(deep(row.shift(16).bitmap) == deep(bitmapLineLeft9))
    assert(deep(row.shift(16).screen) == deep(screenLineLeft9))
  }
}
