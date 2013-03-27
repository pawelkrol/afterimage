package org.c64.attitude.Afterimage
package Format.Config

import org.scalatest.Suite

class AdvancedArtStudioTest extends Suite {

  def testAdvancedArtStudioBitmapOffset {
   assert(AdvancedArtStudio.bitmap == 0x0000)
  }

  def testAdvancedArtStudioScreenOffset {
   assert(AdvancedArtStudio.screen == 0x1f40)
  }

  def testAdvancedArtStudioColorsOffset {
   assert(AdvancedArtStudio.colors == 0x2338)
  }

  def testAdvancedArtStudioBackgroundOffset {
   assert(AdvancedArtStudio.bckgrd == 0x2329)
  }
}
