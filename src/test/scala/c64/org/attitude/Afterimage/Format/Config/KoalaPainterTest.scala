package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.Suite

class KoalaPainterConfigTestSuite extends Suite {

  def testKoalaPainterBitmapOffset {
   assert(KoalaPainter.bitmap == 0x0000)
  }

  def testKoalaPainterScreenOffset {
   assert(KoalaPainter.screen == 0x1f40)
  }

  def testKoalaPainterColorsOffset {
   assert(KoalaPainter.colors == 0x2328)
  }

  def testKoalaPainterBackgroundOffset {
   assert(KoalaPainter.bckgrd == 0x2710)
  }
}
