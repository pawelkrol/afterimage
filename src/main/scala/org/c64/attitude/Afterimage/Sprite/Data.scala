package org.c64.attitude.Afterimage
package Sprite

import ij.ImagePlus
import ij.process.ImageProcessor

import Colour.Palette
import Mode.Data.{ Bitmap, Screen }
import Mode.{ CBM, HiRes, MultiColour }
import View.Image

/** Sprite data.
  *
  * @constructor create a new `Data` sprite data
  * @param data an array of 63 sprite data bytes
  * @param props miscellaneous sprite properties
  */
class Data(
  data: Seq[Byte],
  props: SpriteProperties
) {

  /** Returns an array of pixel colours defined as tuples of an actual colour and its display priority. */
  lazy val pixels =
    data.grouped(3).toList.map(_.map(binaryString(_)).mkString.split("")).map(props.pixels(_)).flatMap(y => {
      val expanded =
        y.flatMap(x =>
          if (props.expandX)
            Seq(x, x)
          else
            Seq(x)
          )
      if (props.expandY)
        Seq(expanded, expanded)
      else
        Seq(expanded)
    })

  /** Renders pixel data of a first sprite and combines it with a target image.
   *
   * @param x an X coordinate of a top-left corner of a next rendered sprite image
   * @param y an Y coordinate of a top-left corner of a next rendered sprite image
   * @param targetImage target `Image` object (defaults to an empty image filled with a `background` colour)
   * @param scaleFactor defines custom image scale factor to be used when rendering a picture (defaults to 1, i.e. no upscaling)
   * @param topLeftX defines custom X coordinate considered as a top-left corner of a `targetImage`
   * @param topLeftY defines custom Y coordinate considered as a top-left corner of a `targetImage`
   * @param backgroundColour default background colour used to fill screen data of an empty target image (defaults to `0xe6`)
   *
   * @return a function creating an `ImagePlus` object which is capable of generating image preview
   */
  def render(
    x: Int,
    y: Int,
    targetImage: (Byte) => Image = (backgroundColour) => emptyImage(backgroundColour),
    scaleFactor: Int = 1,
    topLeftX: Int = 0,
    topLeftY: Int = 0,
    backgroundColour: Byte = 0xe6.toByte
  ): ((ImageProcessor, CBM, Palette, Int, Int) => Unit) => ImagePlus =
    setupRenderer(x, y, scaleFactor, topLeftX, topLeftY) {
      (postProcess) =>
        targetImage(backgroundColour).create(
          scaleFactor = scaleFactor,
          scaleOf = scaleOf,
          postProcessHook = postProcess
        )
    }

  /** Renders pixel data of a next sprite and combines it with a target image.
   *
   * @param x an X coordinate of a top-left corner of a next rendered sprite image
   * @param y an Y coordinate of a top-left corner of a next rendered sprite image
   * @param targetImage a callback function creating an `ImagePlus` object which is capable of generating image preview
   * @param scaleFactor defines custom image scale factor to be used when rendering a picture (defaults to 1, i.e. no upscaling)
   * @param topLeftX defines custom X coordinate considered as a top-left corner of a `targetImage`
   * @param topLeftY defines custom Y coordinate considered as a top-left corner of a `targetImage`
   *
   * @return a function rendering an `ImagePlus` object which is capable of generating image preview
   */
  def renderNext(
    x: Int,
    y: Int,
    targetImage: ((ImageProcessor, CBM, Palette, Int, Int) => Unit) => ImagePlus,
    scaleFactor: Int = 1,
    topLeftX: Int = 0,
    topLeftY: Int = 0
  ): ((ImageProcessor, CBM, Palette, Int, Int) => Unit) => ImagePlus =
    setupRenderer(x, y, scaleFactor, topLeftX, topLeftY) {
      (postProcess) =>
        targetImage(postProcess)
    }

  private def setupRenderer(
    x: Int,
    y: Int,
    scaleFactor: Int = 1,
    topLeftX: Int = 0,
    topLeftY: Int = 0
  )(
    renderer: ((ImageProcessor, CBM, Palette, Int, Int) => Unit) => ImagePlus
  ): ((ImageProcessor, CBM, Palette, Int, Int) => Unit) => ImagePlus = {
    assert(scaleFactor > 0)

    (postProcess) => renderer(
      (ip, picture, palette, xScale, yScale) => {
        renderDelayed(x, y, scaleFactor, topLeftX, topLeftY)((_, _, _, _, _) => {
          postProcess(ip, picture, palette, xScale, yScale)
        })(ip, picture, palette, xScale, yScale)
      }
    )
  }

  private def renderDelayed(
    x: Int,
    y: Int,
    scaleFactor: Int,
    topLeftX: Int,
    topLeftY: Int
  ) = (postProcess: ((ImageProcessor, CBM, Palette, Int, Int) => Unit)) => {

    (ip: ImageProcessor, picture: CBM, palette: Palette, imageScaleX: Int, imageScaleY: Int) => {

      val (spriteScaleX, spriteScaleY) = scaleOf(picture)

      val xScale = imageScaleX / spriteScaleX
      val yScale = imageScaleY / spriteScaleY

      pixels.zipWithIndex.foreach({ case (data, dy) =>
        data.zipWithIndex.foreach({
          case (Some((colour, hasPriority)), dx) => {
            val X = x - topLeftX + dx
            val Y = y - topLeftY + dy
            val pixel = palette.pixel(colour)
            // TODO: Respect sprite to background display priority - target image's pixel combinations may be
            // determined by querying appropriate pixel data from targetImage's "picture" property and delegating
            // computations to concrete "props" classes ("putPixel" won't always be called if hasPriority == false)
            (0 until xScale).foreach(i =>
              (0 until yScale).foreach(j =>
                ip.putPixel(X * xScale + i, Y * yScale + j, pixel)
              )
            )
          }
          case (None, _) =>
        })
      })

      postProcess(ip, picture, palette, xScale, yScale)
    }
  }

  private val scaleOf = (pic: CBM) => {
    pic match {
      case multiColour: MultiColour => (2, 1)
      case hiRes: HiRes => (1, 1)
      case _ => throw new RuntimeException("Something went wrong...")
    }
  }

  private def emptyImage(backgroundColour: Byte) = Image(
    picture = HiRes(
      bitmap = Bitmap(),
      screen = Some(Screen(fill = backgroundColour)),
      border = None
    ),
    palette = Palette("default")
  )

  private def binaryString(byte: Byte) =
    String.format("%8s", Integer.toBinaryString(byte & 0xFF)).replace(' ', '0')
}

/** Factory for [[org.c64.attitude.Afterimage.Sprite.Data]] instances. */
object Data {

  /** Creates a new `Data` sprite data.
    *
    * @param data an array of 63 sprite data bytes
    * @param props miscellaneous sprite properties
    * @return a new `Data` sprite data instance
    */
  def apply(data: Seq[Byte], props: SpriteProperties): Data = new Data(data = data, props = props)
}
