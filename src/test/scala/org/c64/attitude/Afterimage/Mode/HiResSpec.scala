package org.c64.attitude.Afterimage
package Mode

import org.scalatest.FreeSpec

import Mode.Data.{Bitmap,Screen}

class HiResSpec extends FreeSpec {

  "hires config bitmap size" in {
    assert(HiRes.size("bitmap") == 0x1f40)
  }

  "hires config screen size" in {
    assert(HiRes.size("screen") == 0x03e8)
  }

  def setupTestData() = {
    Tuple3[Array[Byte], Array[Byte], Byte](
      Array.fill(HiRes.size("bitmap")){0x00},
      Array.fill(HiRes.size("screen")){0x00},
      0x00
    )
  }

  "hires create with test data" in {
    val (bitmap, screen, border) = setupTestData()
    assert(HiRes(bitmap, screen, border).isInstanceOf[HiRes])
  }

  "hires create with empty bitmap" in {
    val (bitmap, screen, border) = setupTestData()
    intercept[IllegalArgumentException] {
      HiRes(Array[Byte](), screen, border)
    }
  }

  "hires create with too large bitmap" in {
    val (bitmap, screen, border) = setupTestData()
    intercept[IllegalArgumentException] {
      HiRes(bitmap :+ 0x00.toByte, screen, border)
    }
  }

  "hires create with too small bitmap" in {
    val (bitmap, screen, border) = setupTestData()
    intercept[IllegalArgumentException] {
      HiRes(bitmap.init, screen, border)
    }
  }

  "hires create with empty screen" in {
    val (bitmap, screen, border) = setupTestData()
    intercept[IllegalArgumentException] {
      HiRes(bitmap, Array[Byte](), border)
    }
  }

  "hires create with too large screen" in {
    val (bitmap, screen, border) = setupTestData()
    intercept[IllegalArgumentException] {
      HiRes(bitmap, screen :+ 0x00.toByte, border)
    }
  }

  "hires create with too small screen" in {
    val (bitmap, screen, border) = setupTestData()
    intercept[IllegalArgumentException] {
      HiRes(bitmap, screen.init, border)
    }
  }

  "hires create with no screen" in {
    val (bitmap, screen, border) = setupTestData()
    assert(HiRes(bitmap, border).isInstanceOf[HiRes])
  }

  "hires create with no border" in {
    val (bitmap, screen, border) = setupTestData()
    assert(HiRes(bitmap, screen).isInstanceOf[HiRes])
  }

  "get pixel" in {
    val bitmap = Array.fill(HiRes.size("bitmap")){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x80
        case 0x0001 => 0x40
        case 0x0006 => 0x02
        case 0x0007 => 0x01
        case 0x0008 => 0x20
        case 0x000f => 0x10
        case 0x0140 => 0x08
        case 0x0147 => 0x04
        case _ => data
      }
      value.toByte
    })

    val screen = Array.fill(HiRes.size("screen")){0xbc.toByte}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x01 => 0x10
        case 0x28 => 0xd5
        case _    => data
      }
      value.toByte
    })

    val border = 0x00.toByte

    val hiresImage = HiRes(bitmap, screen, border)

    assert(hiresImage.pixel(0, 0) == 0x0b)
    assert(hiresImage.pixel(1, 0) == 0x0c)
    assert(hiresImage.pixel(0, 1) == 0x0c)
    assert(hiresImage.pixel(1, 1) == 0x0b)

    assert(hiresImage.pixel(6, 6) == 0x0b)
    assert(hiresImage.pixel(7, 6) == 0x0c)
    assert(hiresImage.pixel(6, 7) == 0x0c)
    assert(hiresImage.pixel(7, 7) == 0x0b)

    assert(hiresImage.pixel(10, 0) == 0x01)
    assert(hiresImage.pixel(11, 0) == 0x00)
    assert(hiresImage.pixel(10, 7) == 0x00)
    assert(hiresImage.pixel(11, 7) == 0x01)

    assert(hiresImage.pixel(4, 8) == 0x0d)
    assert(hiresImage.pixel(5, 8) == 0x05)
    assert(hiresImage.pixel(4, 15) == 0x05)
    assert(hiresImage.pixel(5, 15) == 0x0d)
  }

  "invert image pixels" in {
    val (bitmap, screen, border) = setupTestData()
    val hiresImage = HiRes(bitmap).invert
    assert(hiresImage.bitmap.get()(0x00) == 0xff.toByte)
    assert(hiresImage.pixel(0, 0) == 0x0b)
  }

  "create hires object from screen slice" in {
    val (bitmap, screen, border) = setupTestData()
    val bitmapSlice = Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows)
    val screenSlice = Screen(screen, Screen.maxCols, Screen.maxRows).slice(0, 0, Screen.maxCols - 1, Screen.maxRows)
    intercept[IllegalArgumentException] {
      new HiRes(bitmapSlice, Some(screenSlice), Some(border))
    }
  }

  "create hires object from bitmap slice" in {
    val (bitmap, screen, border) = setupTestData()
    val bitmapSlice = Bitmap(bitmap, Bitmap.maxCols, Bitmap.maxRows).slice(0, 0, Bitmap.maxCols * 0x08 - 8, Bitmap.maxRows * 0x08)
    val screenSlice = Screen(screen, Screen.maxCols, Screen.maxRows)
    intercept[IllegalArgumentException] {
      new HiRes(bitmapSlice, Some(screenSlice), Some(border))
    }
  }

  "empty hires image" in {
    val hiResImage = HiRes()
    assert(hiResImage.isInstanceOf[HiRes])
  }
}
