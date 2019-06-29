package org.c64.attitude.Afterimage
package Mode.Data

/** Screen colours data of an image.
  *
  * @constructor create a new screen data
  * @param data an array of bytes (must equal "cols * rows" result)
  * @param cols screen width counted as a number of 8x8 character columns
  * @param rows screen height counted as a number of 8x8 character rows
  */
case class Screen(data: Array[Byte], cols: Int, rows: Int) {

  require(
    cols >= 1 && cols <= Screen.maxCols && rows >= 1 && rows <= Screen.maxRows,
    "Invalid screen colours data size: got %s, but expected anything between 1x1 and 40x25 with row and column values counted as occurences of 8x8 character areas".format(
      "%dx%d".format(cols, rows)
    )
  )

  require(
    data.length == cols * rows,
    "Invalid screen colours data length: got %d, but expected %d bytes".format(data.length, cols * rows)
  )

  /** Returns the entire screen colours data as an array of bytes. */
  def get(): Array[Byte] = data

  def validateScreenCoordinates(x: Int, y: Int) =
    require(
      x >= 0 && x < cols && y >= 0 && y < rows,
      "Invalid pixel coordinates: got %d, but expected values within the range of %s".format(
        "[%s,%s]".format(x, y),
        "[%s,%s]".format(cols, rows)
      )
    )

  /** Returns screen colour at a given position.
    *
    * @param x X coordinate of the screen colour
    * @param y Y coordinate of the screen colour
    */
  def get(x: Int, y: Int): Byte = {
    validateScreenCoordinates(x, y)

    data(x + y * cols)
  }

  private def horizontalShift(dx: Int, fill: Byte) = {
    val screenRows = data.sliding(cols, cols)

    val newRows =
      if (dx < 0)
        screenRows.map(row => row.takeRight(cols + dx) ++ Array.fill((-dx).min(cols)){fill})
      else
        screenRows.map(row => Array.fill((dx).min(cols)){fill} ++ row.take(cols - dx))

    new Screen(newRows.reduce((a, b) => a ++ b), cols, rows)
  }

  private def verticalShift(dy: Int, fill: Byte) = {
    val screenRows = data.sliding(cols, cols)

    val addedRow = Array.fill(cols){fill}

    val newScreen =
      if (dy < 0) {
        // Advance the iterator past the first dy elements:
        screenRows.drop(-dy)
        // Select all remaining values of the iterator:
        val init = screenRows.take(rows)
        val tail = Array.fill((-dy).min(rows)){addedRow}
        (init ++ tail).reduce((a, b) => a ++ b)
      }
      else {
        // Select first rows - dy values of the iterator:
        val tail = screenRows.take(rows - dy)
        val init = Array.fill((dy).min(rows)){addedRow}
        (init ++ tail).reduce((a, b) => a ++ b)
      }

    new Screen(newScreen, cols, rows)
  }

  /** Returns the new screen colours data composed from the original image with a shifted content.
    *
    * @param dx value of horizontal data shift (positive for shifting to the right, and negative for shifting to the left)
    * @param dy value of vertical data shift (positive for shifting to the bottom, and negative for shifting to the top)
    * @param fill optional fill value of newly created empty bytes (defaults to 0x00)
    */
  def shift(dx: Int, dy: Int, fill: Byte = 0x00) =
    horizontalShift(dx, fill).verticalShift(dy, fill)

  private def getData(x: Int, y: Int, width: Int, height: Int) =
    (y to y + height.min(rows) - 1).map(row => {
      (x to x + width.min(cols) - 1).map(col => {
        get(col, row)
      })
    }).flatten.toArray

  /** Returns the new screen colours data composed from cutting out a slice of the original image.
   *
   * @param x X coordinate of the top-left corner of a rectangular selection area
   * @param y Y coordinate of the top-left corner of a rectangular selection area
   * @param newWidth total width of a rectangular selection area as a number of 8x8 character columns
   * @param newHeight total height of a rectangular selection area as a number of 8x8 character columns
   */
  def slice(x: Int, y: Int, newWidth: Int, newHeight: Int) = {
    val data = getData(x, y, newWidth, newHeight)
    new Screen(data, newWidth, newHeight)
  }

  /** Returns screen colour at a given position.
    *
    * @param x X coordinate of the screen colour
    * @param y Y coordinate of the screen colour
    */
  def apply(x: Int, y: Int): Byte = get(x, y)

  def canEqual(that: Any) = that.isInstanceOf[Screen]

  override def equals(other: Any) = other match {
    case that: Screen =>
      (that canEqual this) && (this.data.toList == that.data.toList) && (this.cols == that.cols) && (this.rows == that.rows)
    case _ =>
      false
  }
}

/** Factory for [[org.c64.attitude.Afterimage.Mode.Data.Screen]] instances. */
object Screen {

  /** Maximum possible number of columns of a Screen colours data. */
  val maxCols = 40

  /** Maximum possible number of rows of a Screen colours data. */
  val maxRows = 25

  private val maxSize = maxCols * maxRows

  /** Creates a default (empty) screen of a maximum possible size. */
  def apply(): Screen =
    Screen(Array.fill(maxSize){0x00}, maxCols, maxRows)

  /** Creates a screen of a maximum possible size filled all with a constant value. */
  def apply(fill: Byte): Screen =
    Screen(Array.fill(maxSize){fill}, maxCols, maxRows)

  /** Creates a default (empty) screen of a given size. */
  def apply(cols: Int, rows: Int): Screen =
    Screen(Array.fill(cols * rows){0x00}, cols, rows)
}
