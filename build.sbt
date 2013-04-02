name := "afterimage"

version := "0.02-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "gov.nih.imagej" % "imagej" % "1.46",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)

// Disable using the Scala version in output paths and artifacts:
crossPaths := false

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

organization := "org.c64.attitude"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://attitude.c64.org/afterimage</url>
  <licenses>
    <license>
      <name>Scala License</name>
      <url>http://www.scala-lang.org/node/146</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git://github.com/pawelkrol/attitude-afterimage.git</url>
    <connection>scm:git:git://github.com/pawelkrol/attitude-afterimage.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pawelkrol</id>
      <name>Pawel Krol</name>
      <url>http://attitude.c64.org</url>
    </developer>
  </developers>
)
