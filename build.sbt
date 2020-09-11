inThisBuild(List(
  organization := "com.github.scoquelin",
  homepage := Some(url("https://github.com/scoquelin")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "scoquelin",
      "Sébastien Coquelin",
      "seb dot coquelin at gmail dot com",
      url("https://www.github.com/scoquelin")
    )
  )
))

lazy val root = project
  .in(file("."))
  .settings(
      name := "kafka-streams-scalapb-serde",
      organization := "com.github.scoquelin",
      scalaVersion := "2.12.10",
      crossScalaVersions := List("2.12.10", "2.13.3"),
      scalacOptions += "-deprecation"
  )

resolvers += "confluent" at "https://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
    "com.thesamet.scalapb" %% "scalapb-runtime"              % scalapb.compiler.Version.scalapbVersion % "protobuf",
    "io.confluent"          % "kafka-streams-protobuf-serde" % "5.5.1",
    "org.scalatest"        %% "scalatest"                    % "3.2.0"  % Test
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

PB.targets in Test := Seq(
  PB.gens.java -> (sourceManaged in Test).value,
  scalapb.gen(javaConversions = true) -> (sourceManaged in Test).value
)
