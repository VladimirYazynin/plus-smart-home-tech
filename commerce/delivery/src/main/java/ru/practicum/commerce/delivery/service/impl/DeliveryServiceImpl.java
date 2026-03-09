package ru.practicum.commerce.delivery.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.commerce.delivery.service.DeliveryService;
import ru.practicum.commerce.shoppingstore.dto.DeliveryDto;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Override
    public DeliveryDto createDelivery(DeliveryDto newDeliveryDto) {
        return null;
    }

    @Override
    public void emulateSuccessfulDelivery(UUID orderId) {

    }

    @Override
    public void emulateItemPickup(UUID orderId) {

    }

    @Override
    public void emulateDeliveryDeclined(UUID orderId) {

    }

    @Override
    public BigDecimal calculateOrderDeliveryCost(OrderDto orderDto) {
        return null;
    }
}
