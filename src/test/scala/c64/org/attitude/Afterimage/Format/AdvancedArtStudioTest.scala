package org.c64.attitude.Afterimage
package Format

import org.scalatest.Suite

import Memory.Address
import Mode.MultiColour

class AdvancedArtStudioFormatTestSuite extends Suite {

  def testAdvancedArtStudioFormatCreate {
    val addr: Address = AdvancedArtStudio.load
    val data: Array[Byte] = Array.fill(AdvancedArtStudio.size){0x00}
    assert(AdvancedArtStudio(addr, data).isInstanceOf[AdvancedArtStudio])
  }

  def testAdvancedArtStudioFormatInvalidAddress {
    intercept[InvalidImageDataException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(AdvancedArtStudio.size){0x00}
      AdvancedArtStudio(addr, data)
    }
  }

  def testAdvancedArtStudioFormatTooMuchData {
    intercept[InvalidImageDataException] {
      val addr: Address = AdvancedArtStudio.load
      val data: Array[Byte] = Array.fill(AdvancedArtStudio.size + 1){0x00}
      AdvancedArtStudio(addr, data)
    }
  }

  def testAdvancedArtStudioFormatTooLittleData {
    intercept[InvalidImageDataException] {
      val addr: Address = AdvancedArtStudio.load
      val data: Array[Byte] = Array.fill(AdvancedArtStudio.size - 1){0x00}
      AdvancedArtStudio(addr, data)
    }
  }

  def testAdvancedArtStudioFormatEmptyData {
    intercept[InvalidImageDataException] {
      val addr: Address = AdvancedArtStudio.load
      val data: Array[Byte] = Array()
      AdvancedArtStudio(addr, data)
    }
  }

  def testAdvancedArtStudioConfigFileAddr {
   assert(AdvancedArtStudio.load.value == 0x2000)
  }

  def testAdvancedArtStudioConfigFileSize {
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

  def testMultiColourDataSerialization {
    val multiColourImage = setupEmptyTestMultiColourData()
    val AdvancedArtStudioImage = setupEmptyTestAdvancedArtStudioData()
    assert(AdvancedArtStudio(multiColourImage) equals AdvancedArtStudioImage)
  }
}
