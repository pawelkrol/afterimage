package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.Suite

class HiResBitmapConfigTestSuite extends Suite {

  def testHiResBitmapDataOffset {
   assert(HiResBitmap.bitmap == 0x0000)
  }
}
