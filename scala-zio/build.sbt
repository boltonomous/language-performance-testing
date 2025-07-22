ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "scala-zio-bakeoff",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.9",
      "dev.zio" %% "zio-http" % "3.0.1",
      "dev.zio" %% "zio-json" % "0.7.3",
      "ch.qos.logback" % "logback-classic" % "1.4.14",
      "org.mongodb" % "mongodb-driver-sync" % "4.11.1"
    ),
    assembly / mainClass := Some("com.claude.bakeoff.Application"),
    assembly / assemblyJarName := "scala-zio-bakeoff.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
      case PathList("META-INF", "native-image", xs @ _*) => MergeStrategy.first
      case PathList("module-info.class") => MergeStrategy.first
      case x => MergeStrategy.defaultMergeStrategy(x)
    }
  )