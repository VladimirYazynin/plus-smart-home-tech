package ru.practicum.telemetry.aggregator.messaging;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AggregationService {

    private Map<String, SensorsSnapshotAvro> snapshotsByHubId = new ConcurrentHashMap<>();

    public Optional<SensorsSnapshotAvro> updateSnapshot(SensorEventAvro avroSensorEvent) {
        // Проверяем, есть ли снапшот для event.getHubId(),Если снапшот есть, то достаём его
        // Если нет, то создаём новый
        SensorsSnapshotAvro currentSnapshot = snapshotsByHubId.computeIfAbsent(avroSensorEvent.getHubId(), hubId ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setSensorsState(new HashMap<>())
                        .setTimestamp(avroSensorEvent.getTimestamp())
                        .build()
        );

        // Проверяем, есть ли в снапшоте данные для event.getId()
        // Если данные есть, то достаём их в переменную oldState
        SensorStateAvro oldState = currentSnapshot.getSensorsState().get(avroSensorEvent.getId());

        // Если время создания старого события позже или событие осталось без изменения, возвращаем пустой Optional
        if (oldState != null && (
                oldState.getTimestamp().isAfter(avroSensorEvent.getTimestamp()) ||
                        oldState.getData().equals(avroSensorEvent.getPayload())
        )) {
            return Optional.empty();
        }

        SensorStateAvro newSensorState = SensorStateAvro.newBuilder()
                .setTimestamp(avroSensorEvent.getTimestamp())
                .setData(avroSensorEvent.getPayload())
                .build();

        currentSnapshot.getSensorsState().put(avroSensorEvent.getId(), newSensorState);
        currentSnapshot.setTimestamp(avroSensorEvent.getTimestamp());

        return Optional.of(currentSnapshot);
    }
}
