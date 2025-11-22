package ru.practicum.telemetry.collector.service;

import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;

public interface EventService {
    void sendSensorEvent(SensorEvent event);

    void sendHubEvent(HubEvent event);
}
