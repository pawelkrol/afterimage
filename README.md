attitude-afterimage
===================

[Afterimage](http://www.cactus.jawnet.pl/afterimage/) is a Commodore 64 graphics library with a built-in support for the most common CBM file format specifications and sprites, entirely written in [Scala](http://www.scala-lang.org/). It supports reading, translating, displaying, converting, and writing picture data from/to miscellaneous CBM image files. Since version 0.02 it also provides functionality to import (convert) pictures directly from TIFF, BMP, DICOM, FITS, PGM, GIF, JPEG, and PNG files.

VERSION
-------

Version 0.06 (2018-02-27)

PREREQUISITES
-------------

Besides `scala-library-2.12.4` and `scalatest_2.12.4-3.0.5`, some of the [Afterimage](http://www.cactus.jawnet.pl/afterimage/) functionalities rely upon the following Java image processing toolkit: `imagej-1.47`.

Dependency management is normally handled automatically by your build tool.

[Afterimage](http://www.cactus.jawnet.pl/afterimage/) is by default packaged as a standalone JAR.

If you plan on using [Afterimage](http://www.cactus.jawnet.pl/afterimage/) within your program, you have to provide all required dependencies yourself (and the standard Scala library as well, if your project is not developed in Scala, but Java instead). Consult `libraryDependencies` property of a `build.sbt` configuration file for the most recent details.

Default [Afterimage](http://www.cactus.jawnet.pl/afterimage/) configuration requires [Scala](http://www.scala-lang.org/) runtime version 2.12.4. Note that compiling against a different version of the standard Scala library than you are using at runtime would lead to a runtime exception upon execution of your program.

INSTALLATION
------------

You can automatically download and install this library by adding the following dependency information to your `build.sbt` configuration file:

    resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

    libraryDependencies += "org.c64.attitude" % "afterimage" % "0.06"

In order to compile and build this library directly from the source code type the following:

    $ git clone git://github.com/pawelkrol/attitude-afterimage.git
    $ cd attitude-afterimage/
    $ sbt clean update compile test package publishSigned publish-local

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

By default saving a PNG image to file triggers an error when overwriting an existing file. To change this behaviour `overwriteIfExists` flag may be used. In order to upscale rendered image before saving it as a PNG file, an additional argument `scaleFactor` (defaulting to _1_) may be defined:

    PNG(image).save(name = "images/image.png", overwriteIfExists = true, scaleFactor = 2)

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

Preview hires sprite data using a default colour palette:

    import org.c64.attitude.Afterimage.Sprite.{ Data, HiResProps }

    import scala.io.Codec.ISO8859
    import scala.io.Source.fromFile

    val data = fromFile("images/sprites.prg")(ISO8859).toSeq.map(_.toByte).take(0x3f)
    val props = HiResProps(0x00)

    val image = Data(data, props).render(0x00, 0x00)
    image((_, _, _, _, _) => {}).show()

Combine additional multicolour sprites into a preloaded target image:

    import org.c64.attitude.Afterimage.Colour.Palette
    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.Sprite.{ Data, MultiProps }
    import org.c64.attitude.Afterimage.View.Image

    import scala.io.Codec.ISO8859
    import scala.io.Source.fromFile

    val picture = File.load("images/image.fcp")
    val palette = Palette("default")
    val image   = Image(picture, palette)

    val data  = fromFile("images/sprites.prg")(ISO8859).toSeq.map(_.toByte)
    val props = MultiProps(0x00, 0x01, 0x02)

    val data1      = data.take(0x3f)
    val firstImage = Data(data1, props).render(0x00, 0x00, _ => image)

    val data2     = data.drop(0x40).take(0x3f)
    val nextImage = Data(data2, props).renderNext(0x18, 0x00, firstImage)
    nextImage((_, _, _, _, _) => {}).show()

SCRIPTING
---------

Using [Afterimage](http://www.cactus.jawnet.pl/afterimage/) as a standalone library is possible, given that all required dependencies are provided on a classpath. In the following example three PNG pictures named `image-0.png`, `image-1.png` and `image-2.png` are converted into three FacePainter images named `0.fcp`, `1.fcp` and `2.fcp` using a simple Scala script:

    import org.c64.attitude.Afterimage.Colour.Palette
    import org.c64.attitude.Afterimage.File.File
    import org.c64.attitude.Afterimage.File.Import
    import org.c64.attitude.Afterimage.Format.FacePainter
    import org.c64.attitude.Afterimage.Mode

    val palette = Palette("default")

    List("0", "1", "2").foreach { num => {
      val importMode = Import.MultiColour(backgroundColour = 0x01, palette)
      val picture = File.convert("image-" + num + ".png", importMode)
      FacePainter(picture.asInstanceOf[Mode.MultiColour]).save(num + ".fcp")
    } }

Assuming an above script has been saved into a `convert.scala` file, it can be executed using the following command(s):

    $ export AFTERIMAGE=/home/pkrol/.ivy2/cache/org.c64.attitude/afterimage/jars/afterimage-0.06.jar
    $ export IMAGEJ=/home/pkrol/.ivy2/cache/gov.nih.imagej/imagej/jars/imagej-1.47.jar
    $ export JSON_4S=/home/pkrol/.ivy2/cache/org.json4s/json4s-ast_2.12/jars/json4s-ast_2.12-3.5.3.jar:/home/pkrol/.ivy2/cache/org.json4s/json4s-core_2.12/jars/json4s-core_2.12-3.5.3.jar:/home/pkrol/.ivy2/cache/org.json4s/json4s-native_2.12/jars/json4s-native_2.12-3.5.3.jar
    $ scala -classpath $AFTERIMAGE:$IMAGEJ:$JSON_4S convert.scala

CUSTOMISATION
-------------

Applying a customised colour palette is accomplished by specifying a JSON configuration file name as a `Pallete` parameter instead of a predefined template name:

    import org.c64.attitude.Afterimage.Colour.Palette

    val palette = Palette("custom-palette.json")

Colour palette is expected to be a text file defined in a JSON format along the following lines:

    {
        "palette": [
            { "red": 0,   "green": 0,   "blue": 0,   "name": "black"       },
            { "red": 255, "green": 255, "blue": 255, "name": "white"       },
            { "red": 104, "green": 55,  "blue": 43,  "name": "red"         },
            { "red": 112, "green": 164, "blue": 178, "name": "cyan"        },
            { "red": 111, "green": 61,  "blue": 134, "name": "purple"      },
            { "red": 88,  "green": 141, "blue": 67,  "name": "green"       },
            { "red": 53,  "green": 40,  "blue": 121, "name": "blue"        },
            { "red": 184, "green": 199, "blue": 111, "name": "yellow"      },
            { "red": 111, "green": 79,  "blue": 37,  "name": "orange"      },
            { "red": 67,  "green": 57,  "blue": 0,   "name": "brown"       },
            { "red": 154, "green": 103, "blue": 89,  "name": "light red"   },
            { "red": 68,  "green": 68,  "blue": 68,  "name": "dark grey"   },
            { "red": 108, "green": 108, "blue": 108, "name": "grey"        },
            { "red": 154, "green": 210, "blue": 132, "name": "light green" },
            { "red": 108, "green": 94,  "blue": 181, "name": "light blue"  },
            { "red": 149, "green": 149, "blue": 149, "name": "light grey"  }
        ]
    }

FEATURES
--------

### FILE FORMATS ###

The list of currently supported CBM file format specifications includes:

* Advanced Art Studio `.ocp`
* Art Studio `.aas`
* Hires Bitmap `.hpi`
* Face Painter `.fcp`
* Koala Painter `.kla`

Additionally binary (raw) sprite data is recognised by suitable classes (i.e. `Sprite.Data`).

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

Copyright (C) 2013-2016, 2018 by Pawel Krol.

This library is free open source software; you can redistribute it and/or modify it under the same terms as Scala itself, either Scala version 2.12.4 or, at your option, any later version of Scala you may have available.

PLEASE NOTE THAT IT COMES WITHOUT A WARRANTY OF ANY KIND!
