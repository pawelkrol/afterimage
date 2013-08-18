package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus
import ij.process.ImageConverter

import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

import Colour.Colour

class SplitIntoPiecesTest extends Suite with ShouldMatchers {

  private val name = getClass.getResource("/images/bandzior-cuc.png").toString.replace("file:", "")

  def testSplitImagePlusIntoPieces {

    val img = new ImagePlus(name)
    val mode = HiRes()

    val imageConverter = new ImageConverter(img)
    imageConverter.convertToRGB()

    val pieces: Seq[Seq[Piece]] = mode.splitInto8x8Pieces(img)
    val piece_0_3 = pieces(0)(3)

    piece_0_3.getPixels(0)(0) should equal (Colour(0xab.toByte, 0xab.toByte, 0xab.toByte, None))
    piece_0_3.getPixels(0)(7) should equal (Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, None))
    piece_0_3.getPixels(7)(0) should equal (Colour(0xab.toByte, 0xab.toByte, 0xab.toByte, None))
    piece_0_3.getPixels(7)(7) should equal (Colour(0xab.toByte, 0xab.toByte, 0xab.toByte, None))
  }
}
