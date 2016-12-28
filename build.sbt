lazy val root = (project in file(".")).settings(
  name := "afterimage",
  organization := "org.c64.attitude",
  scalaVersion := "2.12.1",
  version := "0.04"
)

maxErrors := 1

libraryDependencies ++= Seq(
  "gov.nih.imagej" % "imagej" % "1.47",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
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
