package ru.practicum.telemetry.analyzer.service;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Optional;

public interface AnalyzerService {
    Optional<List<DeviceActionRequest>> analyze(SensorsSnapshotAvro value);

    void analyze(HubEventAvro value);
}
