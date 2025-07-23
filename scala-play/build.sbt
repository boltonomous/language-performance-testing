name := "scala-play-bakeoff"

version := "1.0"

scalaVersion := "2.13.12"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
  "com.typesafe.play" %% "play-json" % "2.10.3"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.claude.bakeoff.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.claude.bakeoff.binders._"

PlayKeys.devSettings := Seq("play.server.http.port" -> "8086")