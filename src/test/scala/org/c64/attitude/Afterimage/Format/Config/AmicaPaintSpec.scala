package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.freespec.AnyFreeSpec

class AmicaPaintSpec extends AnyFreeSpec {

  "amica paint bitmap offset" in {
    assert(AmicaPaint.bitmap == 0x0000)
  }

  "amica paint screen offset" in {
    assert(AmicaPaint.screen == 0x1f40)
  }

  "amica paint colors offset" in {
    assert(AmicaPaint.colors == 0x2328)
  }

  "amica paint background offset" in {
    assert(AmicaPaint.bckgrd == 0x2710)
  }

  "amica paint olour rotation table offset" in {
    assert(AmicaPaint.colorRotationTable == 0x2711)
  }
}
