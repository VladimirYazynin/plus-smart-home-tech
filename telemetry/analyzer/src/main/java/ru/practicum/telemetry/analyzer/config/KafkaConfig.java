package ru.practicum.telemetry.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    Consumer<String, HubEventAvro> hubEventConsumer(ConsumerConfig hubEventConsumerConfig) {
        Consumer<String, HubEventAvro> consumer = new KafkaConsumer<>(hubEventConsumerConfig.toKafkaProperties());
        consumer.subscribe(List.of(hubEventConsumerConfig.getTopic()));
        return consumer;
    }

    @Bean
    Consumer<String, SensorsSnapshotAvro> sensorsSnapshotConsumer(ConsumerConfig snapshotConsumerConfig) {
        Consumer<String, SensorsSnapshotAvro> consumer = new KafkaConsumer<>(snapshotConsumerConfig.toKafkaProperties());
        consumer.subscribe(List.of(snapshotConsumerConfig.getTopic()));
        return consumer;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.kafka.hub")
    public ConsumerConfig hubEventConsumerConfig() {
        return new ConsumerConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.kafka.snapshot")
    public ConsumerConfig snapshotConsumerConfig() {
        return new ConsumerConfig();
    }

    @Getter
    @Setter
    public static class ConsumerConfig {
        private String bootstrapServers;
        private String topic;
        private String groupId;
        private String keyDeserializer;
        private String valueDeserializer;
        private String clientId;

        public Properties toKafkaProperties() {
            Properties props = new Properties();
            props.setProperty("bootstrap.servers", bootstrapServers);
            props.setProperty("group.id", groupId);
            props.setProperty("key.deserializer", keyDeserializer);
            props.setProperty("value.deserializer", valueDeserializer);
            props.setProperty("client.id", clientId);

            return props;
        }
    }

}
