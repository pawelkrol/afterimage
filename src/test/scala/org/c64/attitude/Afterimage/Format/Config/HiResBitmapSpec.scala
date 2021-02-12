package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.freespec.AnyFreeSpec

class HiResBitmapConfigSpec extends AnyFreeSpec {

  "hires bitmap data offset" in {
   assert(HiResBitmap.bitmap == 0x0000)
  }
}
