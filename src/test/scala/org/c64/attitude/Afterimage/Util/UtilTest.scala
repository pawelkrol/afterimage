package org.c64.attitude.Afterimage
package Util

import org.scalatest.Suite

class UtilTest extends Suite {

  def testGetOrderedNumbersSwap {
    val (one, two) = Util.getOrderedNumbers(2, 1)
    assert(one == 1 && two == 2)
  }

  def testGetOrderedNumbersNoSwap {
    val (one, two) = Util.getOrderedNumbers(1, 2)
    assert(one == 1 && two == 2)
  }
}
