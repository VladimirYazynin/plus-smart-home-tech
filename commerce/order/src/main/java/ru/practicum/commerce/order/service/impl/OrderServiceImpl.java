package ru.practicum.commerce.order.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.commerce.order.service.OrderService;
import ru.practicum.commerce.shoppingstore.dto.CreateNewOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.ProductReturnRequest;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Page<OrderDto> getClientOrders(String username, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrderRequest) {
        return null;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        return null;
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto updateOrderStatusAfterPaymentFailure(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto updateOrderStatusToDeliveryFailed(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateOrderTotal(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto assembleOrder(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto updateOrderStatusToAssemblyFailed(UUID orderId) {
        return null;
    }
}
