CHANGES
=======

Revision history for `Afterimage`, a Commodore 64 graphics library with a built-in support for the most common CBM file format specifications, entirely written in [Scala](http://www.scala-lang.org/).

0.08-SNAPSHOT (2019-09-21)
--------------------------

* Improvement: align exceptions thrown upon failed attempts to load an invalid colour palette to be always `IllegalArgumentException` (previously sometimes a generic `RuntimeException` would have been reported)
* Enhancement: enable creation of a colour palette from a given plain JSON string (add `fromJson` function to the `Palette` companion object)

0.07 (2019-06-30)
-----------------

* Enhancement: add support for more CBM image file formats: Amica Paint
* Refactoring: simplify error handling API by replacing all homebrewed exception classes targetting argument validation with a single `IllegalArgumentException` class thrown upon an invalid argument passed to any method, and a generic `RuntimeException` for all other errors
* `Scala` version upgraded to 2.13.0
* `ScalaTest` version upgraded to 3.0.8
* `sbt` version upgraded to 1.2.8
* `sbt-pgp` version upgraded to 1.1.1
* `JSON4S` version upgraded to 3.6.6

0.06 (2018-02-27)
-----------------

* Enhancement: enable saving rendered images directly in PNG format

0.05 (2018-02-17)
-----------------

* New feature: add basic support for processing and rendering hires and multicolour sprite data
* `Scala` version upgraded to 2.12.4
* `ScalaTest` version upgraded to 3.0.5
* `sbt` version upgraded to 0.13.17
* `JSON4S` version upgraded to 3.5.3

0.04 (2016-12-28)
-----------------

* Enhancement: specifying an arbitrary background colour when importing graphic images in HiRes format is now possible (this might turn out especially handy when processing pictures that are designed to be eventually converted into a custom character set and rendered using screen fonts rather than a bitmap image)
* Enhancement: displaying image preview may now be customised with an arbitrary scale factor to be applied when rendering a picture
* Enhancement: add customisable picture upscaling by an additional factor when saving PNG image to file (previously saving PNG file would automatically upscale rendered image by a factor of 2, now it renders target image in a 1:1 scale, providing an optional argument to select a custom scaling factor)
* `Scala` version upgraded to 2.12.1
* `ScalaTest` version upgraded to 3.0.1
* `sbt` version upgraded to 0.13.13

0.03 (2015-12-06)
-----------------

* Enhancement: importing image files with colour schemes other than RGB is now possible due to an immediate data conversion into an RGB format right after constructing an ImagePlus object from a file prior to an actual image data processing (previously images saved with a colour palette of 8-bit depth resulted in construction of empty pictures, because of inability to get correct colour data)
* Optimization: algorithm converting PC image files into C64 hires mode now considers most frequent colour as a background colour
* Extension: VICE internal colour palette provided
* New feature: enable applying a customised colour scheme by specifying an externally provided JSON configuration file as a colour pallete parameter instead of a predefined template name
* New feature: JSON serialisation of a colour palette data
* New feature: Enable selection of colours from a colour palette by their names
* Bug fixed: loading image data from a Koala Painter file works now correctly by setting valid background colour even if it was initialized with a value greater than `$0f` in an original image file
* `Scala` version upgraded to 2.11.7
* `ScalaTest` version upgraded to 2.2.0
* `sbt` version upgraded to 0.13.5

0.02 (2013-06-30)
-----------------

* Bug fixed: loading "default" colour palette from a resource file embedded within a target JAR package now works correctly
* Bug fixed: calculation of a C64 colour for a given RGB colour yielded incorrect results, now every RGB colour is correctly resolved into the closest matching C64 hue available in a given colour palette
* New feature: image loading mechanism has been extended with a capability to import C64 hires and multicolour pictures directly from TIFF, BMP, DICOM, FITS, PGM, GIF, JPEG, and PNG files

0.01 (2013-03-27)
-----------------

* Initial version (supports reading, translating, displaying, converting, and writing picture data from/to the following CBM image file formats: Advanced Art Studio, Art Studio, Hires Bitmap, Face Painter and Koala Painter)
