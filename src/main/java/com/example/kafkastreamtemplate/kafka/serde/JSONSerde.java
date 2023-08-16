package com.example.kafkastreamtemplate.kafka.serde;

import com.example.kafkastreamtemplate.kafka.type.JsonSerdeCompatible;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
* A serde for any class that implements {@link JsonSerdeCompatible}.
* The classes also need to be registered in the {@code @JsonSubTypes} annotation on {@link JsonSerdeCompatible}.
 *
 * @param <T> The concrete type of class that gets de/serialized
* */
public class JSONSerde<T extends JsonSerdeCompatible> implements Serializer<T>, Deserializer<T>, Serde<T> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Override
    public T deserialize(String topic, byte[] data) {
        if(data == null) return null;

        try {
            return (T) OBJECT_MAPPER.readValue(data, JsonSerdeCompatible.class);
        }catch (final IOException e){
            throw new SerializationException(e);
        }
    }

    @Override
    public Serializer<T> serializer() {
        return this;
    }

    @Override
    public Deserializer<T> deserializer() {
        return this;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, T data) {
        if(data == null) return null;
        try {
            return OBJECT_MAPPER.writeValueAsBytes(data);
        }catch (final Exception e){
            throw new SerializationException("Error serializing JSON message",e);
        }
    }

    @Override
    public void close() {}
}
