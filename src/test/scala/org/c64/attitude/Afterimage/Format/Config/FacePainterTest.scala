package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.Suite

class FacePainterConfigTestSuite extends Suite {

  def testFacePainterBitmapOffset {
   assert(FacePainter.bitmap == 0x0000)
  }

  def testFacePainterScreenOffset {
   assert(FacePainter.screen == 0x1f40)
  }

  def testFacePainterColorsOffset {
   assert(FacePainter.colors == 0x2328)
  }

  def testFacePainterBorderOffset {
   assert(FacePainter.border == 0x2710)
  }

  def testFacePainterBackgroundOffset {
   assert(FacePainter.bckgrd == 0x2711)
  }
}
