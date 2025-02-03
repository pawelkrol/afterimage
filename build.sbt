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
  scalaVersion := "3.6.3",
  version := "1.0.1-SNAPSHOT"
)

maxErrors := 1

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3" % "3.17.0",
  "gov.nih.imagej" % "imagej" % "1.47",
  "org.json4s" %% "json4s-native" % "4.0.7",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test"
)

// Disable using the Scala version in output paths and artifacts:
crossPaths := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

Test / publishArtifact := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/pawelkrol/afterimage</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git://github.com/pawelkrol/afterimage</url>
    <connection>scm:git:git://github.com/pawelkrol/afterimage.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pawelkrol</id>
      <name>Pawel Krol</name>
      <url>https://github.com/pawelkrol</url>
    </developer>
  </developers>
)
