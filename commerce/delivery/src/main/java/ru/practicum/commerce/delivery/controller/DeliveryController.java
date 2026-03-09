package ru.practicum.commerce.delivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commerce.delivery.service.DeliveryService;
import ru.practicum.commerce.shoppingstore.dto.DeliveryDto;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.feign.contract.DeliveryContract;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryContract {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto newDeliveryDto) {
        log.debug("Получен запрос на создание новой доставки. Тело запроса: {}", newDeliveryDto);
        DeliveryDto response = deliveryService.createDelivery(newDeliveryDto);
        log.debug("Доставка успешно создана: {}", response);
        return response;
    }

    @PostMapping("/successful")
    public void emulateSuccessfulDelivery(@RequestBody UUID orderId) {
        log.debug("Получен запрос на подтверждение успешной доставки заказа с uuid: {}", orderId);
        deliveryService.emulateSuccessfulDelivery(orderId);
        log.debug("Доставка подтверждена");
    }

    @PostMapping("/picked")
    public void emulateItemPickup(@RequestBody UUID orderId) {
        log.debug("Получен запрос на подтверждение передачи заказа с uuid: {} в доставку", orderId);
        deliveryService.emulateItemPickup(orderId);
        log.debug("Заказ успешно передан в доставку");
    }

    @PostMapping("/failed")
    public void emulateDeliveryDeclined(@RequestBody UUID orderId) {
        log.debug("Получен запрос на установку статуса неудачного вручения заказа с uuid: {}", orderId);
        deliveryService.emulateDeliveryDeclined(orderId);
        log.debug("");
    }

    @PostMapping("/cost")
    public BigDecimal calculateOrderDeliveryCost(@Valid @RequestBody OrderDto orderDto) {
        log.debug("Получен запрос на расчёт полной стоимости доставки заказа: {}", orderDto);
        BigDecimal response = deliveryService.calculateOrderDeliveryCost(orderDto);
        log.debug("Полная стоимость доставки заказа составит: {}", response);
        return response;
    }
}
