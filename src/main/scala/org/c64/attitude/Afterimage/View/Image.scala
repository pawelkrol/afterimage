package org.c64.attitude.Afterimage
package View

import Colour.Palette
import Mode.CBM

/** Image viewer.
  *
  * @param picture picture to be used as a source of data for generating image preview
  * @param palette colour palette to be used when displaying a picture
  */
class Image(val picture: CBM, val palette: Palette) extends Shower {

  /** Displays image preview.
   *
   * @param scaleFactor defines custom image scale factor to be used when rendering a preview (defaults to 1)
   */
  def show(scaleFactor: Int = 1): Unit = {
    assert(scaleFactor > 0)
    create(scaleFactor).show()
  }
}

/** Factory for [[org.c64.attitude.Afterimage.View.Image]] instances. */
object Image {

  /** Creates a new `Image` viewer from given picture data and using provided colour palette.
    *
    * @param picture picture to be used as a source of data for generating image preview
    * @param palette colour palette to be used when displaying a picture
    */
  def apply(picture: CBM, palette: Palette) = new Image(picture, palette)
}
