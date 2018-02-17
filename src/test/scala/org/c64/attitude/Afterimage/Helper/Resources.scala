package org.c64.attitude.Afterimage
package Helper

import java.io.File

import scala.io.Codec.ISO8859
import scala.io.Source.fromFile

object Resources {

  def testResourcePath(name: String) = getClass.getResource(name).toString.replace("file:", "")

  def testResourceFile(name: String) = new File(testResourcePath(name))

  def testResourceData(file: File) = fromFile(file)(ISO8859).toList

  def testResourceData(name: String) = fromFile(testResourceFile(name))(ISO8859).toList
}
