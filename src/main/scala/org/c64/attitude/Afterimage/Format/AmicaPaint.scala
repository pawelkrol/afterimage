package org.c64.attitude.Afterimage
package Format

import Memory.Address
import Mode.MultiColour
import Util.Util.byte2uint

/** Amica Paint file format.
  *
  * @constructor create a new `AmicaPaint` image data
  * @param addr CBM file loading address
  * @param data array of (unpacked) file data bytes
  */
case class AmicaPaint(
  val addr: Address,
  val data: Array[Byte]
) extends Format {

  /** Memory configuration of the file format data. */
  val config = Config.AmicaPaint()

  /** Default file loading address. */
  val load = AmicaPaint.load

  /** Default length of file data bytes. */
  val size = AmicaPaint.size

  validate()

  /** States that objects of this class are never equal to objects of its superclass.
    *
    * @param that the value being probed for possible equality
    * @return `true` if `this` instance can possibly equal `that`, otherwise `false`
    */
  override def canEqual(that: Any) = that.isInstanceOf[AmicaPaint]
}

/** Factory for [[org.c64.attitude.Afterimage.Format.AmicaPaint]] instances. */
object AmicaPaint {

  /** Amica Paint packed file loading address. */
  val load: Address = 0x6000

  /** Amica Paint length of file data bytes. */
  val size = 0x2811

  /** Creates a new `AmicaPaint` file format from `MultiColour` image data.
    *
    * @param image `MultiColour` image data
    * @return a new `AmicaPaint` instance with the file data determined by the image data
    */
  def apply(image: MultiColour): AmicaPaint = {
    val data = image.bitmap.get ++ image.screen.get ++ image.colors.get ++ Array(image.bckgrd) ++ Array.fill(Config.AmicaPaint.size("colorRotationTable")){0x00.toByte}
    this(load, data)
  }

  /** Creates a new `AmicaPaint` file format from an array of (packed) file data bytes.
    *
    * @param addr CBM file loading address
    * @param data array of (packed) file data bytes
    * @return a new `AmicaPaint` instance with the file data determined by the image data
    */
  def unpack(
    addr: Address,
    packedData: Array[Byte]
  ): AmicaPaint = {

    // Unpack file data modes:
    val modeNormal = 0
    val modeCount = 1
    val modeByte = 2
    val modeEof = 3

    // Unpack file data bytes:
    val data: Array[Byte] = packedData.foldLeft[Tuple3[Array[Byte], Int, Int]](Tuple3(Array[Byte](), modeNormal, 0))((result, byte) => {
      val (unpackedData, mode, count) = result
      val byteValue = byte2uint(byte)
      if (mode == modeNormal) {
        if (byteValue == 0xc2)
          (unpackedData, modeCount, 0)
        else
          (unpackedData :+ byte, modeNormal, 0)
      }
      else if (mode == modeCount) {
        if (byteValue == 0x00)
          (unpackedData, modeEof, 0)
        else
          (unpackedData, modeByte, byteValue)
      }
      else if (mode == modeByte) {
        (unpackedData ++ Array.fill(count){byte}, modeNormal, 0)
      }
      else if (mode == modeEof) {
        (unpackedData, modeEof, 0)
      }
      else {
        throw new RuntimeException("Invalid file data mode when unpacking Amica Paint image: %d".format(mode))
      }
    })._1

    this(load, data)
  }
}
