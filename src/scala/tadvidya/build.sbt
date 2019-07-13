name := """tadvidya"""
organization := "org.tadvidya"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(Common, PlayService, PlayLayoutPlugin)
  .settings(
    name := """tadvidya""",
  )

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "5.2"

libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.16"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.1"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" %  "3.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.2"

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"
libraryDependencies += "com.google.code.findbugs" % "jsr305" % "1.3.9"

maintainer := "yhemanth@gmail.com"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.tadvidya.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.tadvidya.binders._"
