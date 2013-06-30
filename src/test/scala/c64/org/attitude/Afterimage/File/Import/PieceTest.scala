package org.c64.attitude.Afterimage
package File.Import

import org.scalatest.Ignore
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
      Seq.fill[Byte](0x08)(0xff.toByte),
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
      Seq[Int](0x33, 0x33, 0xcc, 0xcc, 0x33, 0x33, 0xcc, 0xcc).map(_.toByte),
      0xd5.toByte
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
      Seq[Int](0x80, 0xc0, 0xe0, 0xf0, 0xf8, 0xfc, 0xfe, 0xff).map(_.toByte),
      0x27.toByte
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
}