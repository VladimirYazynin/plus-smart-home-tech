package ru.practicum.telemetry.kafka.serializer;

import org.apache.avro.specific.SpecificRecordBase;

public interface AvroPayload {
    SpecificRecordBase toPayload();
}
