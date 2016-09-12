package org.c64.attitude.Afterimage
package Format

import org.scalatest.Suite

import File.File
import Mode.{HiRes,MultiColour}

class FormatTest extends Suite {

  def setupTempFile() = {

    val name = getClass.getResource("/").toString.replace("file:", "")
    val directory = new java.io.File(name)

    val tmpFile = java.io.File.createTempFile("afterimage-", ".prg", directory)
    tmpFile.getAbsolutePath()
  }

  def cleanupTempFile(fileName: String) {
    val file = new java.io.File(fileName)
    file.delete()
  }

  def testKoalaPainterToFacePainter {
    val name = getClass.getResource("/images/frighthof83-yazoo.kla").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val facePainter = FacePainter(image)
    facePainter.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  def testFacePainterToKoalaPainter {
    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val koalaPainter = KoalaPainter(image)
    koalaPainter.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  def testAdvancedArtStudioToFacePainter {
    val name = getClass.getResource("/images/frighthof83-yazoo.ocp").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val facePainter = FacePainter(image)
    facePainter.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  def testFacePainterToAdvancedArtStudio {
    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val advancedArtStudio = AdvancedArtStudio(image)
    advancedArtStudio.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  def testHiresBitmapToArtStudio {
    val name = getClass.getResource("/images/niemanazwy-bimber.hpi").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[HiRes]

    val fileName = setupTempFile()

    val artStudio = ArtStudio(image)
    artStudio.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[HiRes])

    cleanupTempFile(fileName)
  }
}
