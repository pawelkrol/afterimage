package org.c64.attitude.Afterimage
package Util

import org.scalatest.FreeSpec

class UtilSpec extends FreeSpec {

  "get ordered numbers swap" in {
    val (one, two) = Util.getOrderedNumbers(2, 1)
    assert(one == 1 && two == 2)
  }

  "get ordered numbers no swap" in {
    val (one, two) = Util.getOrderedNumbers(1, 2)
    assert(one == 1 && two == 2)
  }
}
