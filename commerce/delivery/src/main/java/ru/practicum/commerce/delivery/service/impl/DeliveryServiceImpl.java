package ru.practicum.commerce.delivery.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.commerce.delivery.entity.AddressEntity;
import ru.practicum.commerce.delivery.entity.DeliveryEntity;
import ru.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.practicum.commerce.delivery.repository.AddressRepository;
import ru.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.practicum.commerce.delivery.service.DeliveryService;
import ru.practicum.commerce.delivery.service.OrderClient;
import ru.practicum.commerce.delivery.service.WarehouseClient;
import ru.practicum.commerce.shoppingstore.dto.DeliveryDto;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.ShippedToDeliveryRequest;
import ru.practicum.commerce.shoppingstore.enums.DeliveryState;
import ru.practicum.commerce.shoppingstore.exception.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto newDeliveryDto) {
        DeliveryEntity deliveryEntity = deliveryMapper.toDeliveryEntity(newDeliveryDto);
        return deliveryMapper.toDeliveryDto(deliveryRepository.save(deliveryEntity));
    }

    @Override
    public void emulateSuccessfulDelivery(UUID orderId) {
        DeliveryEntity delivery = getDeliveryByOrderId(orderId);
        orderClient.completeOrder(delivery.getOrderId());
        delivery.setDeliveryState(DeliveryState.DELIVERED);
    }

    @Override
    public void emulateItemPickup(UUID orderId) {
        DeliveryEntity deliveryEntity = getDeliveryByOrderId(orderId);
        warehouseClient.shippedToDelivery(
                new ShippedToDeliveryRequest(deliveryEntity.getOrderId(), deliveryEntity.getDeliveryId()));
        orderClient.assembleOrder(deliveryEntity.getOrderId());
        deliveryEntity.setDeliveryState(DeliveryState.IN_PROGRESS);
    }

    @Override
    public void emulateDeliveryDeclined(UUID orderId) {
        DeliveryEntity deliveryEntity = getDeliveryByOrderId(orderId);
        orderClient.updateOrderStatusToDeliveryFailed(deliveryEntity.getOrderId());
        deliveryEntity.setDeliveryState(DeliveryState.FAILED);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateOrderDeliveryCost(OrderDto orderDto) {
        DeliveryEntity deliveryEntity = deliveryRepository.findByOrderId(orderDto.getOrderId())
                .orElseThrow(() -> new NoDeliveryFoundException(
                        String.format("Доставка не найдена для заказа: {}", orderDto.getOrderId())));
        BigDecimal baseCost = BigDecimal.valueOf(5);
        BigDecimal price = caseWarehouseAddress(baseCost, deliveryEntity.getFromAddress());
        price = price.add(caseFragile(price, orderDto.getFragile()));
        price = price.add(orderDto.getDeliveryWeight().multiply(BigDecimal.valueOf(0.3)));
        price = price.add(orderDto.getDeliveryVolume().multiply(BigDecimal.valueOf(0.2)));
        price = price.add(caseDeliveryAddress(price, deliveryEntity.getFromAddress(), deliveryEntity.getToAddress()));
        return price;
    }

    private BigDecimal caseDeliveryAddress(BigDecimal cost, AddressEntity fromAddress, AddressEntity toAddress) {
        if (fromAddress.getStreet().equalsIgnoreCase(toAddress.getStreet())) {
            return BigDecimal.ZERO;
        }
        return cost.multiply(BigDecimal.valueOf(0.2));
    }

    private BigDecimal caseFragile(BigDecimal cost, Boolean fragile) {
        if (!fragile) {
            return BigDecimal.ZERO;
        }
        return cost.multiply(BigDecimal.valueOf(0.2));
    }

    private BigDecimal caseWarehouseAddress(BigDecimal baseCost, AddressEntity warehouseAddress) {
        if (warehouseAddress.getStreet().equalsIgnoreCase("ADDRESS_1")) {
            return baseCost;
        }
        return baseCost.add(baseCost.multiply(BigDecimal.valueOf(2)));
    }

    private DeliveryEntity getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(
                        String.format("Доставка для заказа с uuid: %s не найдена", orderId)));
    }
}
