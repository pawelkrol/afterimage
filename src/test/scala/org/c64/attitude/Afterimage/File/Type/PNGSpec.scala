package org.c64.attitude.Afterimage
package File.Type

import ij.ImagePlus
import java.nio.file.FileAlreadyExistsException
import org.scalatest.FreeSpec

import Colour.Palette
import File.File
import Mode.MultiColour
import View.Image

class PNGSpec extends FreeSpec {

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

  def setupTestData() = {
    val picture = MultiColour(
      Array.fill(MultiColour.size("bitmap")){0x00},
      Array.fill(MultiColour.size("screen")){0x00},
      Array.fill(MultiColour.size("colors")){0x00},
      0x00
    )
    PNG(Image(picture, Palette("default")))
  }

  def buildImageConverter(resourceImage: String) = {
    val name = getClass.getResource(resourceImage).toString.replace("file:", "")
    val picture = File.load(name)
    PNG(Image(picture, Palette("default")))
  }

  "create PNG" in {
    val png = setupTestData()
    assert(png.isInstanceOf[PNG])
  }

  "koala painter to PNG" in {
    val png = buildImageConverter("/images/frighthof83-yazoo.kla")

    val fileName = setupTempFile()

    png.save(fileName, true)

    val image = new ImagePlus(fileName)
    assert(image.isInstanceOf[ImagePlus])

    cleanupTempFile(fileName)
  }

  "art studio to PNG" in {
    val png = buildImageConverter("/images/desolate-deev.aas")

    val fileName = setupTempFile()

    png.save(fileName, true)

    val image = new ImagePlus(fileName)
    assert(image.isInstanceOf[ImagePlus])

    cleanupTempFile(fileName)
  }

  "overwrite if exists save parameter" in {
    val png = setupTestData()

    val fileName = setupTempFile()

    png.save(fileName, true)
    png.save(fileName, true)

    val image = new ImagePlus(fileName)
    assert(image.isInstanceOf[ImagePlus])

    intercept[FileAlreadyExistsException] {
      png.save(fileName, false)
    }

    cleanupTempFile(fileName)
  }

  "PNG default scale factor" in {
    val png = buildImageConverter("/images/desolate-deev.aas")

    val fileName = setupTempFile()

    png.save(fileName, true)

    val image = new ImagePlus(fileName)
    assert(image.getWidth == 320)
    assert(image.getHeight == 200)

    cleanupTempFile(fileName)
  }

  "PNG custom scale factor" in {
    val png = buildImageConverter("/images/desolate-deev.aas")

    val fileName = setupTempFile()
    val scaleFactor = 3

    png.save(fileName, true, scaleFactor)

    val image = new ImagePlus(fileName)
    assert(image.getWidth == scaleFactor * 320)
    assert(image.getHeight == scaleFactor * 200)

    cleanupTempFile(fileName)
  }
}
