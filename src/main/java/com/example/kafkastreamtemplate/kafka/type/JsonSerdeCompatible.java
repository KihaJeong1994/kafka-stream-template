package com.example.kafkastreamtemplate.kafka.type;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An interface for registering types that can be de/serialized with {@link com.example.kafkastreamtemplate.kafka.serde.JSONSerde}
 * */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_t")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrafficDto.class, name = "td"),
        @JsonSubTypes.Type(value = AggregationDto.class, name = "ad")
})
public interface JsonSerdeCompatible {
}
