package org.c64.attitude.Afterimage
package Colour

import scala.util.parsing.json.JSON
import scala.util.parsing.json.JSONObject

/** Colour palette which maps C64 colours from/to RGB colours.
  *
  * @constructor create a new colour palette with a set of 16 colours
  * @param colours definition of 16 colour mappings (C64 from/to RGB)
  */
class Palette(colours: Array[Colour]) {

  if (colours.length != 16)
    throw new InvalidNumberOfPaletteColours(colours.length)

  /** Returns RGB colour for a given C64 colour.
    *
    * @param value C64 colour value (expected to be integer between 0 and 15)
    */
  def apply(value: Int) = {

    if (value < 0 || value > 15)
      throw new InvalidPaletteColourIndexValue(value)

    colours(value)
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
  def get(colour: Colour) = colours.zipWithIndex.minBy(_._1.delta_to(colour))._2
}

/** Factory for [[org.c64.attitude.Afterimage.Colour.Palette]] instances. */
object Palette {

  private def build(name: String) = {
    val filename = "/palettes/%s.json".format(name)
    val inputStream = getClass().getResourceAsStream(filename)

    val source = scala.io.Source.fromInputStream(inputStream)(scala.io.Codec.ISO8859)

    val palette = JSON.parseFull(source.mkString) match {
      case Some(data) => data match {
        case colours: Map[String,List[Map[String,Any]]] => colours("palette")
        case _ => throw new RuntimeException
      }
      case None => throw new RuntimeException
    }

    val colours = palette.map(Colour(_)).toArray
    new Palette(colours)
  }

  /** Creates a colour palette from a given template name.
    *
    * @param name template name, one of the currently available names: default
    */
  def apply(name: String) = {
    try {
      build(name)
    }
    catch {
      case _ => throw new InvalidColourPaletteTemplate(name)
    }
  }
}
