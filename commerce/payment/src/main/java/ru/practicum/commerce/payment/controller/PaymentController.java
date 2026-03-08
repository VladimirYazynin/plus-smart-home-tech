package ru.practicum.commerce.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.commerce.payment.service.PaymentService;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto initiatePayment(@Valid @RequestBody OrderDto orderDto) {
        log.debug("Начат процесс формирования оплаты для заказа: {}", orderDto);
        PaymentDto response = paymentService.initiatePayment(orderDto);
        log.debug("Оплата сформирована: {}", response);
        return response;
    }

    @PostMapping("/totalCost")
    public BigDecimal calculateTotalOrderAmount(@Valid @RequestBody OrderDto orderDto) {
        log.debug("Получен запрос на расчёт полной стоимости для заказа: {}", orderDto);
        BigDecimal response = paymentService.calculateTotalOrderAmount(orderDto);
        log.debug("Полная стоимость для заказа - {} составит: {}", orderDto.getOrderId(), response);
        return response;
    }

    @PostMapping("/refund")
    public void emulateSuccessfulPayment(@RequestBody UUID paymentId) {
        log.debug("Начат процесс подтверждения успешной оплаты: {}", paymentId);
        paymentService.emulateSuccessfulPayment(paymentId);
        log.debug("Оплата с uuid: {} прошла успешно", paymentId);
    }

    @PostMapping("/productCost")
    public BigDecimal calculateProductsTotal(@Valid @RequestBody OrderDto orderDto) {
        log.debug("Получен запрос на расчёт стоимости товаров в заказе: {}", orderDto);
        BigDecimal response = paymentService.calculateProductsTotal(orderDto);
        log.debug("Итоговая стоимость всех товаров в заказе составит: {}", response);
        return response;
    }

    @PostMapping("/failed")
    public void emulatePaymentDeclined(@RequestBody UUID paymentId) {
        log.debug("Начат процесс подтверждения отказа в оплаты: {}", paymentId);
        paymentService.emulatePaymentDeclined(paymentId);
        log.debug("Оплата с uuid: {} отклонена", paymentId);
    }
}
