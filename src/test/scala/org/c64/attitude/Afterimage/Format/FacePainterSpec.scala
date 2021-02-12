package org.c64.attitude.Afterimage
package Format

import org.scalatest.freespec.AnyFreeSpec

import Memory.Address
import Mode.MultiColour

class FacePainterFormatSpec extends AnyFreeSpec {

  "face painter format create" in {
    val addr: Address = FacePainter.load
    val data: Array[Byte] = Array.fill(FacePainter.size){0x00}
    assert(FacePainter(addr, data).isInstanceOf[FacePainter])
  }

  "face painter format invalid address" in {
    intercept[IllegalArgumentException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(FacePainter.size){0x00}
      FacePainter(addr, data)
    }
  }

  "face painter format too much data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = FacePainter.load
      val data: Array[Byte] = Array.fill(FacePainter.size + 1){0x00}
      FacePainter(addr, data)
    }
  }

  "face painter format too little data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = FacePainter.load
      val data: Array[Byte] = Array.fill(FacePainter.size - 1){0x00}
      FacePainter(addr, data)
    }
  }

  "face painter format empty data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = FacePainter.load
      val data: Array[Byte] = Array()
      FacePainter(addr, data)
    }
  }

  "face painter config file addr" in {
   assert(FacePainter.load.value == 0x4000)
  }

  "face painter config file size" in {
   assert(FacePainter.size == 0x2712)
  }

  def setupEmptyTestMultiColourData() = {
    MultiColour(
      Array.fill(MultiColour.size("bitmap")){0x00.toByte},
      Array.fill(MultiColour.size("screen")){0x00.toByte},
      Array.fill(MultiColour.size("colors")){0x00.toByte},
      0x00.toByte,
      0x00.toByte
    )
  }

  def setupEmptyTestFacePainterData() = {
    FacePainter(
      FacePainter.load,
      Array.fill(FacePainter.size){0x00}
    )
  }

  "multicolour data serialization" in {
    val multiColourImage = setupEmptyTestMultiColourData()
    val facePainterImage = setupEmptyTestFacePainterData()
    assert(FacePainter(multiColourImage) equals facePainterImage)
  }
}
