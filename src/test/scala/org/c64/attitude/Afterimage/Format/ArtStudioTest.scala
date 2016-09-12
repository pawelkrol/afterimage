package org.c64.attitude.Afterimage
package Format

import org.scalatest.Suite

import Memory.Address
import Mode.HiRes

class ArtStudioFormatTestSuite extends Suite {

  def testArtStudioFormatCreate {
    val addr: Address = ArtStudio.load
    val data: Array[Byte] = Array.fill(ArtStudio.size){0x00}
    assert(ArtStudio(addr, data).isInstanceOf[ArtStudio])
  }

  def testArtStudioFormatInvalidAddress {
    intercept[InvalidImageDataException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(ArtStudio.size){0x00}
      ArtStudio(addr, data)
    }
  }

  def testArtStudioFormatTooMuchData {
    intercept[InvalidImageDataException] {
      val addr: Address = ArtStudio.load
      val data: Array[Byte] = Array.fill(ArtStudio.size + 1){0x00}
      ArtStudio(addr, data)
    }
  }

  def testArtStudioFormatTooLittleData {
    intercept[InvalidImageDataException] {
      val addr: Address = ArtStudio.load
      val data: Array[Byte] = Array.fill(ArtStudio.size - 1){0x00}
      ArtStudio(addr, data)
    }
  }

  def testArtStudioFormatEmptyData {
    intercept[InvalidImageDataException] {
      val addr: Address = ArtStudio.load
      val data: Array[Byte] = Array()
      ArtStudio(addr, data)
    }
  }

  def testArtStudioConfigFileAddr {
   assert(ArtStudio.load.value == 0x2000)
  }

  def testArtStudioConfigFileSize {
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

  def testHiResDataSerialization {
    val hiResImage = setupEmptyTestHiResData()
    val ArtStudioImage = setupEmptyTestArtStudioData()
    assert(ArtStudio(hiResImage) equals ArtStudioImage)
  }
}
