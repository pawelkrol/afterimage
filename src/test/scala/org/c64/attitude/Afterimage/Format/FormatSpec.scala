package org.c64.attitude.Afterimage
package Format

import org.scalatest.freespec.AnyFreeSpec

import File.File
import Mode.{HiRes,MultiColour}

class FormatSpec extends AnyFreeSpec {

  def setupTempFile() = {

    val name = getClass.getResource("/").toString.replace("file:", "")
    val directory = new java.io.File(name)

    val tmpFile = java.io.File.createTempFile("afterimage-", ".prg", directory)
    tmpFile.getAbsolutePath()
  }

  def cleanupTempFile(fileName: String): Unit = {
    val file = new java.io.File(fileName)
    file.delete()
  }

  "koala painter to face painter" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.kla").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val facePainter = FacePainter(image)
    facePainter.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  "face painter to koala painter" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val koalaPainter = KoalaPainter(image)
    koalaPainter.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  "advanced art studio to face painter" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.ocp").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val facePainter = FacePainter(image)
    facePainter.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  "face painter to advanced art studio" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[MultiColour]

    val fileName = setupTempFile()

    val advancedArtStudio = AdvancedArtStudio(image)
    advancedArtStudio.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[MultiColour])

    cleanupTempFile(fileName)
  }

  "hires bitmap to art studio" in {
    val name = getClass.getResource("/images/niemanazwy-bimber.hpi").toString.replace("file:", "")
    val image = File.load(name).asInstanceOf[HiRes]

    val fileName = setupTempFile()

    val artStudio = ArtStudio(image)
    artStudio.save(fileName, true)

    assert(File.load(fileName).isInstanceOf[HiRes])

    cleanupTempFile(fileName)
  }
}
