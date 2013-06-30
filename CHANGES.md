CHANGES
=======

Revision history for `Afterimage`, a Commodore 64 graphics library with a built-in support for the most common CBM file format specifications, entirely written in [Scala](http://www.scala-lang.org/).

0.02 (2013-06-30)
-----------------

* Bug fixed: loading "default" colour palette from a resource file embedded within a target JAR package now works correctly
* Bug fixed: calculation of a C64 colour for a given RGB colour yielded incorrect results, now every RGB colour is correctly resolved into the closest matching C64 hue available in a given colour palette
* New feature: image loading mechanism has been extended with a capability to import C64 hires and multicolour pictures directly from TIFF, BMP, DICOM, FITS, PGM, GIF, JPEG, and PNG files

0.01 (2013-03-27)
-----------------

* Initial version (supports reading, translating, displaying, converting, and writing picture data from/to the following CBM image file formats: Advanced Art Studio, Art Studio, Hires Bitmap, Face Painter and Koala Painter)
