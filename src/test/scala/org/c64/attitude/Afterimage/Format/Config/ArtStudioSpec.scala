package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.freespec.AnyFreeSpec

class ArtStudioSpec extends AnyFreeSpec {

  "art studio bitmap offset" in {
   assert(ArtStudio.bitmap == 0x0000)
  }

  "art studio screen offset" in {
   assert(ArtStudio.screen == 0x1f40)
  }
}
