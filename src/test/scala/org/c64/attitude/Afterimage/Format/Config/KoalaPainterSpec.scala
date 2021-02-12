package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.freespec.AnyFreeSpec

class KoalaPainterConfigSpec extends AnyFreeSpec {

  "koala painter bitmap offset" in {
   assert(KoalaPainter.bitmap == 0x0000)
  }

  "koala painter screen offset" in {
   assert(KoalaPainter.screen == 0x1f40)
  }

  "koala painter colors offset" in {
   assert(KoalaPainter.colors == 0x2328)
  }

  "koala painter background offset" in {
   assert(KoalaPainter.bckgrd == 0x2710)
  }
}
