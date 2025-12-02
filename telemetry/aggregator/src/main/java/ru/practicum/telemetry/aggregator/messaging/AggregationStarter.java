package ru.practicum.telemetry.aggregator.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    @Value("${aggregator.kafka.topic.sensors}")
    private String sensorTopic;

    @Value("${spring.kafka.consumer.properties.fetch.max.wait.ms}")
    private int fetchMaxWaitMs;

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final AggregatorKafkaProducer producer;
    private final AggregationService aggregationService;
    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new ConcurrentHashMap<>();

    public void start() {
        log.info("Kafka Consumer успешно внедрён");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Получен сигнал завершения");
            consumer.wakeup();
        }));

        try {
            String topic = sensorTopic;
            consumer.subscribe(Collections.singletonList(topic));
            log.info("Осуществлена подписка на топик: {}", topic);

            while (true) {
                log.debug("Ожидание новых сообщений");
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(Duration.ofMillis(fetchMaxWaitMs));
                handleMessages(records);
            }

        } catch (WakeupException ignored) {
            log.info("Consumer пробуждён");
        } catch (Exception e) {
            log.error("Произошла ошибка в цикле Consumer", e);
        } finally {
            commitOffsets();
        }
    }

    private void handleMessages(ConsumerRecords<String, SensorEventAvro> records) {
        for (ConsumerRecord<String, SensorEventAvro> record : records) {
            log.debug("Получена запись: partition={}, offset={}, value={}", record.partition(), record.offset(),
                    record.value());
            SensorEventAvro avroSensorEvent = record.value();

            aggregationService.updateSnapshot(avroSensorEvent).ifPresent(snapshot -> {
                producer.send(snapshot);
                currentOffsets.put(new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1));
            });
        }

        if (!currentOffsets.isEmpty()) {
            consumer.commitAsync(currentOffsets, (offsets, ex) -> {
                if (ex != null) {
                    log.error("Ошибка при коммите оффсетов {}", offsets, ex);
                }
            });
        }
    }

    private void commitOffsets() {
        try {
            log.info("Коммитим финальные оффсеты");
            consumer.commitSync(currentOffsets);
        } catch (Exception e) {
            log.error("Ошибка при коммите финальных оффсетов", e);
        } finally {
            log.info("Закрываем Consumer");
            consumer.close();
        }
    }
}
