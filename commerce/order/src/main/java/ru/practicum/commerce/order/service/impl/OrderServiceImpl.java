package ru.practicum.commerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.commerce.order.entity.OrderEntity;
import ru.practicum.commerce.order.mapper.OrderMapper;
import ru.practicum.commerce.order.repository.OrderRepository;
import ru.practicum.commerce.order.service.CartClient;
import ru.practicum.commerce.order.service.DeliveryClient;
import ru.practicum.commerce.order.service.OrderService;
import ru.practicum.commerce.order.service.PaymentClient;
import ru.practicum.commerce.order.service.WarehouseClient;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.AssemblyProductsForOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.CreateNewOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.DeliveryDto;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.PaymentDto;
import ru.practicum.commerce.shoppingstore.dto.ProductReturnRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;
import ru.practicum.commerce.shoppingstore.enums.DeliveryState;
import ru.practicum.commerce.shoppingstore.enums.OrderState;
import ru.practicum.commerce.shoppingstore.exception.BadRequestException;
import ru.practicum.commerce.shoppingstore.exception.NoOrderFoundException;
import ru.practicum.commerce.shoppingstore.exception.NotAuthorizedUserException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartClient cartClient;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getClientOrders(String username, Pageable pageable) {
        checkUsername(username);
        Page<OrderEntity> ordersPage = orderRepository.findAllByUsername(username, pageable);
        return orderMapper.toOrderDtoPage(ordersPage);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrderRequest) {
        String username;
        BookedProductsDto bookedProductsDto;
        AddressDto addressWarehouseDto;
        OrderEntity newOrder;
        ShoppingCartDto cartDto = newOrderRequest.getShoppingCartDto();
        username = cartClient.getUsernameById(cartDto.getShoppingCartId());
        bookedProductsDto = warehouseClient.checkProductQuantity(cartDto);
        addressWarehouseDto = warehouseClient.getWarehouseAddress();
        newOrder = orderMapper.toNewOrder(newOrderRequest, bookedProductsDto, username);
        orderRepository.save(newOrder);
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(addressWarehouseDto)
                .toAddress(newOrderRequest.getDeliveryAddress())
                .orderId(newOrder.getOrderId())
                .deliveryState(DeliveryState.CREATED)
                .build();
        newOrder.setDeliveryId(deliveryClient.createDelivery(deliveryDto).getDeliveryId());
        return orderMapper.toOrderDto(newOrder);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        OrderEntity orderEntity = getOrderById(productReturnRequest.getOrderId());
        OrderState orderStateCurrent = orderEntity.getState();
        if (orderStateCurrent == OrderState.NEW
                || orderStateCurrent == OrderState.CANCELED
                || orderStateCurrent == OrderState.PRODUCT_RETURNED) {
            throw new BadRequestException(String.format("В статусе %s невозможно вернуть заказ", orderStateCurrent));
        }
        validateReturnProducts(orderEntity, productReturnRequest);
        warehouseClient.returnProductsToWarehouse(orderEntity.getProducts());
        orderEntity.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        if (orderEntity.getState() == OrderState.PAID) {
            throw new BadRequestException("Заказ уже оплачен");
        }
        if (orderEntity.getState() == OrderState.ON_PAYMENT) {
            orderEntity.setState(OrderState.PAID);
            return orderMapper.toOrderDto(orderEntity);
        }
        if (!orderEntity.getState().equals(OrderState.ASSEMBLED)) {
            throw new BadRequestException(String.format("Заказ с ID= %s еще не собран", orderId));
        }
        orderEntity.setState(OrderState.ON_PAYMENT);
        PaymentDto paymentDto = paymentClient.initiatePayment(orderMapper.toOrderDto(orderEntity));
        orderEntity.setPaymentId(paymentDto.getPaymentId());
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto updateOrderStatusAfterPaymentFailure(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        orderEntity.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        if (orderEntity.getState() == OrderState.ON_DELIVERY) {
            orderEntity.setState(OrderState.DELIVERED);
            return orderMapper.toOrderDto(orderEntity);
        }
        if (orderEntity.getState() != OrderState.PAID) {
            throw new BadRequestException("Заказ не был оплачен");
        }
        deliveryClient.emulateItemPickup(orderEntity.getDeliveryId());
        orderEntity.setState(OrderState.ON_DELIVERY);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto updateOrderStatusToDeliveryFailed(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        orderEntity.setState(OrderState.DELIVERY_FAILED);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        orderEntity.setState(OrderState.COMPLETED);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto calculateOrderTotal(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        BigDecimal productsPrice = paymentClient.calculateProductsTotal(orderMapper.toOrderDto(orderEntity));
        orderEntity.setProductPrice(productsPrice);
        BigDecimal totalPrice = paymentClient.calculateTotalOrderAmount(orderMapper.toOrderDto(orderEntity));
        orderEntity.setTotalPrice(totalPrice);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        BigDecimal deliveryPryce = deliveryClient.calculateOrderDeliveryCost(orderMapper.toOrderDto(orderEntity));
        orderEntity.setDeliveryPrice(deliveryPryce);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto assembleOrder(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        if (orderEntity.getState() != OrderState.NEW) {
            throw new BadRequestException("Заказ в статусе не \"NEW\" нельзя отправить на сборку");
        }
        warehouseClient.assemblyOrderProducts(new AssemblyProductsForOrderRequest(orderId, orderEntity.getProducts()));
        orderEntity.setState(OrderState.ASSEMBLED);
        return orderMapper.toOrderDto(orderEntity);
    }

    @Override
    public OrderDto updateOrderStatusToAssemblyFailed(UUID orderId) {
        OrderEntity orderEntity = getOrderById(orderId);
        orderEntity.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.toOrderDto(orderEntity);
    }

    private void checkUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }
    }

    private OrderEntity getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(String.format("Заказ с ID= %s не найден", orderId)));
    }

    private void validateReturnProducts(OrderEntity orderEntity, ProductReturnRequest returnRequest) {
        Map<UUID, Long> orderProducts = orderEntity.getProducts();
        Map<UUID, Long> returnProducts = returnRequest.getProducts();
        if (orderProducts.equals(returnProducts)) {
            return;
        }
        List<String> missingInReturn = orderProducts.keySet().stream()
                .filter(productId -> !returnProducts.containsKey(productId))
                .map(UUID::toString)
                .collect(Collectors.toList());
        List<String> extraInReturn = returnProducts.keySet().stream()
                .filter(productId -> !orderProducts.containsKey(productId))
                .map(UUID::toString)
                .collect(Collectors.toList());
        List<String> quantityMismatches = orderProducts.entrySet().stream()
                .filter(entry -> {
                    UUID productId = entry.getKey();
                    Long orderedQty = entry.getValue();
                    Long returnQty = returnProducts.get(productId);
                    return returnQty != null && !orderedQty.equals(returnQty);
                })
                .map(entry -> String.format("ID=%s: заказано %d, к возврату %d",
                        entry.getKey(), entry.getValue(), returnProducts.get(entry.getKey())))
                .collect(Collectors.toList());
        String errorMessage = String.format(
                "Несоответствие списка товаров к возврату. Товаров в заказе: %d, товаров к возврату: %d. " +
                        "Отсутствующие в возврате: %s. Лишние в возврате: %s. Несоответствия количества: %s.",
                orderProducts.size(), returnProducts.size(),
                missingInReturn.isEmpty() ? "нет" : "[" + String.join(", ", missingInReturn) + "]",
                extraInReturn.isEmpty() ? "нет" : "[" + String.join(", ", extraInReturn) + "]",
                quantityMismatches.isEmpty() ? "нет" : "[" + String.join(", ", quantityMismatches) + "]"
        );
        throw new BadRequestException(errorMessage);
    }
}
