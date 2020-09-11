package com.github.scoquelin.kafka.serdes.protobuf

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import tutorial.addressbook.Person
import tutorial.addressbook.Person.{PhoneNumber, PhoneType}

class KafkaScalaPBSerdeSpec extends AnyWordSpec with Matchers {

  val person = Person()
    .withName("Totoro")
    .withEmail("totoro@tsukamori.jp")
    .withPhones(Seq(PhoneNumber().withNumber("catbus").withType(PhoneType.MOBILE)))

  "KafkaScalaPBSerde using default companion constructor" should {
    "properly serialize and deserialize a scalapb message" in {
      val serdesConfig: java.util.Map[String, Any] = new java.util.HashMap()
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, "true")
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://fake-schema-registry-url")
      serdesConfig.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, classOf[tutorial.Addressbook.Person])

      val kafkaScalaPBSerde = new KafkaScalaPBSerde[tutorial.addressbook.Person, tutorial.Addressbook.Person](tutorial.addressbook.Person)
      kafkaScalaPBSerde.configure(serdesConfig, false)

      kafkaScalaPBSerde.deserializer().deserialize("fake-topic",
        kafkaScalaPBSerde.serializer().serialize("fake-topic", person)) shouldBe person

      kafkaScalaPBSerde.close()
    }

    "fail casting DynamicMessage if target Java protobuf class type has not been provided" in {
      val serdesConfig: java.util.Map[String, Any] = new java.util.HashMap()
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, "true")
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://fake-schema-registry-url")

      val kafkaScalaPBSerde = new KafkaScalaPBSerde[tutorial.addressbook.Person, tutorial.Addressbook.Person](tutorial.addressbook.Person)
      kafkaScalaPBSerde.configure(serdesConfig, false)

      try {
        kafkaScalaPBSerde.deserializer().deserialize("fake-topic",
          kafkaScalaPBSerde.serializer().serialize("fake-topic", person))
        fail()
      }
      catch {
        case e: ClassCastException if e.getMessage.startsWith("com.google.protobuf.DynamicMessage cannot be cast to") => // Expected
      }
      finally {
        kafkaScalaPBSerde.close()
      }
    }
  }

  "KafkaScalaPBSerde using companion/client constructor" should {
    "properly serialize and deserialize a scalapb message" in {

      val serdesConfig: java.util.Map[String, Any] = new java.util.HashMap()
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, "true")
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "fake-schema-registry-url")
      serdesConfig.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, classOf[tutorial.Addressbook.Person])
      val schemaRegistryClient = new MockSchemaRegistryClient

      val kafkaScalaPBSerde = new KafkaScalaPBSerde[tutorial.addressbook.Person, tutorial.Addressbook.Person](tutorial.addressbook.Person, schemaRegistryClient)
      kafkaScalaPBSerde.configure(serdesConfig, false)

      kafkaScalaPBSerde.deserializer().deserialize("fake-topic",
        kafkaScalaPBSerde.serializer().serialize("fake-topic", person)) shouldBe person

      kafkaScalaPBSerde.close()
    }

    "fail casting DynamicMessage if target Java protobuf class type has not been provided" in {
      val serdesConfig: java.util.Map[String, Any] = new java.util.HashMap()
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, "true")
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://fake-schema-registry-url")
      val schemaRegistryClient = new MockSchemaRegistryClient

      val kafkaScalaPBSerde = new KafkaScalaPBSerde[tutorial.addressbook.Person, tutorial.Addressbook.Person](tutorial.addressbook.Person, schemaRegistryClient)
      kafkaScalaPBSerde.configure(serdesConfig, false)

      try {
        kafkaScalaPBSerde.deserializer().deserialize("fake-topic",
          kafkaScalaPBSerde.serializer().serialize("fake-topic", person))
        fail()
      }
      catch {
        case e: ClassCastException if e.getMessage.startsWith("com.google.protobuf.DynamicMessage cannot be cast to") => // Expected
      }
      finally {
        kafkaScalaPBSerde.close()
      }
    }
  }

  "KafkaScalaPBSerde using companion/client/props/class constructor" should {
    "properly serialize and deserialize a scalapb message" in {

      val serdesConfig: java.util.Map[String, Any] = new java.util.HashMap()
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, "true")
      serdesConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "fake-schema-registry-url")
      val schemaRegistryClient = new MockSchemaRegistryClient

      val kafkaScalaPBSerde = new KafkaScalaPBSerde[tutorial.addressbook.Person, tutorial.Addressbook.Person](
        tutorial.addressbook.Person, schemaRegistryClient, serdesConfig, classOf[tutorial.Addressbook.Person])
      kafkaScalaPBSerde.configure(serdesConfig, false)

      kafkaScalaPBSerde.deserializer().deserialize("fake-topic",
        kafkaScalaPBSerde.serializer().serialize("fake-topic", person)) shouldBe person

      kafkaScalaPBSerde.close()
    }
  }
}
