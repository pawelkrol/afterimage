package org.c64.attitude.Afterimage
package Format

import java.io.{File,PrintStream}

import Config.Offset
import Memory.Address
import Mode.{CBM,HiRes,MultiColour}
import Mode.Data.{Bitmap,Screen}
import Util.ArrayHelper.deep

/** CBM graphic format abstraction functioning as a base class for miscellaneous image formats providing data validation options as well as convenient access to the file loading address and data bytes. */
trait Format {

  /** CBM file loading address that needs to be provided by constructor of a derived class. */
  val addr: Address

  /** Array of file data bytes that needs to be provided by constructor of a derived class. */
  val data: Array[Byte]

  /** Default file loading address that needs to be defined as a property of a derived class is used to validate constructor parameters. */
  val load: Address

  /** Default length of file data bytes that needs to be defined as a property of a derived class is used to validate constructor parameters. */
  val size: Int

  /** Memory configuration of the file format data that needs to be defined as a property of a derived class is used to extract image data from file data bytes. */
  val config: Offset

  /** Validates consistency of an object instance data. */
  def validate(): Unit = {
    if (load != addr || data.length != size)
      throw new InvalidImageDataException(this.getClass.getName.split("\\.").last)
  }

  private def serialize() = addr.toWrite ++ data

  /** Save any supported CBM image format into a file.
    *
    * @param name target file name
    * @param overwriteIfExists boolean flag indicating whether overwriting of an existing file should trigger no error
    */
  def save(name: String, overwriteIfExists: Boolean = false): Unit = {

    val file = new File(name)

    if (!overwriteIfExists)
      if (file.exists())
        throw new FileAlreadyExistsException(name)

    val writer = new PrintStream(file)
    writer.write(serialize())
    writer.close()
  }

  /** Extract image data from file data bytes.
    *
    * @return a new instance of any `Mode`-inherited class with the image mode determined by the file data
    */
  def extractData(): CBM = {

    config match {
      case hiRes: Config.HiRes => {
        val bitmapData = data.slice(hiRes.bitmap, hiRes.bitmap + HiRes.size("bitmap"))
        val bitmap = Bitmap(bitmapData, Bitmap.maxCols, Bitmap.maxRows)

        val screen: Option[Screen] = hiRes match {
          case artStudio: Config.ArtStudio => {
            val screenData = data.slice(artStudio.screen, artStudio.screen + HiRes.size("screen"))
            Some(Screen(screenData, Screen.maxCols, Screen.maxRows))
          }
          case hiResBitmap: Config.HiResBitmap => None
          case _ => throw new RuntimeException("Something went wrong...")
        }

        val border = hiRes match {
          case artStudio: Config.ArtStudio => None
          case hiResBitmap: Config.HiResBitmap => None
          case _ => throw new RuntimeException("Something went wrong...")
        }

        new HiRes(bitmap, screen, border)
      }

      case multiColour: Config.MultiColour => {
        val bitmapData = data.slice(multiColour.bitmap, multiColour.bitmap + MultiColour.size("bitmap"))
        val bitmap = Bitmap(bitmapData, Bitmap.maxCols, Bitmap.maxRows)

        val screenData = data.slice(multiColour.screen, multiColour.screen + MultiColour.size("screen"))
        val screen = Screen(screenData, Screen.maxCols, Screen.maxRows)

        val colorsData = data.slice(multiColour.colors, multiColour.colors + MultiColour.size("colors"))
        val colors = Screen(colorsData, Screen.maxCols, Screen.maxRows)

        // Make sure that background colour is not initialized with value >$0f:
        val bckgrd = (data(multiColour.bckgrd).toInt & 0x0f).toByte

        val border = multiColour match {
          case advancedArtStudio: Config.AdvancedArtStudio => None
          case facePainter: Config.FacePainter => Some(data(facePainter.border))
          case koalaPainter: Config.KoalaPainter => None
          case _ => throw new RuntimeException("Something went wrong...")
        }

        new MultiColour(bitmap, screen, colors, border, bckgrd)
      }

      case _ => throw new RuntimeException("Something went wrong...")
    }
  }

  /** Compares the receiver object (`this`) with the argument object (`other`) for equivalence.
    *
    * @return `true` if the receiver object is equivalent to the argument, `false` otherwise
    */
  override def equals(other: Any) = other match {
    case that: Format =>
      (that canEqual this) && (this.addr == that.addr) && (deep(this.data) == deep(that.data))
    case _ =>
      false
  }

  /** States that objects of this class are never equal to objects of its superclass.
    *
    * @param that the value being probed for possible equality
    * @return `true` if `this` instance can possibly equal `that`, otherwise `false`
    */
  def canEqual(that: Any) = that.isInstanceOf[Format]
}
