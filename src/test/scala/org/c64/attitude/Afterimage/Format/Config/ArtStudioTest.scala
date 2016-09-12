package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.Suite

class ArtStudioTest extends Suite {

  def testArtStudioBitmapOffset {
   assert(ArtStudio.bitmap == 0x0000)
  }

  def testArtStudioScreenOffset {
   assert(ArtStudio.screen == 0x1f40)
  }
}
