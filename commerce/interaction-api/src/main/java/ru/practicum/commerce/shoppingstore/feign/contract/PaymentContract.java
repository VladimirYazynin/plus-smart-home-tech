package ru.practicum.commerce.shoppingstore.feign.contract;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentContract {

    @PostMapping
    PaymentDto initiatePayment(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    BigDecimal calculateTotalOrderAmount(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void emulateSuccessfulPayment(@RequestBody UUID paymentId);

    @PostMapping("/productCost")
    BigDecimal calculateProductsTotal(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void emulatePaymentDeclined(@RequestBody UUID paymentId);
}
