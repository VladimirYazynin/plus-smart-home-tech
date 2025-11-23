package ru.practicum.telemetry.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    public SpecificRecordBase handle(SensorEventProto event) {
        ClimateSensorProto sensor = event.getClimateSensor();
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(sensor.getTemperatureC())
                .setHumidity(sensor.getHumidity())
                .setCo2Level(sensor.getCo2Level())
                .build();
    }

}
