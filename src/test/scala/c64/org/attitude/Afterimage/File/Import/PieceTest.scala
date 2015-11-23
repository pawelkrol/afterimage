package org.c64.attitude.Afterimage
package File.Import

import org.scalatest.Suite
import org.scalatest.matchers.ShouldMatchers

import Colour.{Colour, Palette}

class PieceTest extends Suite with ShouldMatchers {

  private val emptyPiece = Piece()

  private val palette = Palette("default")

  def testPieceInitialization {

    emptyPiece should equal (Piece(Piece.emptyPixelArray))
  }

  def testEmptyPieceToHiResConversion {

    emptyPiece.toHiRes(palette) should equal (Tuple2[Seq[Byte], Byte](
      Seq.fill[Byte](0x08)(0x00.toByte),
      0x00
    ))
  }

  def testCBMPieceToHiResConversion {

    val green = palette(0x05)
    val light_green = palette(0x0d)

    val cbmPiece = Piece(pixels = Seq[Seq[Colour]](
      Seq[Colour](green, green, light_green, light_green, green, green, light_green, light_green),
      Seq[Colour](green, green, light_green, light_green, green, green, light_green, light_green),
      Seq[Colour](light_green, light_green, green, green, light_green, light_green, green, green),
      Seq[Colour](light_green, light_green, green, green, light_green, light_green, green, green),
      Seq[Colour](green, green, light_green, light_green, green, green, light_green, light_green),
      Seq[Colour](green, green, light_green, light_green, green, green, light_green, light_green),
      Seq[Colour](light_green, light_green, green, green, light_green, light_green, green, green),
      Seq[Colour](light_green, light_green, green, green, light_green, light_green, green, green)
    ))

    cbmPiece.toHiRes(palette) should equal (Tuple2[Seq[Byte], Byte](
      Seq[Int](0xcc, 0xcc, 0x33, 0x33, 0xcc, 0xcc, 0x33, 0x33).map(_.toByte),
      0x5d.toByte
    ))
  }

  def testPCPieceToHiResConversion {

    val brown = Colour(0xa5.toByte, 0x2a.toByte, 0x2a.toByte, Some("brown"))
    val orange = Colour(0xff.toByte, 0xa5.toByte, 0x00.toByte, Some("orange"))
    val red = Colour(0xff.toByte, 0x00.toByte, 0x00.toByte, Some("red"))

    val cbmPiece = Piece(pixels = Seq[Seq[Colour]](
      Seq[Colour](red, brown, brown, brown, brown, brown, brown, brown),
      Seq[Colour](orange, red, brown, brown, brown, brown, brown, brown),
      Seq[Colour](orange, orange, red, brown, brown, brown, brown, brown),
      Seq[Colour](orange, orange, orange, red, brown, brown, brown, brown),
      Seq[Colour](orange, orange, orange, orange, red, brown, brown, brown),
      Seq[Colour](orange, orange, orange, orange, orange, red, brown, brown),
      Seq[Colour](orange, orange, orange, orange, orange, orange, red, brown),
      Seq[Colour](orange, orange, orange, orange, orange, orange, orange, red)
    ))

    cbmPiece.toHiRes(palette) should equal (Tuple2[Seq[Byte], Byte](
      Seq[Int](0x7f, 0x3f, 0x1f, 0x0f, 0x07, 0x03, 0x01, 0x00).map(_.toByte),
      0x72.toByte
    ))
  }

  def testEmptyPieceToMultiColourConversion {

    emptyPiece.toMultiColour(palette, 0x00) should equal (Tuple3[Seq[Byte], Byte, Byte](
      Seq.fill[Byte](0x08)(0x00.toByte),
      0x00,
      0x00
    ))
  }

  def testCBMPieceToMultiColourConversion {

    val yellow = palette(0x07)
    val blue = palette(0x06)
    val red = palette(0x02)
    val green = palette(0x05)

    val cbmPiece = Piece(pixels = Seq[Seq[Colour]](
      Seq[Colour](yellow, yellow, yellow, yellow, blue, blue, blue, blue),
      Seq[Colour](yellow, yellow, yellow, yellow, blue, blue, blue, blue),
      Seq[Colour](yellow, yellow, yellow, yellow, blue, blue, blue, blue),
      Seq[Colour](yellow, yellow, yellow, yellow, blue, blue, blue, blue),
      Seq[Colour](red, red, red, red, green, green, green, green),
      Seq[Colour](red, red, red, red, green, green, green, green),
      Seq[Colour](red, red, red, red, green, green, green, green),
      Seq[Colour](red, red, red, red, green, green, green, green)
    ))

    cbmPiece.toMultiColour(palette, 0x02) should equal (Tuple3[Seq[Byte], Byte, Byte](
      Seq[Int](0xa0, 0xa0, 0xa0, 0xa0, 0x5f, 0x5f, 0x5f, 0x5f).map(_.toByte),
      0x67.toByte,
      0x05.toByte
    ))
  }

  def testPCPieceToMultiColourConversion {

    val beige = Colour(0xf5.toByte, 0xf5.toByte, 0xdc.toByte, Some("beige"))
    val blueViolet = Colour(0x8a.toByte, 0x2b.toByte, 0xe2.toByte, Some("blue_violet"))
    val indigo = Colour(0x4b.toByte, 0x00.toByte, 0x82.toByte, Some("indigo"))
    val royalBlue = Colour(0x41.toByte, 0x69.toByte, 0xe1.toByte, Some("royal_blue"))
    val teal = Colour(0x00.toByte, 0x80.toByte, 0x80.toByte, Some("teal"))
    val purple = Colour(0x80.toByte, 0x00.toByte, 0x80.toByte, Some("purple"))

    val cbmPiece = Piece(pixels = Seq[Seq[Colour]](
      Seq[Colour](indigo,     blueViolet, royalBlue,  teal,  teal,  blueViolet, indigo,     purple),
      Seq[Colour](indigo,     blueViolet, royalBlue,  teal,  teal,  blueViolet, indigo,     purple),
      Seq[Colour](blueViolet, royalBlue,  teal,       beige, beige, blueViolet, indigo,     purple),
      Seq[Colour](blueViolet, royalBlue,  teal,       beige, beige, blueViolet, indigo,     purple),
      Seq[Colour](purple,     indigo,     blueViolet, beige, beige, teal,       royalBlue,  blueViolet),
      Seq[Colour](purple,     indigo,     blueViolet, beige, beige, teal,       royalBlue,  blueViolet),
      Seq[Colour](purple,     indigo,     blueViolet, teal,  teal,  royalBlue,  blueViolet, indigo),
      Seq[Colour](purple,     indigo,     blueViolet, teal,  teal,  royalBlue,  blueViolet, indigo)
    ))

    cbmPiece.toMultiColour(palette, 0x04) should equal (Tuple3[Seq[Byte], Byte, Byte](
      Seq[Int](0x60, 0xa5, 0x9a, 0x7d, 0x7d, 0xa6, 0x5a, 0x09).map(_.toByte),
      0x6e.toByte,
      0x01.toByte
    ))
  }

  def testMultiColourConversionWithCustomPalette {

    val customPalette = Palette("vice")

    val black = Colour(0x00.toByte, 0x00.toByte, 0x00.toByte, Some("black"))
    val blue = Colour(0x48.toByte, 0x3a.toByte, 0xaa.toByte, Some("blue"))
    val cyan = Colour(0x84.toByte, 0xc5.toByte, 0xcc.toByte, Some("cyan"))
    val lightBlue = Colour(0x86.toByte, 0x7a.toByte, 0xde.toByte, Some("light blue"))

    val cbmPiece = Piece(pixels = Seq[Seq[Colour]](
      Seq[Colour](blue,      lightBlue, lightBlue, lightBlue, blue,  blue, lightBlue, blue),
      Seq[Colour](blue,      lightBlue, lightBlue, lightBlue, blue,  blue, lightBlue, blue),
      Seq[Colour](lightBlue, lightBlue, blue,      blue,      black, blue, blue,      lightBlue),
      Seq[Colour](lightBlue, lightBlue, blue,      blue,      black, blue, blue,      lightBlue),
      Seq[Colour](cyan,      blue,      lightBlue, blue,      blue,  blue, lightBlue, blue),
      Seq[Colour](cyan,      blue,      lightBlue, blue,      blue,  blue, lightBlue, blue),
      Seq[Colour](lightBlue, lightBlue, lightBlue, lightBlue, blue,  blue, blue,      black),
      Seq[Colour](lightBlue, lightBlue, lightBlue, lightBlue, blue,  blue, blue,      black)
    ))

    cbmPiece.toMultiColour(customPalette, 0x00) should equal (Tuple3[Seq[Byte], Byte, Byte](
      Seq[String]("10011101", "01011001", "01100101", "01101001", "10001010", "10101010", "01100110", "10011000").map(Integer.parseInt(_, 2).toByte),
      0xe6.toByte,
      0x03.toByte
    ))
  }
}
