package org.c64.attitude.Afterimage
package Format

import org.scalatest.Suite

import Memory.Address
import Mode.MultiColour

class KoalaPainterFormatTestSuite extends Suite {

  def testKoalaPainterFormatCreate {
    val addr: Address = KoalaPainter.load
    val data: Array[Byte] = Array.fill(KoalaPainter.size){0x00}
    assert(KoalaPainter(addr, data).isInstanceOf[KoalaPainter])
  }

  def testKoalaPainterFormatInvalidAddress {
    intercept[InvalidImageDataException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(KoalaPainter.size){0x00}
      KoalaPainter(addr, data)
    }
  }

  def testKoalaPainterFormatTooMuchData {
    intercept[InvalidImageDataException] {
      val addr: Address = KoalaPainter.load
      val data: Array[Byte] = Array.fill(KoalaPainter.size + 1){0x00}
      KoalaPainter(addr, data)
    }
  }

  def testKoalaPainterFormatTooLittleData {
    intercept[InvalidImageDataException] {
      val addr: Address = KoalaPainter.load
      val data: Array[Byte] = Array.fill(KoalaPainter.size - 1){0x00}
      KoalaPainter(addr, data)
    }
  }

  def testKoalaPainterFormatEmptyData {
    intercept[InvalidImageDataException] {
      val addr: Address = KoalaPainter.load
      val data: Array[Byte] = Array()
      KoalaPainter(addr, data)
    }
  }

  def testKoalaPainterConfigFileAddr {
   assert(KoalaPainter.load.value == 0x6000)
  }

  def testKoalaPainterConfigFileSize {
   assert(KoalaPainter.size == 0x2711)
  }

  def setupEmptyTestMultiColourData() = {
    MultiColour(
      Array.fill(MultiColour.size("bitmap")){0x00.toByte},
      Array.fill(MultiColour.size("screen")){0x00.toByte},
      Array.fill(MultiColour.size("colors")){0x00.toByte},
      0x00.toByte
    )
  }

  def setupEmptyTestKoalaPainterData() = {
    KoalaPainter(
      KoalaPainter.load,
      Array.fill(KoalaPainter.size){0x00}
    )
  }

  def testMultiColourDataSerialization {
    val multiColourImage = setupEmptyTestMultiColourData()
    val koalaPainterImage = setupEmptyTestKoalaPainterData()
    assert(KoalaPainter(multiColourImage) equals koalaPainterImage)
  }
}
