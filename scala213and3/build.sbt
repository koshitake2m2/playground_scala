// https://docs.scala-lang.org/overviews/jdk-compatibility/overview.html
val scala2Version = "2.13.8"
val scala3Version = "3.3.1"

ThisBuild / scalafmtOnCompile := true

val isStrictCompile = Option(System.getProperty("local.compile")) == Some("strict")
val strictCompileOptions = if (isStrictCompile) Seq.empty else Seq("-Xfatal-warnings")

lazy val root = project
  .in(file("."))
  .settings(
    name := "playground_scala",
    scalacOptions ++= strictCompileOptions,
    version := "0.1.0"
  )
  .aggregate(scala3, scala2, playSample)

lazy val scala3 = project
  .in(file("scala3"))
  .settings(
    scalaVersion := scala3Version,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )

lazy val scala2 = project
  .in(file("scala2"))
  .settings(
    scalaVersion := scala2Version,
    resolvers += "Tabmo Myget Public".at("https://www.myget.org/F/tabmo-public/maven/"),
    libraryDependencies := cats ++ log4cats ++ scalatest ++ mysql ++ doobie ++ http4s ++ circe ++ scalikejdbc ++ awsSdkV2
  )

lazy val playSample = project
  .in(file("playSample"))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLogback)
  .settings(
    scalaVersion := scala2Version,
    resolvers += "Tabmo Myget Public".at("https://www.myget.org/F/tabmo-public/maven/"),
    libraryDependencies ++= cats ++ circe ++ log4cats ++ scalatest ++ mysql ++ play ++ scalikejdbc ++ awsSdkV2 ++ quartz
  )

lazy val playSample3 = project
  .in(file("playSample3"))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLogback)
  .settings(
    scalaVersion := scala3Version,
    resolvers += "Tabmo Myget Public".at("https://www.myget.org/F/tabmo-public/maven/"),
    libraryDependencies ++= log ++ play
  )

lazy val cats = Seq(
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "cats-free" % "2.7.0",
  "org.typelevel" %% "cats-effect" % "2.1.3"
)

lazy val slf4jVersion = "1.7.36"
// https://logback.qos.ch/dependencies.html
// 1.3.x is required jdk8, 1.4.x is required jdk11
lazy val logbackVersion = "1.3.7"
lazy val log4catsVersion = "1.1.1"
lazy val log4cats = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "io.chrisdavenport" %% "log4cats-core" % log4catsVersion,
  "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion
)

lazy val log = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "ch.qos.logback" % "logback-classic" % "1.4.7"
)

lazy val scalatest = Seq(
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.mockito" %% "mockito-scala" % "1.14.4" % Test,
  "org.mockito" %% "mockito-scala-scalatest" % "1.14.4" % Test
)

lazy val mysql = Seq(
  "mysql" % "mysql-connector-java" % "8.0.20"
)
// lazy val postgres = "org.postgresql" % "postgresql" % "42.5.4"

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
  "com.h2database" % "h2" % "2.1.214",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5"
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

// https://github.com/codingwell/scala-guice
lazy val codingwellScalaGuiceVersion = "5.1.1"
lazy val play = Seq(
  // https://mvnrepository.com/artifact/com.typesafe.play/play-ws
  // "com.typesafe.play" %% "play-guice" % "2.9.0-M4",
  // https://mvnrepository.com/artifact/com.typesafe.play/play-ahc-ws
  // "com.typesafe.play" %% "play-ahc-ws" % "2.9.0-M4",
  guice,
  ws,
  "net.codingwell" %% "scala-guice" % codingwellScalaGuiceVersion
)

lazy val awsSdkV2Version = "2.20.68"
lazy val awsSdkV2 = Seq(
  "software.amazon.awssdk" % "s3" % awsSdkV2Version,
  "software.amazon.awssdk" % "sqs" % awsSdkV2Version
)

// https://mvnrepository.com/artifact/org.quartz-scheduler/quartz
lazy val quartz = Seq("org.quartz-scheduler" % "quartz" % "2.3.2")
