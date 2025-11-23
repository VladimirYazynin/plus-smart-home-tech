package ru.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import ru.practicum.telemetry.collector.mapper.EventMapper;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    @Value(value = "${spring.kafka.producer.topic.sensors}")
    private String sensorsTopic;

    @Value(value = "${spring.kafka.producer.topic.hubs}")
    private String hubsTopic;

    private final EventMapper eventMapper;
    private final KafkaTemplate<String, SpecificRecordBase> producer;

    public void collectSensorEvent(SensorEventProto event, StreamObserver<Empty> responseObserver) {
        try {
            log.info("Обработка события датчика id:{}, hubId:{}", event.getId(), event.getHubId());
            SpecificRecordBase eventAvro = eventMapper.mapToAvro(event);
            producer.send(sensorsTopic, eventAvro);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    public void collectHubEvent(HubEventProto event, StreamObserver<Empty> responseObserver) {
        try {
            log.info("Обработка события хаба id:{}, hubId:{}, type:{}", event.getHubId());
            SpecificRecordBase eventAvro = eventMapper.mapToAvro(event);
            producer.send(hubsTopic, eventAvro);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }

    }
}
