# Context

You have generated ScalaPB classes for your proto files.

You want to use Kafka or Kafka Streams and possibly leverage Schema Registry.

The official Kafka protobuf serdes accepts messages that extends Java `com.google.protobuf.Message` to be serialized/deserialized.

This project is - on purpose - a very thin layer on top of official Kafka protobuf serdes will allow you to provide `scalapb.GeneratedMessage` and will take care of the rest for you while - hopefully - remaining compatible with future versions of official serdes without much effort.

# Setup

First you need to enable Java conversions in `scalapb` to allow bridging from Scala PB to Java PB classes.

## sbt

In your `build.sbt` :

```
PB.targets in Compile := Seq(
  PB.gens.java -> (sourceManaged in Compile).value,
  scalapb.gen(javaConversions=true) -> (sourceManaged in Compile).value
)
```

## Gradle

Assuming you're using https://github.com/google/protobuf-gradle-plugin :

```
...

  generateProtoTasks {
    all().each { task ->
      task.plugins {
          scalapb {
            option 'java_conversions'
          }
      }
    }
  }
...
```

# Usage

Check the tests :smiling_imp:
