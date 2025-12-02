package ru.practicum.telemetry.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandler {
    SpecificRecordBase handle(HubEventProto event);

    HubEventProto.PayloadCase getMessageType();
}
