val scala2Version = "2.13.6"
val scala3Version = "3.0.0-RC3"

ThisBuild / scalafmtOnCompile := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "playground_scala",
    version := "0.1.0",
    scalaVersion := scala3Version
  )
  .aggregate(scala3, scala2)

lazy val scala2 = project
  .in(file("scala2"))
  .settings(
    scalaVersion := scala2Version,
    libraryDependencies := cats ++ scalatest
  )

lazy val cats = Seq(
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-free" % "2.1.0",
  "org.typelevel" %% "cats-effect" % "2.1.3"
)

lazy val scalatest = Seq(
  "org.scalatest" %% "scalatest" % "3.1.2",
  "org.mockito" %% "mockito-scala" % "1.14.4",
  "org.mockito" %% "mockito-scala-scalatest" % "1.14.4"
)

lazy val scala3 = project
  .in(file("scala3"))
  .settings(
    scalaVersion := scala3Version,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
