package ru.practicum.telemetry.analyzer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String operation;
    private Integer value;

    public boolean check(SensorStateAvro sensorStateAvro) {
        if (Objects.isNull(sensorStateAvro)) {
            return false;
        }
        int actual = chooseBySensorType(sensorStateAvro.getData());
        return switch (operation) {
            case "EQUALS" -> value == actual;
            case "GREATER_THAN" -> value < actual;
            case "LOWER_THAN" -> value > actual;
            default -> throw new IllegalStateException("Данная операция не поддерживается: " + operation);
        };
    }

    private int chooseBySensorType(Object data) {
        return switch (data) {
            case TemperatureSensorAvro temperatureSensorAvro -> getActualValue(temperatureSensorAvro);
            case ClimateSensorAvro climateSensorAvro -> getActualValue(climateSensorAvro);
            case MotionSensorAvro motionSensorAvro -> getActualValue(motionSensorAvro);
            case SwitchSensorAvro switchSensorAvro -> getActualValue(switchSensorAvro);
            case LightSensorAvro lightSensorAvro -> getActualValue(lightSensorAvro);
            default -> throw new IllegalArgumentException("Неизвестный SensorType: " + data.getClass().getSimpleName());
        };
    }

    private Integer getActualValue(ClimateSensorAvro climateSensorAvro) {
        return switch (type) {
            case "TEMPERATURE" -> climateSensorAvro.getTemperatureC();
            case "HUMIDITY" -> climateSensorAvro.getHumidity();
            case "CO2LEVEL" -> climateSensorAvro.getCo2Level();
            default -> 0;
        };
    }

    private Integer getActualValue(SwitchSensorAvro switchSensorAvro) {
        return switchSensorAvro.getState() ? 1 : 0;
    }

    private Integer getActualValue(MotionSensorAvro motionSensorAvro) {
        return motionSensorAvro.getMotion() ? 1 : 0;
    }

    private Integer getActualValue(LightSensorAvro lightSensorAvro) {
        return lightSensorAvro.getLuminosity();
    }

    private Integer getActualValue(TemperatureSensorAvro temperatureSensorAvro) {
        return temperatureSensorAvro.getTemperatureC();
    }

}
