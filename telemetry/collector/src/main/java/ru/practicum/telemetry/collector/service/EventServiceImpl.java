package ru.practicum.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.mapper.EventMapper;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final KafkaTemplate<String, SpecificRecordBase> producer;
    private final EventMapper eventMapper;
    @Value(value = "${spring.kafka.producer.topic.sensors}")
    private String sensorsTopic;
    @Value(value = "${spring.kafka.producer.topic.hubs}")
    private String hubsTopic;

    @Override
    public void sendSensorEvent(SensorEvent event) {
        SpecificRecordBase eventAvro = eventMapper.mapToAvro(event);
        producer.send(sensorsTopic, eventAvro);
    }

    @Override
    public void sendHubEvent(HubEvent event) {
        SpecificRecordBase eventAvro = eventMapper.mapToAvro(event);
        producer.send(hubsTopic, eventAvro);
    }

}
