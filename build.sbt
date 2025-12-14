lazy val root = (project in file(".")).settings(
  name := "afterimage",
  organization := "com.github.pawelkrol",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings"),
  scalaVersion := "3.7.4",
  version := "1.0.2-SNAPSHOT"
)

maxErrors := 1

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3" % "3.20.0",
  "net.imagej" % "ij" % "1.54p",
  "org.json4s" %% "json4s-native" % "4.0.7",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test"
)

// Disable using the Scala version in output paths and artifacts:
crossPaths := false

Test / publishArtifact := false

ThisBuild / organization := "com.github.pawelkrol"
ThisBuild / organizationName := "Pawel Krol"
ThisBuild / organizationHomepage := Some(url("https://github.com/pawelkrol"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/pawelkrol/afterimage"),
    "scm:git@github.com:pawelkrol/afterimage.git",
  )
)
ThisBuild / developers := List(
  Developer(
    id = "pawelkrol",
    name = "Pawel Krol",
    email = "djgruby@gmail.com",
    url = url("https://github.com/pawelkrol"),
  )
)

ThisBuild / description := "Commodore 64 graphics library with a built-in support for the most common file format specifications."
ThisBuild / licenses := List(
  "Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"),
)
ThisBuild / homepage := Some(url("https://github.com/pawelkrol/afterimage"))

// Remove all additional repository other than Maven Central from POM:
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true

// New setting for the Central Portal:
ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value)
    Some("central-snapshots" at centralSnapshots)
  else
    localStaging.value
}
