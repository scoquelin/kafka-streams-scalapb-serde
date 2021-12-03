inThisBuild(List(
  organization := "com.github.scoquelin",
  homepage := Some(url("https://github.com/scoquelin")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "scoquelin",
      "Sébastien Coquelin",
      "seb.coquelin@gmail.com",
      url("https://www.github.com/scoquelin")
    )
  )
))

lazy val root = project
  .in(file("."))
  .settings(
      name := "kafka-streams-scalapb-serde",
      organization := "com.github.scoquelin",
      scalaVersion := "2.13.3",
      crossScalaVersions := List("2.12.15", "2.13.7"),
      scalacOptions += "-deprecation"
  )

resolvers += "confluent" at "https://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
    "com.thesamet.scalapb" %% "scalapb-runtime"              % scalapb.compiler.Version.scalapbVersion % "protobuf",
    "io.confluent"          % "kafka-streams-protobuf-serde" % "6.0.2",
    "org.scalatest"        %% "scalatest"                    % "3.2.9"  % Test
)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

Test / PB.targets := Seq(
  PB.gens.java -> (Compile / sourceManaged).value,
  scalapb.gen(javaConversions=true) -> (Compile / sourceManaged).value
)
