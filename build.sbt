lazy val root = (project in file(".")).settings(
  name := "afterimage",
  organization := "org.c64.attitude",
  scalacOptions ++= Seq("-deprecation", "-feature"),
  scalaVersion := "2.13.0",
  version := "0.08-SNAPSHOT"
)

maxErrors := 1

libraryDependencies ++= Seq(
  "gov.nih.imagej" % "imagej" % "1.47",
  "org.json4s" %% "json4s-native" % "3.6.6",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
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

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://attitude.c64.org/afterimage</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git://github.com/pawelkrol/attitude-afterimage</url>
    <connection>scm:git:git://github.com/pawelkrol/attitude-afterimage.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pawelkrol</id>
      <name>Pawel Krol</name>
      <url>http://www.attitude.c64.org</url>
    </developer>
  </developers>
)
