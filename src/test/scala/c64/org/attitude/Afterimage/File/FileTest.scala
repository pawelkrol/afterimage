package org.c64.attitude.Afterimage
package File

import org.scalatest.Suite

import Mode.{HiRes,MultiColour}

class FileTest extends Suite {

  def testLoadFacePainterFile {
    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  def testLoadKoalaPainterFile {
    val name = getClass.getResource("/images/frighthof83-yazoo.kla").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
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
}
