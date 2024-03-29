package com.github.pawelkrol.Afterimage
package Colour

import org.json4s.{JArray, JObject}
import org.json4s.MonadicJValue.jvalueToMonadic
import org.json4s.native.JsonMethods.parse

import scala.math.Ordering.Double.TotalOrdering

import Util.Util.listResources

/** Colour palette which maps C64 colours from/to RGB colours.
  *
  * @constructor create a new colour palette with a set of 16 colours
  * @param colours definition of 16 colour mappings (C64 from/to RGB)
  */
case class Palette(colours: Array[Colour]) {

  require(
    colours.length == 16,
    "Invalid number of palette colours: got %d, but expected an array of 16 colours".format(colours.length)
  )

  /** Returns RGB colour for a given C64 colour.
    *
    * @param value C64 colour value (expected to be integer between 0 and 15)
    */
  def apply(value: Int) = {

    require(
      value >= 0 && value <= 15,
      "Invalid palette colour index: got %d, but expected an integer between 0 and 15".format(value)
    )

    colours(value)
  }

  /** Returns optional RGB colour for a given C64 colour name as defined in a colour palette data.
    *
    * @param name C64 colour name (expected to be a string matching colour name from a colour palette)
    */
  def apply(name: String) = {
    colours.find(_.name match {
      case Some(colourName) => name == colourName
      case None => false
    })
  }

  /** Returns ImageJ colour for a given C64 colour.
    *
    * @param value C64 colour value (expected to be integer between 0 and 15)
    */
  def pixel(value: Int) = this(value).pixel

  /** Returns C64 colour for a given RGB colour.
    *
    * @param colour RGB colour to be resolved into the closest matching C64 hue
    */
  def get(colour: Colour) = colours.zipWithIndex.minBy(_._1.delta_to(colour))(TotalOrdering)._2

  def canEqual(that: Any) = that.isInstanceOf[Palette]

  override def equals(other: Any) = other match {
    case that: Palette =>
      (that canEqual this) && (this.colours.toList == that.colours.toList)
    case _ =>
      false
  }

  /** Serialises colour palette to a JSON object.
    */
  def toJson = JObject(List(("palette", JArray(colours.map(_.toJson).toList))))
}

/** Factory for [[com.github.pawelkrol.Afterimage.Colour.Palette]] instances. */
object Palette {

  private def build(name: String) = {

    val filename = "/palettes/%s.json".format(name)
    val inputStream = getClass().getResourceAsStream(filename)

    val source = scala.io.Source.fromInputStream(inputStream)(scala.io.Codec.UTF8)

    parseJSON(source.mkString)
  }

  private def load(file: String) = {

    val source = scala.io.Source.fromFile(file)(scala.io.Codec.UTF8)

    parseJSON(source.mkString)
  }

  private def parseJSON(source: String) = {

    val palette = (parse(source) \ "palette").children

    val colours = palette.map(item => {
      item match {
        case colour: JObject => Colour(colour.values)
        case _ => throw new RuntimeException
      }
    }).toArray

    new Palette(colours)
  }

  import org.apache.commons.lang3.StringUtils

  private val availableColourPalettes =
    listResources("/palettes").filter(_.endsWith(".json")).map(fileName => "'%s'".format(StringUtils.removeEnd(fileName, ".json"))).mkString(", ")

  /** Creates a colour palette from a given JSON configuration file or as
    * a fallback from a pre-configured template name (if file does not exist
    * and template name does not match any predefined colour palette template
    * names, a runtime exception will be throw).
    *
    * @param name JSON configuration file or template name
    */
  def apply(name: String) = {
    try {
      fromFile(name)
    }
    catch {
      case _: Throwable =>
        try {
          fromTemplate(name)
        }
        catch {
          case _: IllegalArgumentException =>
            throw new IllegalArgumentException("Invalid colour palette: '%s' (no such file or template found, expected one of: %s)".format(name, availableColourPalettes))
          case e: Throwable =>
            throw new RuntimeException("Something went wrong: %s".format(e.getMessage()))
        }
    }
  }

  /** Creates a colour palette from a given template name.
    *
    * @param name template name, one of the currently available names: default
    */
  def fromTemplate(name: String) = {
    try {
      build(name)
    }
    catch {
      case e: NoClassDefFoundError =>
        throw new RuntimeException("Library missing from CLASSPATH: %s".format(e.getMessage()))
      case e: Throwable =>
        throw new IllegalArgumentException("Invalid colour palette template: check '%s' configuration".format(name))
    }
  }

  /** Creates a colour palette from a given JSON configuration file.
    *
    * @param name JSON configuration file with a customised colour palette
    */
  def fromFile(name: String) = {
    try {
      load(name)
    }
    catch {
      case _: Throwable =>
        throw new IllegalArgumentException("Invalid colour palette setup: malformed JSON data in '%s' file".format(name))
    }
  }

  /** Creates a colour palette from a given plain JSON string.
    *
    * @param json Plain JSON string with a customised colour palette
    */
  def fromJson(json: String) = {
    try {
      parseJSON(json)
    }
    catch {
      case _: Throwable =>
        throw new IllegalArgumentException("Invalid colour palette setup: malformed JSON data in input string")
    }
  }
}
