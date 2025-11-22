package ru.practicum.telemetry.collector.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;

@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public SpecificRecordBase mapToAvro(HubEvent event) {
        return null;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEvent event) {
        return null;
    }

}
