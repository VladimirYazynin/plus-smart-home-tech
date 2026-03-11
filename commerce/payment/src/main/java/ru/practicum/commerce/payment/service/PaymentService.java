package ru.practicum.commerce.payment.service;

import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    PaymentDto initiatePayment(OrderDto orderDto);

    BigDecimal calculateTotalOrderAmount(OrderDto orderDto);

    void emulateSuccessfulPayment(UUID orderId);

    BigDecimal calculateProductsTotal(OrderDto orderDto);

    void emulatePaymentDeclined(UUID orderId);
}
