package ru.practicum.telemetry.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Component
public class ScenarioAddedEventHandler implements  HubEventHandler {

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public SpecificRecordBase handle(HubEventProto event) {
        ScenarioAddedEventProto hubEvent = event.getScenarioAdded();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(hubEvent.getName())
                .setActions(getDeviceActions(hubEvent.getActionList()))
                .setConditions(getConditions(hubEvent.getConditionList()))
                .build();
    }

    private List<ScenarioConditionAvro> getConditions(List<ScenarioConditionProto> conditionList) {
        return conditionList.stream()
                .map(c -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(c.getSensorId())
                        .setOperation(ConditionOperationAvro.valueOf(c.getOperation().toString()))
                        .setType(ConditionTypeAvro.valueOf(c.getType().toString()))
                        .setValue(c.hasIntValue() ? c.getIntValue() : c.getBoolValue())
                        .build())
                .toList();
    }

    private List<DeviceActionAvro> getDeviceActions(List<DeviceActionProto> actionList) {
        return actionList.stream()
                .map(a -> DeviceActionAvro.newBuilder()
                        .setSensorId(a.getSensorId())
                        .setType(ActionTypeAvro.valueOf(a.getType().toString()))
                        .setValue(a.getValue())
                        .build())
                .toList();
    }

}
