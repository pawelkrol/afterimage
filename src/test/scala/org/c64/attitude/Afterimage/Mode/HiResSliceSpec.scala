package org.c64.attitude.Afterimage
package Mode

import org.scalatest.FreeSpec

import Mode.Data.{Bitmap,Screen}
import Util.ArrayHelper.deep

class HiResSliceSpec extends FreeSpec {

  val border = Some(0x00.toByte)

  "create hires with screen slice success" in {

    // HiResSlice of an exact size of a full-screen HiRes image:
    val bitmap01 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    val screen01 = Some(Screen(Array.fill(HiRes.size("screen")){0x00.toByte}, 40, 25))
    assert(HiResSlice(bitmap01, screen01, border, 320, 200).isInstanceOf[HiResSlice])

    // HiResSlice of a smaller width than a full-screen HiRes image:
    val bitmapSize02 = HiRes.size("bitmap") - 0x08 * 0x19
    val screenSize02 = HiRes.size("screen") - 0x01 * 0x19
    val bitmap02 = Bitmap(Array.fill(bitmapSize02){0x00.toByte}, 39, 25)
    val screen02 = Some(Screen(Array.fill(screenSize02){0x00.toByte}, 39, 25))
    assert(HiResSlice(bitmap02, screen02, border, 312, 200).isInstanceOf[HiResSlice])

    // HiResSlice of a smaller height than a full-screen HiRes image:
    val bitmapSize03 = HiRes.size("bitmap") - 0x08 * 0x28
    val screenSize03 = HiRes.size("screen") - 0x01 * 0x28
    val bitmap03 = Bitmap(Array.fill(bitmapSize03){0x00.toByte}, 40, 24)
    val screen03 = Some(Screen(Array.fill(screenSize03){0x00.toByte}, 40, 24))
    assert(HiResSlice(bitmap03, screen03, border, 320, 192).isInstanceOf[HiResSlice])

    // HiResSlice of a minimum possible size of image data:
    val bitmap04 = Bitmap(Array.fill(0x08){0x00.toByte}, 1, 1)
    val screen04 = Some(Screen(Array.fill(0x01){0x00.toByte}, 1, 1))
    assert(HiResSlice(bitmap04, screen04, border, 8, 8).isInstanceOf[HiResSlice])
  }

  "create hires without screen slice success" in {

    // HiResSlice with more bitmap data provided than indicated by number of image columns:
    val bitmapSize01 = HiRes.size("bitmap") - 0x08 * 0x19
    val bitmap01 = Bitmap(Array.fill(bitmapSize01){0x00.toByte}, 39, 25)
    assert(HiResSlice(bitmap01, None, border, 305, 200).isInstanceOf[HiResSlice])

    // HiResSlice of an exact size of a full-screen HiRes image:
    val bitmap02 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    assert(HiResSlice(bitmap02, None, border, 320, 200).isInstanceOf[HiResSlice])

    // HiResSlice of a smaller size than a full-screen HiRes image:
    val bitmapSize03 = HiRes.size("bitmap") - 0x08 * 0x28 - 0x08 * 0x18
    val bitmap03 = Bitmap(Array.fill(bitmapSize03){0x00.toByte}, 39, 24)
    assert(HiResSlice(bitmap03, None, border, 312, 192).isInstanceOf[HiResSlice])

    // HiResSlice of a smaller dimensions than a full-screen HiRes image:
    val bitmap04 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    assert(HiResSlice(bitmap04, None, border, 319, 200).isInstanceOf[HiResSlice])
    assert(HiResSlice(bitmap04, None, border, 313, 200).isInstanceOf[HiResSlice])
    assert(HiResSlice(bitmap04, None, border, 320, 199).isInstanceOf[HiResSlice])
    assert(HiResSlice(bitmap04, None, border, 320, 193).isInstanceOf[HiResSlice])

    // HiResSlice of a minimum possible size of image data:
    val bitmap05 = Bitmap(Array.fill(0x08){0x00.toByte}, 1, 1)
    assert(HiResSlice(bitmap05, None, border, 1, 1).isInstanceOf[HiResSlice])
  }

  "create hires with screen slice failure" in {

    // HiResSlice with image width/height exceeding maximum allowed size:
    val bitmap01 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    val screen01 = Some(Screen(Array.fill(HiRes.size("screen")){0x00.toByte}, 40, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap01, screen01, border, 328, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap01, screen01, border, 328, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap01, screen01, border, 320, 208)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap01, screen01, border, 320, 208)
    }

    // HiResSlice of an undefined slice width/height:
    val bitmap04 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    val screen04 = Some(Screen(Array.fill(HiRes.size("screen")){0x00.toByte}, 40, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap04, screen04, border, 0, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap04, screen04, border, 320, 0)
    }

    // HiResSlice of an undefined slice width/height:
    val bitmap05 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    val screen05 = Some(Screen(Array.fill(HiRes.size("screen")){0x00.toByte}, 40, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap05, screen05, border, 0, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap05, screen05, border, 320, 0)
    }

    // HiResSlice with image width/height not divisible by 8 (although with valid data):
    val bitmap09 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    val screen09 = Some(Screen(Array.fill(HiRes.size("screen")){0x00.toByte}, 40, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap09, screen09, border, 319, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap09, screen09, border, 320, 199)
    }

    // HiResSlice with more bitmap and screen data provided than indicated by number of image rows:
    val bitmapSize10 = HiRes.size("bitmap") - 0x08 * 0x28
    val screenSize10 = HiRes.size("screen") - 0x01 * 0x28
    val bitmap10 = Bitmap(Array.fill(bitmapSize10){0x00.toByte}, 40, 24)
    val screen10 = Some(Screen(Array.fill(screenSize10){0x00.toByte}, 40, 24))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap10, screen10, border, 320, 184)
    }

    // HiResSlice with less bitmap and screen data provided than indicated by number of image rows:
    val bitmapSize11 = HiRes.size("bitmap") - 0x08 * 0x28
    val screenSize11 = HiRes.size("screen") - 0x01 * 0x28
    val bitmap11 = Bitmap(Array.fill(bitmapSize11){0x00.toByte}, 40, 24)
    val screen11 = Some(Screen(Array.fill(screenSize11){0x00.toByte}, 40, 24))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap11, screen11, border, 320, 200)
    }

    // HiResSlice with more bitmap and screen data provided than indicated by number of image columns:
    val bitmapSize12 = HiRes.size("bitmap") - 0x08 * 0x19
    val screenSize12 = HiRes.size("screen") - 0x01 * 0x19
    val bitmap12 = Bitmap(Array.fill(bitmapSize12){0x00.toByte}, 39, 25)
    val screen12 = Some(Screen(Array.fill(screenSize12){0x00.toByte}, 39, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap12, screen12, border, 304, 200)
    }

    // HiResSlice with less bitmap and screen data provided than indicated by number of image columns:
    val bitmapSize13 = HiRes.size("bitmap") - 0x08 * 0x19
    val screenSize13 = HiRes.size("screen") - 0x01 * 0x19
    val bitmap13 = Bitmap(Array.fill(bitmapSize13){0x00.toByte}, 39, 25)
    val screen13 = Some(Screen(Array.fill(screenSize13){0x00.toByte}, 39, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap13, screen13, border, 320, 200)
    }

    // HiResSlice with more screen data provided than bitmap data was given:
    val bitmapSize14 = HiRes.size("bitmap") - 0x08 * 0x19
    val bitmap14 = Bitmap(Array.fill(bitmapSize14){0x00.toByte}, 39, 25)
    val screen14 = Some(Screen(Array.fill(HiRes.size("screen")){0x00.toByte}, 40, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap14, screen14, border, 312, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap14, screen14, border, 320, 200)
    }

    // HiResSlice with more bitmap data provided than screen data was given:
    val screenSize15 = HiRes.size("screen") - 0x01 * 0x19
    val bitmap15 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    val screen15 = Some(Screen(Array.fill(screenSize15){0x00.toByte}, 39, 25))
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap15, screen15, border, 312, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap15, screen15, border, 320, 200)
    }
  }

  "create hires without screen slice failure" in {

    // HiResSlice with more bitmap data provided than indicated by number of image rows/columns:
    val bitmap01 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap01, None, border, 321, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap01, None, border, 320, 201)
    }

    // HiResSlice of an undefined slice width/height:
    val bitmap02 = Bitmap(Array.fill(HiRes.size("bitmap")){0x00.toByte}, 40, 25)
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap02, None, border, 0, 200)
    }
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap02, None, border, 320, 0)
    }

    // HiResSlice with more bitmap data provided than indicated by number of image rows:
    val bitmapSize04 = HiRes.size("bitmap") - 0x08 * 0x28
    val bitmap04 = Bitmap(Array.fill(bitmapSize04){0x00.toByte}, 40, 24)
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap04, None, border, 320, 184)
    }

    // HiResSlice with less bitmap data provided than indicated by number of image rows:
    val bitmapSize05 = HiRes.size("bitmap") - 0x08 * 0x28
    val bitmap05 = Bitmap(Array.fill(bitmapSize05){0x00.toByte}, 40, 24)
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap05, None, border, 320, 200)
    }

    // HiResSlice with more bitmap data provided than indicated by number of image columns:
    val bitmapSize06 = HiRes.size("bitmap") - 0x08 * 0x19
    val bitmap06 = Bitmap(Array.fill(bitmapSize06){0x00.toByte}, 39, 25)
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap06, None, border, 304, 200)
    }

    // HiResSlice with less bitmap data provided than indicated by number of image columns:
    val bitmapSize07 = HiRes.size("bitmap") - 0x08 * 0x19
    val bitmap07 = Bitmap(Array.fill(bitmapSize07){0x00.toByte}, 39, 25)
    intercept[IllegalArgumentException] {
      HiResSlice(bitmap07, None, border, 320, 200)
    }
  }

  def setupHiResImageWithScreen() = {
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
        case 0x0148 => 0x01
        case 0x014f => 0x80
        case _ => data
      }
      value.toByte
    })

    val screen = Array.fill(HiRes.size("screen")){0xbc.toByte}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x01 => 0x10
        case 0x28 => 0xd5
        case 0x29 => 0xe6
        case _    => data
      }
      value.toByte
    })

    HiRes(bitmap, screen)
  }

  def setupHiResImageWithoutScreen() = {
    val bitmap = Array.fill(HiRes.size("bitmap")){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0xff
        case 0x0001 => 0xfe
        case 0x0002 => 0xfc
        case 0x0003 => 0xf8
        case 0x0004 => 0xf0
        case 0x0005 => 0xe0
        case 0x0006 => 0xc0
        case 0x0007 => 0x80
        case _ => data
      }
      value.toByte
    })

    HiRes(bitmap)
  }

  "get pixel from hires top slice with screen" in {
    val hiresSlice = setupHiResImageWithScreen().slice(0, 0, 24, 24)

    assert(hiresSlice.pixel(0, 0) == 0x0b)
    assert(hiresSlice.pixel(1, 0) == 0x0c)
    assert(hiresSlice.pixel(0, 1) == 0x0c)
    assert(hiresSlice.pixel(1, 1) == 0x0b)

    assert(hiresSlice.pixel(6, 6) == 0x0b)
    assert(hiresSlice.pixel(7, 6) == 0x0c)
    assert(hiresSlice.pixel(6, 7) == 0x0c)
    assert(hiresSlice.pixel(7, 7) == 0x0b)

    assert(hiresSlice.pixel(10, 0) == 0x01)
    assert(hiresSlice.pixel(11, 0) == 0x00)
    assert(hiresSlice.pixel(10, 7) == 0x00)
    assert(hiresSlice.pixel(11, 7) == 0x01)

    assert(hiresSlice.pixel(4, 8) == 0x0d)
    assert(hiresSlice.pixel(5, 8) == 0x05)
    assert(hiresSlice.pixel(4, 15) == 0x05)
    assert(hiresSlice.pixel(5, 15) == 0x0d)
  }

  "get pixel from hires shifted slice with screen" in {
    val hiresSlice = setupHiResImageWithScreen().slice(8, 8, 32, 32)

    assert(hiresSlice.pixel(7, 0) == 0x0e)
    assert(hiresSlice.pixel(6, 0) == 0x06)
    assert(hiresSlice.pixel(0, 7) == 0x0e)
    assert(hiresSlice.pixel(1, 7) == 0x06)
  }

  "get pixel from hires top slice without screen" in {
    val hiresSlice = setupHiResImageWithoutScreen().slice(0, 0, 24, 24)

    assert(hiresSlice.pixel(0, 0) == 0x0b)
    assert(hiresSlice.pixel(7, 0) == 0x0b)
    assert(hiresSlice.pixel(0, 1) == 0x0b)
    assert(hiresSlice.pixel(7, 1) == 0x0c)

    assert(hiresSlice.pixel(2, 2) == 0x0b)
    assert(hiresSlice.pixel(5, 2) == 0x0b)
    assert(hiresSlice.pixel(2, 3) == 0x0b)
    assert(hiresSlice.pixel(5, 3) == 0x0c)

    assert(hiresSlice.pixel(2, 4) == 0x0b)
    assert(hiresSlice.pixel(5, 4) == 0x0c)
    assert(hiresSlice.pixel(2, 5) == 0x0b)
    assert(hiresSlice.pixel(5, 5) == 0x0c)

    assert(hiresSlice.pixel(0, 6) == 0x0b)
    assert(hiresSlice.pixel(1, 6) == 0x0b)
    assert(hiresSlice.pixel(2, 6) == 0x0c)
    assert(hiresSlice.pixel(0, 7) == 0x0b)
    assert(hiresSlice.pixel(1, 7) == 0x0c)
    assert(hiresSlice.pixel(2, 7) == 0x0c)
  }

  "get pixel from hires shifted slice without screen" in {
    val hiresSlice = setupHiResImageWithoutScreen().slice(1, 2, 48, 30)

    assert(hiresSlice.pixel(0, 0) == 0x0b)
    assert(hiresSlice.pixel(4, 0) == 0x0b)
    assert(hiresSlice.pixel(5, 0) == 0x0c)
    assert(hiresSlice.pixel(7, 0) == 0x0c)

    assert(hiresSlice.pixel(0, 1) == 0x0b)
    assert(hiresSlice.pixel(3, 1) == 0x0b)
    assert(hiresSlice.pixel(4, 1) == 0x0c)
    assert(hiresSlice.pixel(7, 1) == 0x0c)

    assert(hiresSlice.pixel(2, 2) == 0x0b)
    assert(hiresSlice.pixel(3, 2) == 0x0c)
    assert(hiresSlice.pixel(1, 3) == 0x0b)
    assert(hiresSlice.pixel(2, 3) == 0x0c)

    assert(hiresSlice.pixel(0, 4) == 0x0b)
    assert(hiresSlice.pixel(1, 4) == 0x0c)
    assert(hiresSlice.pixel(0, 5) == 0x0c)
    assert(hiresSlice.pixel(1, 5) == 0x0c)

    assert(hiresSlice.pixel(0, 6) == 0x0c)
    assert(hiresSlice.pixel(7, 6) == 0x0c)
    assert(hiresSlice.pixel(0, 7) == 0x0c)
    assert(hiresSlice.pixel(7, 7) == 0x0c)
  }

  "create hires without screen shifted slice" in {

    val hiresSlice = setupHiResImageWithoutScreen().slice(1, 1, 5, 5)
    val bitmap = List(0xf8, 0xf8, 0xf0, 0xe0, 0xc0, 0x00, 0x00, 0x00).map(_.toByte).toArray
    assert(deep(hiresSlice.bitmap.get) == deep(bitmap))
  }

  "get pixel from hires slice created from slice" in {
    val hiresSlice = setupHiResImageWithScreen().slice(0, 0, 16, 32).slice(0, 8, 8, 16)

    assert(hiresSlice.pixel(4, 0) == 0x0d)
    assert(hiresSlice.pixel(5, 0) == 0x05)
    assert(hiresSlice.pixel(4, 7) == 0x05)
    assert(hiresSlice.pixel(5, 7) == 0x0d)
  }
}
