package ru.practicum.telemetry.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public SpecificRecordBase handle(SensorEventProto event) {
        LightSensorProto sensor = event.getLightSensor();
        return LightSensorAvro.newBuilder()
                .setLuminosity(sensor.getLuminosity())
                .setLinkQuality(sensor.getLinkQuality())
                .build();
    }

}
