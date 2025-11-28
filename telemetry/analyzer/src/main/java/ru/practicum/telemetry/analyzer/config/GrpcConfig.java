package ru.practicum.telemetry.analyzer.config;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Configuration
@ConfigurationProperties("grpc.client.hub-router")
public class GrpcConfig {

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterControllerBlockingStub;

    @Bean
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient() {
        return this.hubRouterControllerBlockingStub;
    }

}
