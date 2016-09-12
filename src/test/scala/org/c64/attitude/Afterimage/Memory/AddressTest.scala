package org.c64.attitude.Afterimage
package Memory

import org.scalatest.Suite

class MemoryAddressTestSuite extends Suite {

  def testAddressCreateFromBytes {
    val addr = Address(0x00, 0x10)
    assert(addr.lo == 0x00)
    assert(addr.hi == 0x10)
  }

  def testAddressCreateFromInt {
    val addr = Address(0x1000)
    assert(addr.lo == 0x00)
    assert(addr.hi == 0x10)
  }

  def testAddressImplicitCreateFromInt {
    val addr: Address = 0x1000
    assert(addr.lo == 0x00)
    assert(addr.hi == 0x10)
  }

  def testAddressPrintFromBytes {
    val addr = Address(0x00, 0x10)
    assert(addr.toString == "$1000")
  }

  def testAddressPrintFromInt {
    val addr = Address(0x1000)
    assert(addr.toString == "$1000")
  }

  def testAddressImplicitPrintFromInt {
    val addr: Address = 0x1000
    assert(addr.toString == "$1000")
  }

  def testAddressValueFromBytes {
    val addr = Address(0x00, 0x10)
    assert(addr.value == 4096)
  }

  def testAddressValueFromInt {
    val addr = Address(0x1000)
    assert(addr.value == 4096)
  }

  def testAddressImplicitValueFromInt {
    val addr: Address = 0x1000
    assert(addr.value == 4096)
  }

  def testAddressCreateNegative {
    intercept[InvalidAddressException] {
      Address(-1)
    }
  }

  def testAddressCreateTooLarge {
    intercept[InvalidAddressException] {
      Address(0x10000)
    }
  }
}
