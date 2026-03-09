package ru.practicum.commerce.delivery.service;

import ru.practicum.commerce.shoppingstore.dto.DeliveryDto;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto newDeliveryDto);

    void emulateSuccessfulDelivery(UUID orderId);

    void emulateItemPickup(UUID orderId);

    void emulateDeliveryDeclined(UUID orderId);

    BigDecimal calculateOrderDeliveryCost(OrderDto orderDto);
}
