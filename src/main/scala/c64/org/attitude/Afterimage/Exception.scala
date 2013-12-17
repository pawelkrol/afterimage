package org.c64.attitude.Afterimage

trait AfterimageException extends java.lang.Throwable

class InvalidAddressException(address: Int) extends AfterimageException {
  override def getMessage() = "Illegal memory address: $%04x".format(address)
}

class InvalidImageDataException(format: String) extends AfterimageException {
  override def getMessage() = "Invalid image data: Not a %s format".format(format)
}

class InvalidImageModeDataException(mode: String) extends AfterimageException {
  override def getMessage() = "Invalid %s image data".format(mode)
}

class InvalidImageDataSliceException(mode: String, got: String, expected: String, what: String) extends AfterimageException {
  override def getMessage() = "Invalid %s image data slice: got %s, but expected %s %s".format(mode, got, expected, what)
}

class InvalidDataRowIndexException(index: Int) extends AfterimageException {
  override def getMessage() = "Invalid data row index requested: got %d, but expected an integer between 0 and 24".format(index)
}

class InvalidNumberOfPaletteColours(number: Int) extends AfterimageException {
  override def getMessage() = "Invalid number of palette colours: got %d, but expected an array of 16 colours".format(number)
}

class InvalidPaletteColourIndexValue(index: Int) extends AfterimageException {
  override def getMessage() = "Invalid palette colour index: got %d, but expected an integer between 0 and 15".format(index)
}

class InvalidColourPalette(name: String) extends AfterimageException {
  override def getMessage() = "Invalid colour palette: %s (no such file or template found)".format(name)
}

class InvalidColourPaletteTemplate(name: String) extends AfterimageException {
  override def getMessage() = "Invalid colour palette template: check %s configuration".format(name)
}

class InvalidColourPaletteFilename(name: String) extends AfterimageException {
  override def getMessage() = "Invalid colour palette setup: malformed JSON data in %s file".format(name)
}

class InvalidPixelCoordinates(coord: String, maxim: String) extends AfterimageException {
  override def getMessage() = "Invalid pixel coordinates: got %d, but expected values within the range of %s".format(coord, maxim)
}

class InvalidScreenCoordinates(coord: String, maxim: String) extends AfterimageException {
  override def getMessage() = "Invalid pixel coordinates: got %d, but expected values within the range of %s".format(coord, maxim)
}

class InvalidSliceCoordinatesException(got: String, reason: String) extends AfterimageException {
  override def getMessage() = "Invalid slice coordinates requested: got %s, but expected %s".format(got, reason)
}

class InvalidSliceDimensionsException(got: String, reason: String) extends AfterimageException {
  override def getMessage() = "Invalid slice dimensions requested: got %s, but at this position maximum allowed size is %s".format(got, reason)
}

class InvalidBitmapDataSizeException(got: String) extends AfterimageException {
  override def getMessage() = "Invalid bitmap data size: got %s, but expected anything between 1x1 and 40x25 with row and column values counted as occurences of 8x8 character areas".format(got)
}

class InvalidScreenDataSizeException(got: String) extends AfterimageException {
  override def getMessage() = "Invalid screen colours data size: got %s, but expected anything between 1x1 and 40x25 with row and column values counted as occurences of 8x8 character areas".format(got)
}

class InvalidBitmapDataLengthException(got: Int, expected: Int) extends AfterimageException {
  override def getMessage() = "Invalid bitmap data length: got %d, but expected %d bytes".format(got, expected)
}

class InvalidScreenDataLengthException(got: Int, expected: Int) extends AfterimageException {
  override def getMessage() = "Invalid screen colours data length: got %d, but expected %d bytes".format(got, expected)
}

class InvalidPixelsNumberException(numBits: Int) extends AfterimageException {
  override def getMessage() = "Invalid number of pixels requested: got %d, but expected an integer between 1 and 8".format(numBits)
}

class InvalidSliceSelectionAreaException(got: String, expected: Int) extends AfterimageException {
  override def getMessage() = "Invalid slice selection area in MultiColour mode requested: got %s, but expected rows between 0 and %s".format(got, expected)
}

class FileAlreadyExistsException(fileName: String) extends AfterimageException {
  override def getMessage() = "File already exists: %s".format(fileName)
}

class NoDataException() extends AfterimageException {
  override def getMessage() = "No data has been provided: cannot process empty data"
}

