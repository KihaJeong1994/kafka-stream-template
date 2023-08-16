package com.example.kafkastreamtemplate.kafka.type;

public class AggregationDto implements JsonSerdeCompatible{
    public long max;
    public long min;
    public double avg;
    public long sum;
    public int cnt;

    public AggregationDto() {
        this.max = 0;
        this.min = Long.MAX_VALUE;
        this.cnt = 0;
        this.sum = 0;
    }
}
