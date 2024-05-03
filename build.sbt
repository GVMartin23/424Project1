ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "Project2"
  )
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"