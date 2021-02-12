package org.c64.attitude.Afterimage
package File.Import

import ij.ImagePlus
import ij.process.ImageConverter

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

import Colour.Colour

class SplitIntoPiecesSpec extends AnyFreeSpec with Matchers {

  private val name = getClass.getResource("/images/bandzior-cuc.png").toString.replace("file:", "")

  "split image plus into pieces" in {

    val img = new ImagePlus(name)
    val mode = HiRes()

    val imageConverter = new ImageConverter(img)
    imageConverter.convertToRGB()

    val pieces: Seq[Seq[Piece]] = mode.splitInto8x8Pieces(img)
    val piece_0_3 = pieces(0)(3)

    piece_0_3.getPixels(0)(0) must equal (Colour(0xab.toByte, 0xab.toByte, 0xab.toByte, None))
    piece_0_3.getPixels(0)(7) must equal (Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, None))
    piece_0_3.getPixels(7)(0) must equal (Colour(0xab.toByte, 0xab.toByte, 0xab.toByte, None))
    piece_0_3.getPixels(7)(7) must equal (Colour(0xab.toByte, 0xab.toByte, 0xab.toByte, None))
  }
}
