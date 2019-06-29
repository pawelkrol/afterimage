package org.c64.attitude.Afterimage
package Format

import org.scalatest.FreeSpec

import Memory.Address
import Mode.HiRes

class ArtStudioFormatSpec extends FreeSpec {

  "art studio format create" in {
    val addr: Address = ArtStudio.load
    val data: Array[Byte] = Array.fill(ArtStudio.size){0x00}
    assert(ArtStudio(addr, data).isInstanceOf[ArtStudio])
  }

  "art studio format invalid address" in {
    intercept[IllegalArgumentException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(ArtStudio.size){0x00}
      ArtStudio(addr, data)
    }
  }

  "art studio format too much data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = ArtStudio.load
      val data: Array[Byte] = Array.fill(ArtStudio.size + 1){0x00}
      ArtStudio(addr, data)
    }
  }

  "art studio format too little data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = ArtStudio.load
      val data: Array[Byte] = Array.fill(ArtStudio.size - 1){0x00}
      ArtStudio(addr, data)
    }
  }

  "art studio format empty data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = ArtStudio.load
      val data: Array[Byte] = Array()
      ArtStudio(addr, data)
    }
  }

  "art studio config file addr" in {
   assert(ArtStudio.load.value == 0x2000)
  }

  "art studio config file size" in {
   assert(ArtStudio.size == 0x2328)
  }

  def setupEmptyTestHiResData() = {
    HiRes(
      Array.fill(HiRes.size("bitmap")){0x00.toByte},
      Array.fill(HiRes.size("screen")){0x00.toByte}
    )
  }

  def setupEmptyTestArtStudioData() = {
    ArtStudio(
      ArtStudio.load,
      Array.fill(ArtStudio.size){0x00}
    )
  }

  "hires data serialization" in {
    val hiResImage = setupEmptyTestHiResData()
    val ArtStudioImage = setupEmptyTestArtStudioData()
    assert(ArtStudio(hiResImage) equals ArtStudioImage)
  }
}
