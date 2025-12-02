package ru.practicum.telemetry.collector.mapper;

import com.google.protobuf.Timestamp;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.handler.hub.HubEventHandler;
import ru.practicum.telemetry.collector.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EventMapperImpl implements EventMapper {

    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    public EventMapperImpl(Set<SensorEventHandler> sensorEventHandlers,
                           Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageType,
                        Function.identity()
                ));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageType,
                        Function.identity()
                ));
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setTimestamp(getInstant(event.getTimestamp()))
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setPayload(toAvroPayload(event))
                .build();
    }

    @Override
    public SpecificRecordBase mapToAvro(HubEventProto event) {
        return HubEventAvro.newBuilder()
                .setTimestamp(getInstant(event.getTimestamp()))
                .setHubId(event.getHubId())
                .setPayload(toAvroPayload(event))
                .build();
    }

    private Instant getInstant(Timestamp event) {
        return Instant.ofEpochSecond(event.getSeconds(), event.getNanos());
    }

    private Object toAvroPayload(SensorEventProto event) {
        if (sensorEventHandlers.containsKey(event.getPayloadCase())) {
            return sensorEventHandlers.get(event.getPayloadCase()).handle(event);
        }
        throw new IllegalArgumentException(
                "Нет обработчик для данного типа: " + event.getPayloadCase()
        );
    }

    private Object toAvroPayload(HubEventProto event) {
        if (hubEventHandlers.containsKey(event.getPayloadCase())) {
            return hubEventHandlers.get(event.getPayloadCase()).handle(event);
        }
        throw new IllegalArgumentException(
                "Нет обработчик для данного типа: " + event.getPayloadCase()
        );
    }

}
