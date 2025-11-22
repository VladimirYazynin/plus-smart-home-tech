package ru.practicum.telemetry.collector.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;

public interface EventMapper {
    SpecificRecordBase mapToAvro(HubEvent event);

    SpecificRecordBase mapToAvro(SensorEvent event);
}
