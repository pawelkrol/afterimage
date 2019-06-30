package org.c64.attitude.Afterimage
package Format

import org.scalatest.FreeSpec

import Memory.Address
import Mode.MultiColour

class AdvancedArtStudioFormatSpec extends FreeSpec {

  "advanced art studio format create" in {
    val addr: Address = AdvancedArtStudio.load
    val data: Array[Byte] = Array.fill(AdvancedArtStudio.size){0x00}
    assert(AdvancedArtStudio(addr, data).isInstanceOf[AdvancedArtStudio])
  }

  "advanced art studio format invalid address" in {
    intercept[IllegalArgumentException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(AdvancedArtStudio.size){0x00}
      AdvancedArtStudio(addr, data)
    }
  }

  "advanced art studio format too much data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = AdvancedArtStudio.load
      val data: Array[Byte] = Array.fill(AdvancedArtStudio.size + 1){0x00}
      AdvancedArtStudio(addr, data)
    }
  }

  "advanced art studio format too little data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = AdvancedArtStudio.load
      val data: Array[Byte] = Array.fill(AdvancedArtStudio.size - 1){0x00}
      AdvancedArtStudio(addr, data)
    }
  }

  "advanced art studio format empty data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = AdvancedArtStudio.load
      val data: Array[Byte] = Array()
      AdvancedArtStudio(addr, data)
    }
  }

  "advanced art studio config file addr" in {
   assert(AdvancedArtStudio.load.value == 0x2000)
  }

  "advanced art studio config file size" in {
   assert(AdvancedArtStudio.size == 0x2720)
  }

  def setupEmptyTestMultiColourData() = {
    MultiColour(
      Array.fill(MultiColour.size("bitmap")){0x00.toByte},
      Array.fill(MultiColour.size("screen")){0x00.toByte},
      Array.fill(MultiColour.size("colors")){0x00.toByte},
      0x00.toByte
    )
  }

  def setupEmptyTestAdvancedArtStudioData() = {
    AdvancedArtStudio(
      AdvancedArtStudio.load,
      Array.fill(AdvancedArtStudio.size){0x00}
    )
  }

  "multicolour data serialization" in {
    val multiColourImage = setupEmptyTestMultiColourData()
    val advancedArtStudioImage = setupEmptyTestAdvancedArtStudioData()
    assert(AdvancedArtStudio(multiColourImage) equals advancedArtStudioImage)
  }
}
