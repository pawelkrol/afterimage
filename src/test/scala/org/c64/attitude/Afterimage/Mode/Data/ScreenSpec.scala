package org.c64.attitude.Afterimage
package Mode.Data

import org.scalatest.freespec.AnyFreeSpec

import Util.ArrayHelper.deep

class ScreenSpec extends AnyFreeSpec {

  "create default screen" in {

    assert(Screen().isInstanceOf[Screen])
  }

  "create screen with size" in {

    assert(Screen(40, 25).isInstanceOf[Screen])
    assert(Screen(39, 25).isInstanceOf[Screen])
    assert(Screen(40, 24).isInstanceOf[Screen])
    assert(Screen(40, 1).isInstanceOf[Screen])
    assert(Screen(1, 25).isInstanceOf[Screen])
    assert(Screen(1, 1).isInstanceOf[Screen])

    intercept[IllegalArgumentException] {
      Screen(40, 26)
    }
    intercept[IllegalArgumentException] {
      Screen(41, 25)
    }
    intercept[IllegalArgumentException] {
      Screen(40, 0)
    }
    intercept[IllegalArgumentException] {
      Screen(0, 25)
    }
    intercept[IllegalArgumentException] {
      Screen(0, 0)
    }
    intercept[IllegalArgumentException] {
      Screen(-1, -1)
    }
  }

  "create screen with data" in {

    assert(Screen(Array.fill(1000){0x00}, 40, 25).isInstanceOf[Screen])
    assert(Screen(Array.fill(975){0x00}, 39, 25).isInstanceOf[Screen])
    assert(Screen(Array.fill(960){0x00}, 40, 24).isInstanceOf[Screen])
    assert(Screen(Array.fill(40){0x00}, 40, 1).isInstanceOf[Screen])
    assert(Screen(Array.fill(25){0x00}, 1, 25).isInstanceOf[Screen])
    assert(Screen(Array.fill(1){0x00}, 1, 1).isInstanceOf[Screen])

    intercept[IllegalArgumentException] {
      Screen(Array.fill(1001){0x00}, 40, 25)
    }
    intercept[IllegalArgumentException] {
      Screen(Array.fill(999){0x00}, 40, 25)
    }
    intercept[IllegalArgumentException] {
      Screen(Array(), 40, 25)
    }
  }

  def setupTestScreen = {

    val screen = Array.fill(1000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x02
        case 0x0027 => 0x03
        case 0x0028 => 0x04
        case 0x0029 => 0x05
        case _ => data
      }
      value.toByte
    })

    Screen(screen, 40, 25)
  }

  "get value" in {

    val screen = setupTestScreen

    assert(screen.get(0, 0) == 0x01.toByte)
    assert(screen.get(1, 0) == 0x02.toByte)
    assert(screen.get(39, 0) == 0x03.toByte)
    assert(screen.get(0, 1) == 0x04.toByte)
    assert(screen.get(1, 1) == 0x05.toByte)

    intercept[IllegalArgumentException] {
      screen.get(-1, -1)
    }
    intercept[IllegalArgumentException] {
      screen.get(-1, 0)
    }
    intercept[IllegalArgumentException] {
      screen.get(0, -1)
    }
    intercept[IllegalArgumentException] {
      screen.get(40, 0)
    }
    intercept[IllegalArgumentException] {
      screen.get(0, 25)
    }
    intercept[IllegalArgumentException] {
      screen.get(40, 25)
    }
  }

  "screen data serialization" in {

    assert(deep(Screen().get()) == deep(Array.fill(0x03e8){0x00}))
    assert(deep(Screen(1, 1).get()) == deep(Array.fill(0x01){0x00}))
    assert(deep(Screen(40, 25).get()) == deep(Array.fill(0x03e8){0x00}))

    val screen = Array.fill(1000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x02
        case 0x0027 => 0x03
        case 0x0028 => 0x04
        case 0x0029 => 0x05
        case _ => data
      }
      value.toByte
    })

    assert(deep(setupTestScreen.get()) == deep(screen))
  }

  "screen data shift" in {

    val screenToLeft = Array.fill(1000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x02
        case 0x0026 => 0x03
        case 0x0028 => 0x05
        case _ => data
      }
      value.toByte
    })

    val screenToRight = Array.fill(1000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0001 => 0x01
        case 0x0002 => 0x02
        case 0x0029 => 0x04
        case 0x002a => 0x05
        case _ => data
      }
      value.toByte
    })

    val screenToTop = Array.fill(1000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x04
        case 0x0001 => 0x05
        case _ => data
      }
      value.toByte
    })

    val screenToBottom = Array.fill(1000){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0028 => 0x01
        case 0x0029 => 0x02
        case 0x004f => 0x03
        case 0x0050 => 0x04
        case 0x0051 => 0x05
        case _ => data
      }
      value.toByte
    })

    val screen = setupTestScreen

    assert(deep(screen.shift(-1, 0).get()) == deep(screenToLeft))
    assert(deep(screen.shift(1, 0).get()) == deep(screenToRight))
    assert(deep(screen.shift(0, -1).get()) == deep(screenToTop))
    assert(deep(screen.shift(0, 1).get()) == deep(screenToBottom))

    assert(deep(screen.shift(-40, 0).get()) == deep(Array.fill(1000){0x00}))
    assert(deep(screen.shift(40, 0).get()) == deep(Array.fill(1000){0x00}))
    assert(deep(screen.shift(0, -25).get()) == deep(Array.fill(1000){0x00}))
    assert(deep(screen.shift(0, 25).get()) == deep(Array.fill(1000){0x00}))
  }

  "screen data slice" in {

    val screenSliceHoriz = Array.fill(2){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x02
        case _ => data
      }
      value.toByte
    })

    val screenSliceVert = Array.fill(2){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x04
        case _ => data
      }
      value.toByte
    })

    val screenSliceRight = Array.fill(1){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x02
        case _ => data
      }
      value.toByte
    })

    val screenSliceLower = Array.fill(1){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x04
        case _ => data
      }
      value.toByte
    })

    val screenSliceWider = Array.fill(12){0x00}.zipWithIndex.map(zip => {
      val (data, index) = zip
      val value = index match {
        case 0x0000 => 0x01
        case 0x0001 => 0x02
        case 0x0004 => 0x04
        case 0x0005 => 0x05
        case _ => data
      }
      value.toByte
    })

    val screen = setupTestScreen

    assert(deep(screen.slice(0, 0, 2, 1).get()) == deep(screenSliceHoriz))
    assert(deep(screen.slice(0, 0, 1, 2).get()) == deep(screenSliceVert))
    assert(deep(screen.slice(1, 0, 1, 1).get()) == deep(screenSliceRight))
    assert(deep(screen.slice(0, 1, 1, 1).get()) == deep(screenSliceLower))
    assert(deep(screen.slice(0, 0, 4, 3).get()) == deep(screenSliceWider))

    intercept[IllegalArgumentException] {
      screen.slice(0, 0, 41, 25)
    }
    intercept[IllegalArgumentException] {
      screen.slice(0, 0, 40, 26)
    }
  }
}
