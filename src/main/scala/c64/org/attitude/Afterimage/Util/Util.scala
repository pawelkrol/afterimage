package org.c64.attitude.Afterimage
package Util

/** Various utility functions used accross the entire `Afterimage` library. */
object Util {

  /** Returns two integer values sorted in ascending order.
    *
    * @param one first integer value
    * @param two second integer value
    */
  def getOrderedNumbers(one: Int, two: Int): Tuple2[Int, Int] =
    if (one > two)
      Tuple2(two, one)
    else
      Tuple2(one, two)

  /** Returns integer value rounded up to the closest number divisible by 8.
    *
    * @param value integer value to be rounded up
    */
  def roundSizeToChar(value: Int): Int = {
    val addition = if ((value & 0x0007) != 0) 0x08 else 0x00
    (value & 0xfff8) + addition
  }
}
