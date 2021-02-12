package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.freespec.AnyFreeSpec

class AdvancedArtStudioSpec extends AnyFreeSpec {

  "advanced art studio bitmap offset" in {
   assert(AdvancedArtStudio.bitmap == 0x0000)
  }

  "advanced art studio screen offset" in {
   assert(AdvancedArtStudio.screen == 0x1f40)
  }

  "advanced art studio colors offset" in {
   assert(AdvancedArtStudio.colors == 0x2338)
  }

  "advanced art studio background offset" in {
   assert(AdvancedArtStudio.bckgrd == 0x2329)
  }
}
