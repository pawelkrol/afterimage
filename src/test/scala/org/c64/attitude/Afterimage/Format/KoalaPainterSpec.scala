package org.c64.attitude.Afterimage
package Format

import org.scalatest.FreeSpec

import Memory.Address
import Mode.MultiColour

class KoalaPainterFormatSpec extends FreeSpec {

  "koala painter format create" in {
    val addr: Address = KoalaPainter.load
    val data: Array[Byte] = Array.fill(KoalaPainter.size){0x00}
    assert(KoalaPainter(addr, data).isInstanceOf[KoalaPainter])
  }

  "koala painter format invalid address" in {
    intercept[IllegalArgumentException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(KoalaPainter.size){0x00}
      KoalaPainter(addr, data)
    }
  }

  "koala painter format too much data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = KoalaPainter.load
      val data: Array[Byte] = Array.fill(KoalaPainter.size + 1){0x00}
      KoalaPainter(addr, data)
    }
  }

  "koala painter format too little data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = KoalaPainter.load
      val data: Array[Byte] = Array.fill(KoalaPainter.size - 1){0x00}
      KoalaPainter(addr, data)
    }
  }

  "koala painter format empty data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = KoalaPainter.load
      val data: Array[Byte] = Array()
      KoalaPainter(addr, data)
    }
  }

  "koala painter config file addr" in {
   assert(KoalaPainter.load.value == 0x6000)
  }

  "koala painter config file size" in {
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

  "multicolour data serialization" in {
    val multiColourImage = setupEmptyTestMultiColourData()
    val koalaPainterImage = setupEmptyTestKoalaPainterData()
    assert(KoalaPainter(multiColourImage) equals koalaPainterImage)
  }
}
