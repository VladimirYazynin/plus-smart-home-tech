package ru.practicum.telemetry.collector.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface EventMapper {
    SpecificRecordBase mapToAvro(HubEventProto event);

    SpecificRecordBase mapToAvro(SensorEventProto event);
}
