package ru.practicum.commerce.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.commerce.shoppingstore.dto.CreateNewOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {

    Page<OrderDto> getClientOrders(String username, Pageable pageable);

    OrderDto createOrder(CreateNewOrderRequest newOrderRequest);

    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    OrderDto payOrder(UUID orderId);

    OrderDto updateOrderStatusAfterPaymentFailure(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto updateOrderStatusToDeliveryFailed(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    OrderDto calculateOrderTotal(UUID orderId);

    OrderDto calculateDeliveryCost(UUID orderId);

    OrderDto assembleOrder(UUID orderId);

    OrderDto updateOrderStatusToAssemblyFailed(UUID orderId);
}
