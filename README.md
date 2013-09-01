attitude-afterimage
===================

[Afterimage](http://www.cactus.jawnet.pl/afterimage/) is a Commodore 64 graphics library with a built-in support for the most common CBM file format specifications, entirely written in [Scala](http://www.scala-lang.org/). It supports reading, translating, displaying, converting, and writing picture data from/to miscellaneous CBM image files. Since version 0.02 it also provides functionality to import (convert) pictures directly from TIFF, BMP, DICOM, FITS, PGM, GIF, JPEG, and PNG files.

VERSION
-------

Version 0.03-SNAPSHOT (2013-09-01)

PREREQUISITES
-------------

Besides `scala-library-2.10.2` and `scalatest_2.10.2-1.9.1`, some of the [Afterimage](http://www.cactus.jawnet.pl/afterimage/) functionalities rely upon the following Java image processing toolkit: `imagej-1.46`.

Dependency management is normally handled automatically by your build tool.

INSTALLATION
------------

You can automatically download and install this library by adding the following dependency information to your `build.sbt` configuration file:

    libraryDependencies += "org.c64.attitude" % "afterimage" % "0.03-SNAPSHOT"

In order to compile and build this library directly from the source code type the following:

    $ git clone git://github.com/pawelkrol/attitude-afterimage.git
    $ cd attitude-afterimage/
    $ sbt clean update compile test package

EXAMPLES
--------

Convert KoalaPainter image to FacePainter format:

    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.Format.FacePainter
    import org.c64.attitude.Afterimage.Mode.MultiColour

    val picture = File.load("images/image.kla")

    FacePainter(picture.asInstanceOf[MultiColour]).save("images/image.fcp")

Preview Art Studio image using a default colour palette:

    import org.c64.attitude.Afterimage.Colour.Palette
    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.View.Image

    val picture = File.load("images/image.aas")
    val palette = Palette("default")
    val image   = Image(picture, palette)

    image.show()

Save Advanced Art Studio image to PNG file using a default colour palette:

    import org.c64.attitude.Afterimage.Colour.Palette
    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.File.Type.PNG
    import org.c64.attitude.Afterimage.View.Image

    val picture = File.load("images/image.ocp")
    val palette = Palette("default")
    val image   = Image(picture, palette)

    PNG(image).save("images/image.png")

Serialize first data row of a HiRes Bitmap image to a sequence of byte values:

    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.Mode.HiRes

    val picture = File.load("images/image.hpi").asInstanceOf[HiRes]
    val values  = picture.rows(0).serialize()

Deserialize the sequence of byte values into row data of a HiRes Bitmap image:

    import org.c64.attitude.Afterimage.Mode.Data.Row.HiResRow

    val row = HiResRow.inflate(values)

Generate dreamass-compatible source code string with HiRes image row data:

    val sourceCode = row.toCode("image_row_data_01")

Import 320x200 pixels PNG file and convert image data directly to HiRes mode:

    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.File.Import.HiRes

    val picture = File.convert("images/image.png", HiRes())

Import 320x200 pixels JPEG file and convert image data directly to MultiColour mode:

    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.File.Import.MultiColour

    val picture = File.convert("images/image.jpg", MultiColour(backgroundColour = 0x00))

FEATURES
--------

### FILE FORMATS ###

The list of currently supported CBM file format specifications includes:

* Advanced Art Studio `.ocp`
* Art Studio `.aas`
* Hires Bitmap `.hpi`
* Face Painter `.fcp`
* Koala Painter `.kla`

The list of PC file format specifications suitable for conversion includes:

* TIFF `.tiff`, `.tif`
* BMP `.bmp`, `.dib`
* DICOM `.dcm`
* FITS `.fits`, `.fit`, `.fts`
* PGM `.pgm`
* GIF `.gif`
* JPEG `.jpg`, `.jpeg`, `.jpe`, `.jif`, `.jfif`, `.jfi`
* PNG `.png`

The list of currently writable PC file format specifications includes:

* PNG `.png`

COPYRIGHT AND LICENCE
---------------------

Copyright (C) 2013 by Pawel Krol.

This library is free open source software; you can redistribute it and/or modify it under the same terms as Scala itself, either Scala version 2.10.2 or, at your option, any later version of Scala you may have available.

PLEASE NOTE THAT IT COMES WITHOUT A WARRANTY OF ANY KIND!
