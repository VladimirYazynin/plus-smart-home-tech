package ru.practicum.telemetry.collector.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {

    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return ScenarioAddedEventAvro
                .newBuilder()
                .setName(name)
                .setConditions(conditions.stream().map(c ->
                        ScenarioConditionAvro
                                .newBuilder()
                                .setSensorId(c.getSensorId())
                                .setOperation(ConditionOperationAvro.valueOf(c.getOperation().toString()))
                                .setType(ConditionTypeAvro.valueOf(c.getType().toString()))
                                .setValue(c.getValue()).build()).toList())
                .setActions(actions.stream().map(a ->
                        DeviceActionAvro
                                .newBuilder()
                                .setSensorId(a.getSensorId())
                                .setType(ActionTypeAvro.valueOf(a.getType().toString()))
                                .setValue(a.getValue())
                                .build()).toList())
                .build();
    }
}
