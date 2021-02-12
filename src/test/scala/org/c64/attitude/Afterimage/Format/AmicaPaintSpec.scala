package org.c64.attitude.Afterimage
package Format

import org.scalatest.freespec.AnyFreeSpec

import Memory.Address
import Mode.MultiColour

class AmicaPaintFormatSpec extends AnyFreeSpec {

  "amica paint format create" in {
    val addr: Address = AmicaPaint.load
    val data: Array[Byte] = Array.fill(AmicaPaint.size){0x00}
    assert(AmicaPaint(addr, data).isInstanceOf[AmicaPaint])
  }

  "amica paint data unpack" in {
    val addr: Address = AmicaPaint.load
    val data: Array[Byte] = (Array.fill(AmicaPaint.size){0x00}.grouped(0xff).flatMap(chunk => Seq(0xc2, chunk.size, chunk.head)).toArray ++ Array(0xc2, 0x00)).map(_.toByte)
    assert(AmicaPaint.unpack(addr, data).isInstanceOf[AmicaPaint])
  }

  "amica paint format invalid address" in {
    intercept[IllegalArgumentException] {
      val addr: Address = 0x0000
      val data: Array[Byte] = Array.fill(AmicaPaint.size){0x00}
      AmicaPaint(addr, data)
    }
  }

  "amica paint format too much data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = AmicaPaint.load
      val data: Array[Byte] = Array.fill(AmicaPaint.size + 1){0x00}
      AmicaPaint(addr, data)
    }
  }

  "amica paint format too little data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = AmicaPaint.load
      val data: Array[Byte] = Array.fill(AmicaPaint.size - 1){0x00}
      AmicaPaint(addr, data)
    }
  }

  "amica paint format empty data" in {
    intercept[IllegalArgumentException] {
      val addr: Address = AmicaPaint.load
      val data: Array[Byte] = Array()
      AmicaPaint(addr, data)
    }
  }

  "amica paint config file addr" in {
    assert(AmicaPaint.load.value == 0x6000)
  }

  "amica paint config file size" in {
    assert(AmicaPaint.size == 0x2811)
  }

  def setupEmptyTestMultiColourData() = {
    MultiColour(
      Array.fill(MultiColour.size("bitmap")){0x00.toByte},
      Array.fill(MultiColour.size("screen")){0x00.toByte},
      Array.fill(MultiColour.size("colors")){0x00.toByte},
      0x00.toByte
    )
  }

  def setupEmptyTestAmicaPaintData() = {
    AmicaPaint(
      AmicaPaint.load,
      Array.fill(AmicaPaint.size){0x00}
    )
  }

  "multicolour data serialization" in {
    val multiColourImage = setupEmptyTestMultiColourData()
    val amicaPaintImage = setupEmptyTestAmicaPaintData()
    assert(AmicaPaint(multiColourImage) equals amicaPaintImage)
  }
}
