package org.c64.attitude.Afterimage
package Mode.Data

import org.scalatest.freespec.AnyFreeSpec

import Util.ArrayHelper.deep

class BitmapSpec extends AnyFreeSpec {

  "create default bitmap" in {

    assert(Bitmap().isInstanceOf[Bitmap])
  }

  "create bitmap with size" in {

    assert(Bitmap(40, 25).isInstanceOf[Bitmap])
    assert(Bitmap(39, 25).isInstanceOf[Bitmap])
    assert(Bitmap(40, 24).isInstanceOf[Bitmap])
    assert(Bitmap(40, 1).isInstanceOf[Bitmap])
    assert(Bitmap(1, 25).isInstanceOf[Bitmap])
    assert(Bitmap(1, 1).isInstanceOf[Bitmap])

    intercept[IllegalArgumentException] {
      Bitmap(40, 26)
    }
    intercept[IllegalArgumentException] {
      Bitmap(41, 25)
    }
    intercept[IllegalArgumentException] {
      Bitmap(40, 0)
    }
    intercept[IllegalArgumentException] {
      Bitmap(0, 25)
    }
    intercept[IllegalArgumentException] {
      Bitmap(0, 0)
    }
    intercept[IllegalArgumentException] {
      Bitmap(-1, -1)
    }
  }

  "create bitmap with data" in {

    assert(Bitmap(Array.fill(8000){0x00}, 40, 25).isInstanceOf[Bitmap])
    assert(Bitmap(Array.fill(7800){0x00}, 39, 25).isInstanceOf[Bitmap])
    assert(Bitmap(Array.fill(7680){0x00}, 40, 24).isInstanceOf[Bitmap])
    assert(Bitmap(Array.fill(320){0x00}, 40, 1).isInstanceOf[Bitmap])
    assert(Bitmap(Array.fill(200){0x00}, 1, 25).isInstanceOf[Bitmap])
    assert(Bitmap(Array.fill(8){0x00}, 1, 1).isInstanceOf[Bitmap])

    intercept[IllegalArgumentException] {
      Bitmap(Array.fill(8001){0x00}, 40, 25)
    }
    intercept[IllegalArgumentException] {
      Bitmap(Array.fill(7999){0x00}, 40, 25)
    }
    intercept[IllegalArgumentException] {
      Bitmap(Array(), 40, 25)
    }
  }

  def setupTestBitmap = {

    val bitmap = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
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
        case 0x0148 => 0x01
        case 0x014f => 0x80
        case _ => data
      }
      value.toByte
    })

    Bitmap(bitmap, 40, 25)
  }

  "is pixel set" in {

    val bitmap = setupTestBitmap

    assert(bitmap.isPixelSet(0, 0) == true)
    assert(bitmap.isPixelSet(1, 0) == false)

    assert(bitmap.isPixelSet(0, 1) == false)
    assert(bitmap.isPixelSet(1, 1) == true)

    assert(bitmap.isPixelSet(0, 7) == false)
    assert(bitmap.isPixelSet(7, 7) == true)

    assert(bitmap.isPixelSet(10, 0) == true)
    assert(bitmap.isPixelSet(11, 0) == false)

    assert(bitmap.isPixelSet(10, 7) == false)
    assert(bitmap.isPixelSet(11, 7) == true)

    assert(bitmap.isPixelSet(4, 8) == true)
    assert(bitmap.isPixelSet(5, 8) == false)

    assert(bitmap.isPixelSet(8, 15) == true)
    assert(bitmap.isPixelSet(9, 15) == false)

    intercept[IndexOutOfBoundsException] {
      bitmap.isPixelSet(-1, -1)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.isPixelSet(-1, 0)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.isPixelSet(0, -1)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.isPixelSet(320, 0)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.isPixelSet(0, 200)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.isPixelSet(320, 200)
    }
  }

  "get pixels" in {

    val bitmap = setupTestBitmap

    assert(bitmap.getPixels(0, 0, 1) == 0x01.toByte)
    assert(bitmap.getPixels(0, 0, 2) == 0x02.toByte)
    assert(bitmap.getPixels(0, 0, 3) == 0x04.toByte)
    assert(bitmap.getPixels(0, 0, 4) == 0x08.toByte)
    assert(bitmap.getPixels(0, 0, 5) == 0x10.toByte)
    assert(bitmap.getPixels(0, 0, 6) == 0x20.toByte)
    assert(bitmap.getPixels(0, 0, 7) == 0x40.toByte)
    assert(bitmap.getPixels(0, 0, 8) == 0x80.toByte)

    intercept[IllegalArgumentException] {
      bitmap.getPixels(0, 0, 0)
    }
    intercept[IllegalArgumentException] {
      bitmap.getPixels(0, 0, -1)
    }
    intercept[IllegalArgumentException] {
      bitmap.getPixels(0, 0, 9)
    }

    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(-1, -1, 1)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(-1, 0, 1)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(0, -1, 1)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(320, 0, 1)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(0, 200, 1)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(320, 200, 1)
    }

    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(319, 0, 2)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(313, 0, 8)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.getPixels(319, 199, 2)
    }
  }

  "bitmap data serialization" in {

    assert(deep(Bitmap().get()) == deep(Array.fill(0x1f40){0x00}))
    assert(deep(Bitmap(1, 1).get()) == deep(Array.fill(0x08){0x00}))
    assert(deep(Bitmap(40, 25).get()) == deep(Array.fill(0x1f40){0x00}))

    val bitmap = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
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
        case 0x0148 => 0x01
        case 0x014f => 0x80
        case _ => data
      }
      value.toByte
    })

    assert(deep(setupTestBitmap.get()) == deep(bitmap))
  }

  "bitmap data shift at char level" in {

    val bitmapToLeft = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x20
        case 0x0007 => 0x10
        case 0x0140 => 0x01
        case 0x0147 => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapToRight = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0008 => 0x80
        case 0x0009 => 0x40
        case 0x000e => 0x02
        case 0x000f => 0x01
        case 0x0010 => 0x20
        case 0x0017 => 0x10
        case 0x0148 => 0x08
        case 0x014f => 0x04
        case 0x0150 => 0x01
        case 0x0157 => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapToTop = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x08
        case 0x0007 => 0x04
        case 0x0008 => 0x01
        case 0x000f => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapToBottom = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0140 => 0x80
        case 0x0141 => 0x40
        case 0x0146 => 0x02
        case 0x0147 => 0x01
        case 0x0148 => 0x20
        case 0x014f => 0x10
        case 0x0280 => 0x08
        case 0x0287 => 0x04
        case 0x0288 => 0x01
        case 0x028f => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmap = setupTestBitmap

    assert(deep(bitmap.shift(-8, 0).get()) == deep(bitmapToLeft))
    assert(deep(bitmap.shift(8, 0).get()) == deep(bitmapToRight))
    assert(deep(bitmap.shift(0, -8).get()) == deep(bitmapToTop))
    assert(deep(bitmap.shift(0, 8).get()) == deep(bitmapToBottom))

    assert(deep(bitmap.shift(-320, 0).get()) == deep(Array.fill(8000){0x00}))
    assert(deep(bitmap.shift(320, 0).get()) == deep(Array.fill(8000){0x00}))
    assert(deep(bitmap.shift(0, -200).get()) == deep(Array.fill(8000){0x00}))
    assert(deep(bitmap.shift(0, 200).get()) == deep(Array.fill(8000){0x00}))
  }

  "bitmap data shift at pixel level" in {

    val bitmapToLeft = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0001 => 0x80
        case 0x0006 => 0x04
        case 0x0007 => 0x02
        case 0x0008 => 0x40
        case 0x000f => 0x20
        case 0x0140 => 0x10
        case 0x0147 => 0x09
        case 0x0148 => 0x02
        case _ => data
      }
      value.toByte
    })

    val bitmapToRight = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x40
        case 0x0001 => 0x20
        case 0x0006 => 0x01
        case 0x0008 => 0x10
        case 0x000f => 0x88
        case 0x0140 => 0x04
        case 0x0147 => 0x02
        case 0x014f => 0x40
        case 0x0150 => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapToTop = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x40
        case 0x0005 => 0x02
        case 0x0006 => 0x01
        case 0x0007 => 0x08
        case 0x000e => 0x10
        case 0x000f => 0x01
        case 0x0146 => 0x04
        case 0x014e => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapToBottom = Array.fill(8000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0001 => 0x80
        case 0x0002 => 0x40
        case 0x0007 => 0x02
        case 0x0009 => 0x20
        case 0x0140 => 0x01
        case 0x0141 => 0x08
        case 0x0148 => 0x10
        case 0x0149 => 0x01
        case 0x0280 => 0x04
        case 0x0288 => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmap = setupTestBitmap

    assert(deep(bitmap.shift(-1, 0).get()) == deep(bitmapToLeft))
    assert(deep(bitmap.shift(1, 0).get()) == deep(bitmapToRight))
    assert(deep(bitmap.shift(0, -1).get()) == deep(bitmapToTop))
    assert(deep(bitmap.shift(0, 1).get()) == deep(bitmapToBottom))
  }

  "bitmap data slice at char level" in {

    val bitmapSliceHoriz = Array.fill(16){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x80
        case 0x0001 => 0x40
        case 0x0006 => 0x02
        case 0x0007 => 0x01
        case 0x0008 => 0x20
        case 0x000f => 0x10
        case _ => data
      }
      value.toByte
    })

    val bitmapSliceVert = Array.fill(16){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x80
        case 0x0001 => 0x40
        case 0x0006 => 0x02
        case 0x0007 => 0x01
        case 0x0008 => 0x08
        case 0x000f => 0x04
        case _ => data
      }
      value.toByte
    })

    val bitmapSliceRight = Array.fill(8){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x20
        case 0x0007 => 0x10
        case _ => data
      }
      value.toByte
    })

    val bitmapSliceLower = Array.fill(8){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x08
        case 0x0007 => 0x04
        case _ => data
      }
      value.toByte
    })

    val bitmap = setupTestBitmap

    assert(deep(bitmap.slice(0, 0, 16, 8).get()) == deep(bitmapSliceHoriz))
    assert(deep(bitmap.slice(0, 0, 8, 16).get()) == deep(bitmapSliceVert))
    assert(deep(bitmap.slice(8, 0, 8, 8).get()) == deep(bitmapSliceRight))
    assert(deep(bitmap.slice(0, 8, 8, 8).get()) == deep(bitmapSliceLower))

    intercept[IndexOutOfBoundsException] {
      bitmap.slice(0, 0, 328, 200)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.slice(0, 0, 320, 208)
    }
  }

  "bitmap data slice at pixel level" in {

    val bitmapSliceHoriz = Array.fill(8){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapSliceVert = Array.fill(8){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x80
        case _ => data
      }
      value.toByte
    })

    val bitmapSliceRight = Array.fill(8){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x10
        case _ => data
      }
      value.toByte
    })

    val bitmapSliceLower = Array.fill(8){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0002 => 0x02
        case 0x0004 => 0x08
        case _ => data
      }
      value.toByte
    })

    val bitmap = setupTestBitmap

    assert(deep(bitmap.slice(0, 0, 2, 1).get()) == deep(bitmapSliceHoriz))
    assert(deep(bitmap.slice(0, 0, 1, 2).get()) == deep(bitmapSliceVert))
    assert(deep(bitmap.slice(7, 0, 4, 4).get()) == deep(bitmapSliceRight))
    assert(deep(bitmap.slice(0, 4, 7, 7).get()) == deep(bitmapSliceLower))

    intercept[IndexOutOfBoundsException] {
      bitmap.slice(0, 0, 321, 200)
    }
    intercept[IndexOutOfBoundsException] {
      bitmap.slice(0, 0, 320, 201)
    }
  }
}
