package ru.practicum.commerce.shoppingstore.feign.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commerce.shoppingstore.dto.CreateNewOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.ProductReturnRequest;

import java.util.UUID;

public interface OrderContract {

    @GetMapping
    Page<OrderDto> getClientOrders(@NotBlank @RequestParam String username, Pageable pageable);

    @PutMapping
    OrderDto createOrder(@Valid @RequestBody CreateNewOrderRequest newOrderRequest);

    @PostMapping("/return")
    OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest productReturnRequest);

    @PostMapping("/payment")
    OrderDto payOrder(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto updateOrderStatusAfterPaymentFailure(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliveryOrder(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto updateOrderStatusToDeliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateOrderTotal(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembleOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto updateOrderStatusToAssemblyFailed(@RequestBody UUID orderId);
}
