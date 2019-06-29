package org.c64.attitude.Afterimage
package Memory

import org.scalatest.FreeSpec

class MemoryAddressSpec extends FreeSpec {

  "address create from bytes" in {
    val addr = Address(0x00, 0x10)
    assert(addr.lo == 0x00)
    assert(addr.hi == 0x10)
  }

  "address create from int" in {
    val addr = Address(0x1000)
    assert(addr.lo == 0x00)
    assert(addr.hi == 0x10)
  }

  "address implicit create from int" in {
    val addr: Address = 0x1000
    assert(addr.lo == 0x00)
    assert(addr.hi == 0x10)
  }

  "address print from bytes" in {
    val addr = Address(0x00, 0x10)
    assert(addr.toString == "$1000")
  }

  "address print from int" in {
    val addr = Address(0x1000)
    assert(addr.toString == "$1000")
  }

  "address implicit print from int" in {
    val addr: Address = 0x1000
    assert(addr.toString == "$1000")
  }

  "address value from bytes" in {
    val addr = Address(0x00, 0x10)
    assert(addr.value == 4096)
  }

  "address value from int" in {
    val addr = Address(0x1000)
    assert(addr.value == 4096)
  }

  "address implicit value from int" in {
    val addr: Address = 0x1000
    assert(addr.value == 4096)
  }

  "address create negative" in {
    intercept[IllegalArgumentException] {
      Address(-1)
    }
  }

  "address create too large" in {
    intercept[IllegalArgumentException] {
      Address(0x10000)
    }
  }
}
