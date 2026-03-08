package ru.practicum.commerce.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.commerce.payment.service.PaymentService;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentDto initiatePayment(OrderDto orderDto) {
        return null;
    }

    @Override
    public BigDecimal calculateTotalOrderAmount(OrderDto orderDto) {
        return null;
    }

    @Override
    public void emulateSuccessfulPayment(UUID orderId) {

    }

    @Override
    public BigDecimal calculateProductsTotal(OrderDto orderDto) {
        return null;
    }

    @Override
    public void emulatePaymentDeclined(UUID orderId) {

    }
}
