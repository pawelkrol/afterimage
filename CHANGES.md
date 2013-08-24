CHANGES
=======

Revision history for `Afterimage`, a Commodore 64 graphics library with a built-in support for the most common CBM file format specifications, entirely written in [Scala](http://www.scala-lang.org/).

0.03-SNAPSHOT (2013-08-24)
--------------------------

* Enhancement: importing image files with colour schemes other than RGB is now possible due to an immediate data conversion into an RGB format right after constructing an ImagePlus object from a file prior to an actual image data processing (previously images saved with a colour palette of 8-bit depth resulted in construction of empty pictures, because of inability to get correct colour data)
* Optimization: algorithm converting PC image files into C64 hires mode now considers most frequent colour as a background colour
* Bug fixed: loading image data from a Koala Painter file works now correctly by setting valid background colour even if it was initialized with a value greater than $0f in an original image file

0.02 (2013-06-30)
-----------------

* Bug fixed: loading "default" colour palette from a resource file embedded within a target JAR package now works correctly
* Bug fixed: calculation of a C64 colour for a given RGB colour yielded incorrect results, now every RGB colour is correctly resolved into the closest matching C64 hue available in a given colour palette
* New feature: image loading mechanism has been extended with a capability to import C64 hires and multicolour pictures directly from TIFF, BMP, DICOM, FITS, PGM, GIF, JPEG, and PNG files

0.01 (2013-03-27)
-----------------

* Initial version (supports reading, translating, displaying, converting, and writing picture data from/to the following CBM image file formats: Advanced Art Studio, Art Studio, Hires Bitmap, Face Painter and Koala Painter)
