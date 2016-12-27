package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.FreeSpec

class ArtStudioSpec extends FreeSpec {

  "art studio bitmap offset" in {
   assert(ArtStudio.bitmap == 0x0000)
  }

  "art studio screen offset" in {
   assert(ArtStudio.screen == 0x1f40)
  }
}
