package ru.practicum.commerce.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.commerce.order.service.OrderService;
import ru.practicum.commerce.shoppingstore.dto.CreateNewOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.ProductReturnRequest;
import ru.practicum.commerce.shoppingstore.feign.contract.OrderContract;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderContract {

    private final OrderService orderService;

    @Override
    @GetMapping
    public Page<OrderDto> getClientOrders(@NotBlank @RequestParam String username, Pageable pageable) {
        return orderService.getClientOrders(username, pageable);
    }

    @Override
    @PutMapping
    public OrderDto createOrder(@Valid @RequestBody CreateNewOrderRequest newOrderRequest) {
        log.debug("Получен запрос на создание нового заказа. Тело запроса: {}", newOrderRequest);
        OrderDto response = orderService.createOrder(newOrderRequest);
        log.debug("Заказ успешно создан: {}", response);
        return response;
    }

    @Override
    @PostMapping("/return")
    public OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest productReturnRequest) {
        log.debug("Получен запрос на обновление статуса заказа на: PRODUCT_RETURNED. Тело запроса: {}",
                productReturnRequest);
        OrderDto response = orderService.returnOrder(productReturnRequest);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }

    @Override
    @PostMapping("/payment")
    public OrderDto payOrder(@RequestBody UUID orderId) {
        log.debug("Получен запрос на обновление статуса заказа на: ON_PAYMENT. uuid заказа: {}", orderId);
        OrderDto response = orderService.payOrder(orderId);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto updateOrderStatusAfterPaymentFailure(@RequestBody UUID orderId) {
        log.debug("Получен запрос на обновление статуса заказа на: PAYMENT_FAILED. uuid заказа: {}",
                orderId);
        OrderDto response = orderService.updateOrderStatusAfterPaymentFailure(orderId);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody UUID orderId) {
        log.debug("Получен запрос на обновление статуса заказа на: DELIVERED. uuid заказа: {}", orderId);
        OrderDto response = orderService.deliveryOrder(orderId);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto updateOrderStatusToDeliveryFailed(@RequestBody UUID orderId) {
        log.debug("Получен запрос на обновление статуса заказа на: DELIVERY_FAILED. uuid заказа: {}", orderId);
        OrderDto response = orderService.updateOrderStatusToDeliveryFailed(orderId);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }

    @Override
    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody UUID orderId) {
        log.debug("Получен запрос на обновление статуса заказа на: COMPLETED. uuid заказа: {}", orderId);
        OrderDto response = orderService.completeOrder(orderId);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateOrderTotal(@RequestBody UUID orderId) {
        log.debug("Получен запрос на расчёт стоимости заказа. uuid заказа: {}", orderId);
        OrderDto response = orderService.calculateOrderTotal(orderId);
        log.debug("Стоимость заказа рассчитана: {}", response);
        return response;
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID orderId) {
        log.debug("Получен запрос на расчёт стоимости доставки заказа. uuid заказа: {}", orderId);
        OrderDto response = orderService.calculateDeliveryCost(orderId);
        log.debug("Стоимость доставки заказа рассчитана: {}", response);
        return response;
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto assembleOrder(@RequestBody UUID orderId) {
        log.debug("Получен запрос на обновление статуса заказа на: ASSEMBLED. uuid заказа: {}", orderId);
        OrderDto response = orderService.assembleOrder(orderId);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto updateOrderStatusToAssemblyFailed(@RequestBody UUID orderId) {
        log.debug("Получен запрос на обновление статуса заказа на: ASSEMBLY_FAILED. uuid заказа: {}", orderId);
        OrderDto response = orderService.updateOrderStatusToAssemblyFailed(orderId);
        log.debug("Статус заказа обновлён: {}", response);
        return response;
    }
}
