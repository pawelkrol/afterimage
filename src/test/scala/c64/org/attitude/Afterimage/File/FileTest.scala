package org.c64.attitude.Afterimage
package File

import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

import Colour.Palette

import Mode.{HiRes,MultiColour}

class FileTest extends Suite with ShouldMatchers {

  def testLoadFacePainterFile {
    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  def testLoadKoalaPainterFileV1 {
    val name = getClass.getResource("/images/frighthof83-yazoo.kla").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  def testLoadKoalaPainterFileV2 {
    val name = getClass.getResource("/images/metal.kla").toString.replace("file:", "")
    val picture = File.load(name).asInstanceOf[MultiColour]
    picture.pixel(0, 0) should equal (0x00)
  }

  def testLoadAdvancedArtStudioFile {
    val name = getClass.getResource("/images/frighthof83-yazoo.ocp").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  def testLoadHiResBitmapFile {
    val name = getClass.getResource("/images/niemanazwy-bimber.hpi").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[HiRes])
  }

  def testLoadArtStudioFile {
    val name = getClass.getResource("/images/desolate-deev.aas").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[HiRes])
  }

  def testImportHiResPNGFile {
    val name = getClass.getResource("/images/desolate-deev.png").toString.replace("file:", "")
    assert(File.convert(name, Import.HiRes()).isInstanceOf[HiRes])
  }

  def testImportMultiColourPNGFile {
    val name = getClass.getResource("/images/frighthof83-yazoo.png").toString.replace("file:", "")
    assert(File.convert(name, Import.MultiColour(backgroundColour = 0x00)).isInstanceOf[MultiColour])
  }
}
