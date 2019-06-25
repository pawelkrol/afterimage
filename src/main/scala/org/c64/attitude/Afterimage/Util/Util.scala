package org.c64.attitude.Afterimage
package Util

import Mode.{HiRes, MultiColour}
import Mode.Data.{Bitmap, Screen}

/** Various utility functions used accross the entire `Afterimage` library. */
object Util {

  /** Returns two integer values sorted in ascending order.
    *
    * @param one first integer value
    * @param two second integer value
    */
  def getOrderedNumbers(one: Int, two: Int): Tuple2[Int, Int] =
    if (one > two)
      Tuple2(two, one)
    else
      Tuple2(one, two)

  /** Returns integer value rounded up to the closest number divisible by 8.
    *
    * @param value integer value to be rounded up
    */
  def roundSizeToChar(value: Int): Int = {
    val addition = if ((value & 0x0007) != 0) 0x08 else 0x00
    (value & 0xfff8) + addition
  }

  /** Returns binary representation of a `Byte` value.
    *
    * @param byte byte value to be converted
    * @return string representation with a binary value of a given byte
    */
  def byte2binary(byte: Byte): String = {
    (0x00 until 0x08).foldLeft[String]("")((binary, bit) => {
      val bitMask = 0x80 >> bit

      if ((byte.toInt & bitMask) > 0)
        binary + "1"
      else
        binary + "0"
    })
  }

  /** Converts `Byte` into an unsigned integer value.
    *
    * @param byte byte value to be converted
    * @return unsigned integer value of a given byte
    */
  def byte2uint(byte: Byte): Int = byte.toInt & 0xff

  private def dumpBitmapData(bitmap: Bitmap): Unit = {

    println("======")
    println("BITMAP")
    println("======")

    val data = bitmap.get()
    val cols = bitmap.cols
    val rows = bitmap.rows

    (0 until rows).foreach(row => {
      val from = row * cols * 8
      val to = ((row + 1) * cols - 1) * 8
      println("[$%04x-$%04x]".format(from, to))

      val rowData = data.slice(from, to).grouped(8).toList
      (0 until 0x08).foreach(pixelRow => {
        val rowBytes = rowData.map(elem => "%02x".format(elem(pixelRow))).mkString(" ")
        println("%s".format(rowBytes))
      })
      println()
    })
  }

  private def dumpScreenData(screen: Option[Screen], label: String = "SCREEN"): Unit = {

    println("======")
    println(label)
    println("======")

    screen match {
      case Some(screen) =>
        val cols = screen.cols
        val rows = screen.rows

        (0 until rows).foreach(row => {
          val from = row * cols
          val to = ((row + 1) * cols - 1)

          val rowBytes = screen.get().slice(from, to).map(byte => "%02x".format(byte)).mkString(" ")

          println("%02x: %s".format(row, rowBytes))
        })
      case None =>
        println("%s".format(screen))
    }
  }

  private def dumpByteData(byte: Option[Byte], label: String = "BORDER"): Unit = {

    println("======")
    println(label)
    println("======")

    byte match {
      case Some(value) =>
        println("%02x".format(value))
      case None =>
        println("%s".format(byte))
    }
  }

  /** Prints out entire `HiRes` mode image data.
    *
    * @param image `HiRes` image data to be printed out
    */
  def dumpHiResImageData(image: HiRes): Unit = {

    val bitmap = image.bitmap
    val screen = image.screen
    val border = image.border

    dumpBitmapData(bitmap)
    dumpScreenData(screen)
    println()
    dumpByteData(border)
    println()
  }

  /** Prints out entire `MultiColour` mode image data.
    *
    * @param image `MultiColour` image data to be printed out
    */
  def dumpMultiColourImageData(image: MultiColour): Unit = {

    val bitmap = image.bitmap
    val screen = image.screen
    val colors = image.colors
    val border = image.border
    val bckgrd = image.bckgrd

    dumpBitmapData(bitmap)
    dumpScreenData(Some(screen))
    println()
    dumpScreenData(Some(colors), "COLORS")
    println()
    dumpByteData(border)
    println()
    dumpByteData(Some(bckgrd), "BCKGRD")
    println()
  }
}
