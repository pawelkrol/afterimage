package org.c64.attitude.Afterimage
package Format

import org.scalatest.FreeSpec

import Memory.Address
import Mode.HiRes

class HiResBitmapFormatSpec extends FreeSpec {

  "hires bitmap format create" in {
    val addr: Address = HiResBitmap.load
    val data: Array[Byte] = Array.fill(HiResBitmap.size){0x00}
    assert(HiResBitmap(addr, data).isInstanceOf[HiResBitmap])
  }

  "hires bitmap format irrelevant address" in {
    val addr: Address = 0xffff
    val data: Array[Byte] = Array.fill(HiResBitmap.size){0x00}
    assert(HiResBitmap(addr, data).isInstanceOf[HiResBitmap])
  }

  "hires bitmap format too much data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = HiResBitmap.load
      val data: Array[Byte] = Array.fill(HiResBitmap.size + 1){0x00}
      HiResBitmap(addr, data)
    }
  }

  "hires bitmap format too little data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = HiResBitmap.load
      val data: Array[Byte] = Array.fill(HiResBitmap.size - 1){0x00}
      HiResBitmap(addr, data)
    }
  }

  "hires bitmap format empty data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = HiResBitmap.load
      val data: Array[Byte] = Array()
      HiResBitmap(addr, data)
    }
  }

  "hires bitmap config file addr" in {
   assert(HiResBitmap.load.value == 0x0000)
  }

  "hires bitmap config file size" in {
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

  "hires data serialization" in {
    val hiResImage = setupEmptyTestHiResData()
    val hiResBitmapImage = setupEmptyTestHiResBitmapData()
    assert(HiResBitmap(hiResImage) equals hiResBitmapImage)
  }
}
