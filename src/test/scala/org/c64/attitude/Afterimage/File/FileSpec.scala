package org.c64.attitude.Afterimage
package File

import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

import Colour.Palette

import Mode.{HiRes,MultiColour}

class FileSpec extends FreeSpec with MustMatchers {

  "load face painter file" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.fcp").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  "load koala painter file v1" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.kla").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  "load koala painter file v2" in {
    val name = getClass.getResource("/images/metal.kla").toString.replace("file:", "")
    val picture = File.load(name).asInstanceOf[MultiColour]
    picture.pixel(0, 0) must equal (0x00)
  }

  "load advanced art studio file" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.ocp").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  "load amica paint file" in {
    val name = getClass.getResource("/images/phantasy-rrr.ami").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[MultiColour])
  }

  "load hires bitmap file" in {
    val name = getClass.getResource("/images/niemanazwy-bimber.hpi").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[HiRes])
  }

  "load art studio file" in {
    val name = getClass.getResource("/images/desolate-deev.aas").toString.replace("file:", "")
    assert(File.load(name).isInstanceOf[HiRes])
  }

  "import hires PNG file" in {
    val name = getClass.getResource("/images/desolate-deev.png").toString.replace("file:", "")
    assert(File.convert(name, Import.HiRes()).isInstanceOf[HiRes])
  }

  "import multicolour PNG file" in {
    val name = getClass.getResource("/images/frighthof83-yazoo.png").toString.replace("file:", "")
    assert(File.convert(name, Import.MultiColour(backgroundColour = 0x00)).isInstanceOf[MultiColour])
  }
}
