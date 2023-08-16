package com.example.kafkastreamtemplate.kafka.pipeline;

import com.example.kafkastreamtemplate.kafka.serde.JSONSerde;
import com.example.kafkastreamtemplate.kafka.type.AggregationDto;
import com.example.kafkastreamtemplate.kafka.type.JsonSerdeCompatible;
import com.example.kafkastreamtemplate.kafka.type.TrafficDto;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

@Component
public class TrafficAggregationProcessor {

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder){
        KStream<String, TrafficDto> traffics = streamsBuilder
                .stream("streams-traffic-input", Consumed.with(Serdes.String(), new JSONSerde<>()));

        KTable<Windowed<String>, AggregationDto> aggregation = traffics
                .mapValues(t -> t.resTime)
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofSeconds(1), Duration.ofMillis(10)))
                .aggregate(() -> new AggregationDto(),
                        ((key, value, aggregate) -> {
                            aggregate.cnt++;
                            aggregate.sum += value;
                            aggregate.max = Math.max(aggregate.max, value);
                            aggregate.min = Math.min(aggregate.min, value);
                            aggregate.avg = aggregate.sum / aggregate.cnt;
                            return aggregate;
                        })
                        , Materialized.with(Serdes.String(), new JSONSerde<>())
                );
        aggregation.toStream().to("streams-aggregation-output");

    }
}
