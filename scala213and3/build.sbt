val scala2Version = "2.13.6"
val scala3Version = "3.2.2"

ThisBuild / scalafmtOnCompile := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "playground_scala",
    version := "0.1.0"
  )
  .aggregate(scala3, scala2)

lazy val scala2 = project
  .in(file("scala2"))
  .settings(
    scalaVersion := scala2Version,
    resolvers += "Tabmo Myget Public".at("https://www.myget.org/F/tabmo-public/maven/"),
    libraryDependencies := cats ++ log ++ scalatest ++ mysql ++ doobie ++ http4s ++ circe ++ scalikejdbc
  )

lazy val cats = Seq(
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "cats-free" % "2.7.0",
  "org.typelevel" %% "cats-effect" % "2.1.3"
)

lazy val slf4jVersion = "1.7.36"
lazy val logbackVersion = "1.2.11"
lazy val log4catsVersion = "1.1.1"
lazy val log = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "io.chrisdavenport" %% "log4cats-core" % log4catsVersion,
  "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion
)

lazy val scalatest = Seq(
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.mockito" %% "mockito-scala" % "1.14.4" % Test,
  "org.mockito" %% "mockito-scala-scalatest" % "1.14.4" % Test
)

lazy val mysql = Seq(
  "mysql" % "mysql-connector-java" % "8.0.20"
)

lazy val doobieVersion = "0.9.0"
lazy val doobie = Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-h2" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-quill" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2" % doobieVersion % Test,
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test
)

lazy val scalikejdbcVersion = "3.5.0"
lazy val scalikejdbc = Seq(
  "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
  "com.h2database" % "h2" % "1.4.200",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

lazy val http4sVersion = "0.21.4"
lazy val http4s = Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion
)

lazy val circeVersion = "0.14.2"
lazy val circeConfigVersion = "0.8.0"
lazy val circeValidationVersion = "0.1.1" // NOTE: 最新版を指定しないとresolveしない場合あり.
lazy val circe = Seq(
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-config" % circeConfigVersion,
  "io.tabmo" %% "circe-validation-core" % circeValidationVersion,
  "io.tabmo" %% "circe-validation-extra-rules" % circeValidationVersion
)

lazy val scala3 = project
  .in(file("scala3"))
  .settings(
    scalaVersion := scala3Version,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
