package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.FreeSpec

class HiResBitmapConfigSpec extends FreeSpec {

  "hires bitmap data offset" in {
   assert(HiResBitmap.bitmap == 0x0000)
  }
}
