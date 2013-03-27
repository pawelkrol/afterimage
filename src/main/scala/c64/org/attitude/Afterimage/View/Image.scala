package org.c64.attitude.Afterimage
package View

import Colour.Palette
import Mode.Mode

/** Image viewer.
  *
  * @param picture picture to be used as a source of data for generating image preview
  * @param palette colour palette to be used when displaying a picture
  */
class Image(val picture: Mode, val palette: Palette) extends Shower {

  /** Displays image preview. */
  def show() {
    create().show()
  }
}

/** Factory for [[org.c64.attitude.Afterimage.View.Image]] instances. */
object Image {

  /** Creates a new `Image` viewer from given picture data and using provided colour palette.
    *
    * @param picture picture to be used as a source of data for generating image preview
    * @param palette colour palette to be used when displaying a picture
    */
  def apply(picture: Mode, palette: Palette) = new Image(picture, palette)
}
