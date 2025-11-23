package ru.practicum.telemetry.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public SpecificRecordBase handle(SensorEventProto event) {
        MotionSensorProto sensor = event.getMotionSensor();
        return MotionSensorAvro.newBuilder()
                .setVoltage(sensor.getVoltage())
                .setMotion(sensor.getMotion())
                .setLinkQuality(sensor.getLinkQuality())
                .build();
    }

}
