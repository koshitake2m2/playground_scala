addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.9.0-RC3")

// https://github.com/rtimush/sbt-rewarn
// 差分コンパイルでも既存のwarningが出るようになる
addSbtPlugin("com.timushev.sbt" % "sbt-rewarn" % "0.1.3")
