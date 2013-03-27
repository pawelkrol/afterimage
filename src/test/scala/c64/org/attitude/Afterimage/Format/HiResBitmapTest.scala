package org.c64.attitude.Afterimage
package Format

import org.scalatest.Suite

import Memory.Address
import Mode.HiRes

class HiResBitmapFormatTestSuite extends Suite {

  def testHiResBitmapFormatCreate {
    val addr: Address = HiResBitmap.load
    val data: Array[Byte] = Array.fill(HiResBitmap.size){0x00}
    assert(HiResBitmap(addr, data).isInstanceOf[HiResBitmap])
  }

  def testHiResBitmapFormatIrrelevantAddress {
    val addr: Address = 0xffff
    val data: Array[Byte] = Array.fill(HiResBitmap.size){0x00}
    assert(HiResBitmap(addr, data).isInstanceOf[HiResBitmap])
  }

  def testHiResBitmapFormatTooMuchData {
    intercept[InvalidImageDataException] {
      val addr: Address = HiResBitmap.load
      val data: Array[Byte] = Array.fill(HiResBitmap.size + 1){0x00}
      HiResBitmap(addr, data)
    }
  }

  def testHiResBitmapFormatTooLittleData {
    intercept[InvalidImageDataException] {
      val addr: Address = HiResBitmap.load
      val data: Array[Byte] = Array.fill(HiResBitmap.size - 1){0x00}
      HiResBitmap(addr, data)
    }
  }

  def testHiResBitmapFormatEmptyData {
    intercept[InvalidImageDataException] {
      val addr: Address = HiResBitmap.load
      val data: Array[Byte] = Array()
      HiResBitmap(addr, data)
    }
  }

  def testHiResBitmapConfigFileAddr {
   assert(HiResBitmap.load.value == 0x0000)
  }

  def testHiResBitmapConfigFileSize {
   assert(HiResBitmap.size == 0x1f40)
  }

  def setupEmptyTestHiResData() = {
    HiRes(Array.fill(HiRes.size("bitmap")){0x00})
  }

  def setupEmptyTestHiResBitmapData() = {
    HiResBitmap(
      HiResBitmap.load,
      Array.fill(HiResBitmap.size){0x00}
    )
  }

  def testHiResDataSerialization {
    val hiResImage = setupEmptyTestHiResData()
    val hiResBitmapImage = setupEmptyTestHiResBitmapData()
    assert(HiResBitmap(hiResImage) equals hiResBitmapImage)
  }
}
