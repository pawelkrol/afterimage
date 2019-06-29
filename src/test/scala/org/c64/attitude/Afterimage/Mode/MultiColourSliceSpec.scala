package org.c64.attitude.Afterimage
package Mode

import org.scalatest.FreeSpec

import Mode.Data.{Bitmap,Screen}

class MultiColourSliceSpec extends FreeSpec {

  val bckgrd = 0x00.toByte
  val border = Some(0x00.toByte)

  "create multicolour slice success" in {

    // MultiColourSlice of an exact size of a full-screen MultiColour image:
    val bitmap01 = Bitmap(Array.fill(MultiColour.size("bitmap")){0x00.toByte}, 40, 25)
    val screen01 = Screen(Array.fill(MultiColour.size("screen")){0x00.toByte}, 40, 25)
    val colors01 = Screen(Array.fill(MultiColour.size("colors")){0x00.toByte}, 40, 25)
    assert(MultiColourSlice(bitmap01, screen01, colors01, border, bckgrd, 160, 200).isInstanceOf[MultiColourSlice])

    // MultiColourSlice of a smaller width than a full-screen MultiColour image:
    val bitmapSize02 = MultiColour.size("bitmap") - 0x08 * 0x19
    val screenSize02 = MultiColour.size("screen") - 0x01 * 0x19
    val colorsSize02 = MultiColour.size("colors") - 0x01 * 0x19
    val bitmap02 = Bitmap(Array.fill(bitmapSize02){0x00.toByte}, 39, 25)
    val screen02 = Screen(Array.fill(screenSize02){0x00.toByte}, 39, 25)
    val colors02 = Screen(Array.fill(colorsSize02){0x00.toByte}, 39, 25)
    assert(MultiColourSlice(bitmap02, screen02, colors02, border, bckgrd, 156, 200).isInstanceOf[MultiColourSlice])

    // MultiColourSlice of a smaller height than a full-screen MultiColour image:
    val bitmapSize03 = MultiColour.size("bitmap") - 0x08 * 0x28
    val screenSize03 = MultiColour.size("screen") - 0x01 * 0x28
    val colorsSize03 = MultiColour.size("colors") - 0x01 * 0x28
    val bitmap03 = Bitmap(Array.fill(bitmapSize03){0x00.toByte}, 40, 24)
    val screen03 = Screen(Array.fill(screenSize03){0x00.toByte}, 40, 24)
    val colors03 = Screen(Array.fill(colorsSize03){0x00.toByte}, 40, 24)
    assert(MultiColourSlice(bitmap03, screen03, colors03, border, bckgrd, 160, 192).isInstanceOf[MultiColourSlice])

    // MultiColourSlice of a minimum possible size of image data:
    val bitmap04 = Bitmap(Array.fill(0x08){0x00.toByte}, 1, 1)
    val screen04 = Screen(Array.fill(0x01){0x00.toByte}, 1, 1)
    val colors04 = Screen(Array.fill(0x01){0x00.toByte}, 1, 1)
    assert(MultiColourSlice(bitmap04, screen04, colors04, border, bckgrd, 4, 8).isInstanceOf[MultiColourSlice])
  }

  "create multicolour with screen slice failure" in {

    // MultiColourSlice with image width exceeding maximum allowed size (although with valid data)
    // cannot be created, because it is impossible to construct invalid Bitmap/Screen instances:
    val bitmapSize01 = MultiColour.size("bitmap") + 0x08 * 0x19
    intercept[IllegalArgumentException] {
      Bitmap(Array.fill(bitmapSize01){0x00.toByte}, 41, 25)
    }
    val screenSize01 = MultiColour.size("screen") + 0x01 * 0x19
    intercept[IllegalArgumentException] {
      Screen(Array.fill(screenSize01){0x00.toByte}, 41, 25)
    }

    // MultiColourSlice of an empty bitmap or screen data cannot be created,
    // because it is impossible to construct invalid Bitmap/Screen instances:
    intercept[IllegalArgumentException] {
      Bitmap(Array[Byte](), 0, 0)
    }
    intercept[IllegalArgumentException] {
      Screen(Array[Byte](), 0, 0)
    }

    // MultiColourSlice with image height exceeding maximum allowed size (although with valid data)
    // cannot be created, because it is impossible to construct invalid Bitmap/Screen instances:
    val bitmapSize06 = MultiColour.size("bitmap") + 0x08 * 0x28
    intercept[IllegalArgumentException] {
      Bitmap(Array.fill(bitmapSize06){0x00.toByte}, 40, 26)
    }
    val screenSize06 = MultiColour.size("screen") + 0x01 * 0x28
    intercept[IllegalArgumentException] {
      Screen(Array.fill(screenSize06){0x00.toByte}, 40, 26)
    }

    // MultiColourSlice with image width/height not divisible by 4 (although with valid data):
    val bitmap09 = Bitmap(Array.fill(MultiColour.size("bitmap")){0x00.toByte}, 40, 25)
    val screen09 = Screen(Array.fill(MultiColour.size("screen")){0x00.toByte}, 40, 25)
    val colors09 = Screen(Array.fill(MultiColour.size("colors")){0x00.toByte}, 40, 25)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap09, screen09, colors09, border, bckgrd, 159, 200)
    }
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap09, screen09, colors09, border, bckgrd, 160, 199)
    }

    // MultiColourSlice with more bitmap and screen/colors data provided than indicated by number of image rows:
    val bitmapSize10 = MultiColour.size("bitmap") - 0x08 * 0x28
    val screenSize10 = MultiColour.size("screen") - 0x01 * 0x28
    val colorsSize10 = MultiColour.size("colors") - 0x01 * 0x28
    val bitmap10 = Bitmap(Array.fill(bitmapSize10){0x00.toByte}, 40, 24)
    val screen10 = Screen(Array.fill(screenSize10){0x00.toByte}, 40, 24)
    val colors10 = Screen(Array.fill(colorsSize10){0x00.toByte}, 40, 24)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap10, screen10, colors10, border, bckgrd, 160, 184)
    }

    // MultiColourSlice with less bitmap and screen/colors data provided than indicated by number of image rows:
    val bitmapSize11 = MultiColour.size("bitmap") - 0x08 * 0x28
    val screenSize11 = MultiColour.size("screen") - 0x01 * 0x28
    val colorsSize11 = MultiColour.size("colors") - 0x01 * 0x28
    val bitmap11 = Bitmap(Array.fill(bitmapSize11){0x00.toByte}, 40, 24)
    val screen11 = Screen(Array.fill(screenSize11){0x00.toByte}, 40, 24)
    val colors11 = Screen(Array.fill(colorsSize11){0x00.toByte}, 40, 24)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap11, screen11, colors11, border, bckgrd, 160, 200)
    }

    // MultiColourSlice with more bitmap and screen/colors data provided than indicated by number of image columns:
    val bitmapSize12 = MultiColour.size("bitmap") - 0x08 * 0x19
    val screenSize12 = MultiColour.size("screen") - 0x01 * 0x19
    val colorsSize12 = MultiColour.size("colors") - 0x01 * 0x19
    val bitmap12 = Bitmap(Array.fill(bitmapSize12){0x00.toByte}, 39, 25)
    val screen12 = Screen(Array.fill(screenSize12){0x00.toByte}, 39, 25)
    val colors12 = Screen(Array.fill(colorsSize12){0x00.toByte}, 39, 25)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap12, screen12, colors12, border, bckgrd, 152, 200)
    }

    // MultiColourSlice with less bitmap and screen/colors data provided than indicated by number of image columns:
    val bitmapSize13 = MultiColour.size("bitmap") - 0x08 * 0x19
    val screenSize13 = MultiColour.size("screen") - 0x01 * 0x19
    val colorsSize13 = MultiColour.size("colors") - 0x01 * 0x19
    val bitmap13 = Bitmap(Array.fill(bitmapSize13){0x00.toByte}, 39, 25)
    val screen13 = Screen(Array.fill(screenSize13){0x00.toByte}, 39, 25)
    val colors13 = Screen(Array.fill(colorsSize13){0x00.toByte}, 39, 25)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap13, screen13, colors13, border, bckgrd, 160, 200)
    }

    // MultiColourSlice with more screen data provided than bitmap/colors data was given:
    val bitmapSize14 = MultiColour.size("bitmap") - 0x08 * 0x19
    val colorsSize14 = MultiColour.size("colors") - 0x01 * 0x19
    val bitmap14 = Bitmap(Array.fill(bitmapSize14){0x00.toByte}, 39, 25)
    val screen14 = Screen(Array.fill(MultiColour.size("screen")){0x00.toByte}, 40, 25)
    val colors14 = Screen(Array.fill(colorsSize14){0x00.toByte}, 39, 25)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap14, screen14, colors14, border, bckgrd, 156, 200)
    }
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap14, screen14, colors14, border, bckgrd, 160, 200)
    }

    // MultiColourSlice with more bitmap data provided than screen/colors data was given:
    val screenSize15 = MultiColour.size("screen") - 0x01 * 0x19
    val colorsSize15 = MultiColour.size("colors") - 0x01 * 0x19
    val bitmap15 = Bitmap(Array.fill(MultiColour.size("bitmap")){0x00.toByte}, 40, 25)
    val screen15 = Screen(Array.fill(screenSize15){0x00.toByte}, 39, 25)
    val colors15 = Screen(Array.fill(colorsSize15){0x00.toByte}, 39, 25)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap15, screen15, colors15, border, bckgrd, 156, 200)
    }
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap15, screen15, colors15, border, bckgrd, 160, 200)
    }

    // MultiColourSlice with more colors data provided than bitmap/screen data was given:
    val bitmapSize19 = MultiColour.size("bitmap") - 0x08 * 0x19
    val screenSize19 = MultiColour.size("screen") - 0x01 * 0x19
    val bitmap19 = Bitmap(Array.fill(bitmapSize19){0x00.toByte}, 39, 25)
    val screen19 = Screen(Array.fill(screenSize19){0x00.toByte}, 39, 25)
    val colors19 = Screen(Array.fill(MultiColour.size("colors")){0x00.toByte}, 40, 25)
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap19, screen19, colors19, border, bckgrd, 156, 200)
    }
    intercept[IllegalArgumentException] {
      MultiColourSlice(bitmap19, screen19, colors19, border, bckgrd, 160, 200)
    }
  }

  def setupMultiColourImage() = {
    val bitmap = Array.fill(MultiColour.size("bitmap")){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0xe4 // 11100100
        case 0x0007 => 0x93 // 10010011
        case 0x000f => 0x4e // 01001110
        case 0x0140 => 0x39 // 00111001
        case 0x014f => 0xe1 // 11100001
        case _ => data
      }
      value.toByte
    })

    val screen = Array.fill(MultiColour.size("screen")){0x00.toByte}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x00 => 0x12
        case 0x01 => 0x34
        case 0x28 => 0x56
        case 0x29 => 0xde
        case _    => data
      }
      value.toByte
    })

    val colors = Array.fill(MultiColour.size("colors")){0x00.toByte}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x00 => 0x07
        case 0x01 => 0x08
        case 0x28 => 0x09
        case 0x29 => 0x0f
        case _    => data
      }
      value.toByte
    })

    MultiColour(bitmap, screen, colors, bckgrd)
  }

  "get slice from multicolour image success" in {

    val multiColourImage = setupMultiColourImage()

    assert(multiColourImage.slice(0, 24).isInstanceOf[MultiColourSlice])
    assert(multiColourImage.slice(0, 0).isInstanceOf[MultiColourSlice])
    assert(multiColourImage.slice(24, 24).isInstanceOf[MultiColourSlice])
  }

  "get slice from multicolour image failure" in {

    val multiColourImage = setupMultiColourImage()

    intercept[IllegalArgumentException] {
      multiColourImage.slice(-1, 5)
    }
    intercept[IllegalArgumentException] {
      multiColourImage.slice(-1, 5)
    }
  }

  "get pixel from multicolour top slice with screen" in {
    val multiColourSlice = setupMultiColourImage().slice(0, 2)

    assert(multiColourSlice.pixel(0, 0) == 0x07)
    assert(multiColourSlice.pixel(1, 0) == 0x02)
    assert(multiColourSlice.pixel(2, 0) == 0x01)
    assert(multiColourSlice.pixel(3, 0) == 0x00)

    assert(multiColourSlice.pixel(0, 7) == 0x02)
    assert(multiColourSlice.pixel(1, 7) == 0x01)
    assert(multiColourSlice.pixel(2, 7) == 0x00)
    assert(multiColourSlice.pixel(3, 7) == 0x07)

    assert(multiColourSlice.pixel(4, 7) == 0x03)
    assert(multiColourSlice.pixel(5, 7) == 0x00)
    assert(multiColourSlice.pixel(6, 7) == 0x08)
    assert(multiColourSlice.pixel(7, 7) == 0x04)

    assert(multiColourSlice.pixel(0, 8) == 0x00)
    assert(multiColourSlice.pixel(1, 8) == 0x09)
    assert(multiColourSlice.pixel(2, 8) == 0x06)
    assert(multiColourSlice.pixel(3, 8) == 0x05)

    assert(multiColourSlice.pixel(4, 15) == 0x0f)
    assert(multiColourSlice.pixel(5, 15) == 0x0e)
    assert(multiColourSlice.pixel(6, 15) == 0x00)
    assert(multiColourSlice.pixel(7, 15) == 0x0d)
  }

  "get pixel from multicolour shifted slice with screen" in {
    val multiColourSlice = setupMultiColourImage().slice(1, 4)

    assert(multiColourSlice.pixel(0, 0) == 0x00)
    assert(multiColourSlice.pixel(1, 0) == 0x09)
    assert(multiColourSlice.pixel(2, 0) == 0x06)
    assert(multiColourSlice.pixel(3, 0) == 0x05)

    assert(multiColourSlice.pixel(4, 7) == 0x0f)
    assert(multiColourSlice.pixel(5, 7) == 0x0e)
    assert(multiColourSlice.pixel(6, 7) == 0x00)
    assert(multiColourSlice.pixel(7, 7) == 0x0d)
  }
}
