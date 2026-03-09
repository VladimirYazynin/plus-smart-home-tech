package ru.practicum.commerce.shoppingstore.feign.contract;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.commerce.shoppingstore.dto.DeliveryDto;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryContract {

    @PutMapping
    DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto newDeliveryDto);

    @PostMapping("/successful")
    void emulateSuccessfulDelivery(@RequestBody UUID orderId);

    @PostMapping("/picked")
    void emulateItemPickup(@RequestBody UUID orderId);

    @PostMapping("/failed")
    void emulateDeliveryDeclined(@RequestBody UUID orderId);

    @PostMapping("/cost")
    BigDecimal calculateOrderDeliveryCost(@Valid @RequestBody OrderDto orderDto);
}
