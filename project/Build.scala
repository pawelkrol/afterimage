import sbt._
import Keys._

object AfterimageBuild extends Build {

  lazy val afterimage = Project(
    id = "afterimage",
    base = file("."),
    settings = Project.defaultSettings
  )
}