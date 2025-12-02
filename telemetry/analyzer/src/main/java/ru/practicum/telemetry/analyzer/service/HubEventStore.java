package ru.practicum.telemetry.analyzer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.analyzer.model.Action;
import ru.practicum.telemetry.analyzer.model.Condition;
import ru.practicum.telemetry.analyzer.model.Scenario;
import ru.practicum.telemetry.analyzer.model.ScenarioAction;
import ru.practicum.telemetry.analyzer.model.ScenarioActionId;
import ru.practicum.telemetry.analyzer.model.ScenarioCondition;
import ru.practicum.telemetry.analyzer.model.ScenarioConditionId;
import ru.practicum.telemetry.analyzer.model.Sensor;
import ru.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.practicum.telemetry.analyzer.repository.ScenarioActionRepository;
import ru.practicum.telemetry.analyzer.repository.ScenarioConditionRepository;
import ru.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.practicum.telemetry.analyzer.repository.SensorRepository;
import ru.practicum.telemetry.analyzer.repository.exception.EntityNotFoundException;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventStore {

    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;

    @Transactional
    public void saveDevice(String sensorId, String hubId) {
        Optional<Sensor> foundSensor = sensorRepository.findByIdAndHubId(sensorId, hubId);
        if (foundSensor.isEmpty()) {
            Sensor sensor = Sensor.builder()
                    .id(sensorId)
                    .hubId(hubId)
                    .build();
            sensorRepository.save(sensor);
        }
    }

    @Transactional
    public void saveScenario(HubEventAvro event, ScenarioAddedEventAvro added) {
        Optional<Scenario> foundScenario =
                scenarioRepository.findByHubIdAndName(event.getHubId(), added.getName());
        Scenario scenario;

        if (foundScenario.isEmpty()) {
            Scenario newScenario = Scenario.builder()
                    .hubId(event.getHubId())
                    .name(added.getName())
                    .build();

            scenario = scenarioRepository.save(newScenario);
        } else {
            scenario = foundScenario.get();
            scenarioActionRepository.deleteAllByScenarioId(scenario.getId());
            scenarioConditionRepository.deleteAllByScenarioId(scenario.getId());
        }

        added.getConditions().forEach(condition -> {
            Integer value = null;
            if (condition.getValue() instanceof Boolean boolVal) {
                value = boolVal ? 1 : 0;
            } else if (condition.getValue() instanceof Integer intVal) {
                value = intVal;
            }

            Condition newCondition = Condition.builder()
                    .type(condition.getType().toString())
                    .operation(condition.getOperation().toString())
                    .value(value)
                    .build();

            Condition savedCondition = conditionRepository.save(newCondition);

            Sensor sensor = sensorRepository.findByIdAndHubId(condition.getSensorId(), event.getHubId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Сенсор " + condition.getSensorId() + " не найден")
                    );

            ScenarioConditionId sensorId = ScenarioConditionId.builder()
                    .scenarioId(scenario.getId())
                    .sensorId(sensor.getId())
                    .conditionId(savedCondition.getId())
                    .build();

            ScenarioCondition scenarioCondition = ScenarioCondition.builder()
                    .id(sensorId)
                    .scenario(scenario)
                    .sensor(sensor)
                    .condition(savedCondition)
                    .build();

            scenarioConditionRepository.save(scenarioCondition);
        });

        added.getActions().forEach(action -> {
            Action newAction = Action.builder()
                    .type(action.getType().toString())
                    .value(action.getValue())
                    .build();

            Action saveAction = actionRepository.save(newAction);

            Sensor sensor = sensorRepository.findByIdAndHubId(action.getSensorId(), event.getHubId())
                    .orElseThrow(() -> new EntityNotFoundException("Сенсор " + action.getSensorId() + " не найден"));

            ScenarioActionId actionId = ScenarioActionId.builder()
                    .scenarioId(scenario.getId())
                    .sensorId(sensor.getId())
                    .actionId(saveAction.getId())
                    .build();

            ScenarioAction scenarioAction = ScenarioAction.builder()
                    .id(actionId)
                    .scenario(scenario)
                    .sensor(sensor)
                    .action(saveAction)
                    .build();

            scenarioActionRepository.save(scenarioAction);
        });
    }

    @Transactional
    public void removeDevice(String sensorId, String hubId) {
        sensorRepository.deleteByIdAndHubId(sensorId, hubId);
    }

    @Transactional
    public void removeScenario(String name, String hubId) {
        scenarioRepository.deleteByNameAndHubId(name, hubId);
    }

}
