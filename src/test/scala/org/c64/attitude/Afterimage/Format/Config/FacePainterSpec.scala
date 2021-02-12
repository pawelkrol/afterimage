package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.freespec.AnyFreeSpec

class FacePainterConfigSpec extends AnyFreeSpec {

  "face painter bitmap offset" in {
   assert(FacePainter.bitmap == 0x0000)
  }

  "face painter screen offset" in {
   assert(FacePainter.screen == 0x1f40)
  }

  "face painter colors offset" in {
   assert(FacePainter.colors == 0x2328)
  }

  "face painter border offset" in {
   assert(FacePainter.border == 0x2710)
  }

  "face painter background offset" in {
   assert(FacePainter.bckgrd == 0x2711)
  }
}
