attitude-afterimage
===================

`Afterimage` is a Commodore 64 graphics library with a built-in support for the most common CBM file format specifications, entirely written in [Scala](http://www.scala-lang.org/). It supports reading, translating, displaying, converting, and writing picture data from/to miscellaneous CBM image files.

VERSION
-------

Version 0.01 (2013-03-27)

INSTALLATION
------------

To build and install this library type the following:

    $ git clone git://github.com/pawelkrol/attitude-afterimage.git
    $ cd attitude-afterimage/
    $ sbt clean update compile test package
    $ cp target/afterimage-0.01.jar <PROJECT_DIR>

EXAMPLES
--------

Convert KoalPainter image to FacePainter format:

    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.Format.FacePainter
    import org.c64.attitude.Afterimage.Mode.MultiColour

    val picture = File.load("images/image.kla")

    FacePainter(picture.asInstanceOf[MultiColour]).save("images/image.fcp")

Preview Art Studio image:

    import org.c64.attitude.Afterimage.Colour.Palette
    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.View.Image

    val picture = File.load("images/image.aas")
    val palette = Palette("default")
    val image   = Image(picture, palette)

    image.show()

Save Advanced Art Studio image to PNG file:

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

FEATURES
--------

### FILE FORMATS ###

The list of currently supported CBM file format specifications includes:

* Advanced Art Studio `.ocp`
* Art Studio `.aas`
* Hires Bitmap `.hpi`
* Face Painter `.fcp`
* Koala Painter `.kla`

The list of currently writable PC file format specifications includes:

* PNG `.png`

COPYRIGHT AND LICENCE
---------------------

Copyright (C) 2013 by Pawel Krol.

This library is free open source software; you can redistribute it and/or modify it under the same terms as Scala itself, either Scala version 2.9.2 or, at your option, any later version of Scala you may have available.

PLEASE NOTE THAT IT COMES WITHOUT A WARRANTY OF ANY KIND!
