package org.c64.attitude.Afterimage
package Memory

import scala.language.implicitConversions

/** 16-bit memory address.
  *
  * @constructor create a new memory `Address`
  * @param lo low byte of the memory address
  * @param hi high byte of the memory address
  */
case class Address(lo: Byte, hi: Byte) {

  /** Returns memory address as an integer value. */
  val value: Int = lo + 0x100 * hi

  /** Creates a String representation of this object.
    *
    * @return a String representation of the object
    */
  override def toString() = "$%04x".format(value)

  /** Creates an array of values that can be written into the first two bytes of a CBM file. */
  def toWrite = List(lo, hi).toArray
}

/** Factory for [[org.c64.attitude.Afterimage.Memory.Address]] instances. */
object Address {

  /** Creates a new memory `Address` from an integer value.
    *
    * @param addr integer value representing a memory address
    * @return a new `Address` instance with the memory address determined by the integer value
    */
  def apply(addr: Int): Address = {
    if (addr < 0x0000 || addr > 0xffff)
      throw new InvalidAddressException(addr)
    val lo = (addr & 0x00ff).toByte
    val hi = ((addr & 0xff00) >> 8).toByte
    this(lo, hi)
  }

  /** Defines implicit conversion between an integer value and a memory `Address`.
    *
    * @param addr integer value representing a memory address
    * @return a new `Address` instance with the memory address determined by the integer value
    */
  implicit def toAddress(addr: Int) = Address(addr)
}
