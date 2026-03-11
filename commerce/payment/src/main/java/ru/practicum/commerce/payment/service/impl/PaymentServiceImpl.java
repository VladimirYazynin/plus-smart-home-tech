package ru.practicum.commerce.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.commerce.payment.entity.PaymentEntity;
import ru.practicum.commerce.payment.mapper.PaymentMapper;
import ru.practicum.commerce.payment.repository.PaymentRepository;
import ru.practicum.commerce.payment.service.OrderClient;
import ru.practicum.commerce.payment.service.PaymentService;
import ru.practicum.commerce.payment.service.ShoppingStoreClient;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.PaymentDto;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.enums.PaymentState;
import ru.practicum.commerce.shoppingstore.exception.BadRequestException;
import ru.practicum.commerce.shoppingstore.exception.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.commerce.shoppingstore.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Value("${pricing.nds-rate}")
    private BigDecimal ndsRate;

    @Override
    public PaymentDto initiatePayment(OrderDto orderDto) {
        PaymentEntity paymentEntity = paymentMapper.toPaymentEntity(orderDto);
        paymentEntity.setFeeTotal(calculateFeeTotal(orderDto));
        return paymentMapper.toPaymentDto(paymentRepository.save(paymentEntity));
    }

    @Override
    public BigDecimal calculateTotalOrderAmount(OrderDto orderDto) {
        BigDecimal productPrice = Objects.requireNonNullElse(orderDto.getProductPrice(), BigDecimal.ZERO);
        BigDecimal deliveryPrice = Objects.requireNonNullElse(orderDto.getDeliveryPrice(), BigDecimal.ZERO);
        BigDecimal feeTotal = productPrice.multiply(ndsRate);
        BigDecimal priceWithFee = productPrice.add(feeTotal);
        BigDecimal totalAmount = priceWithFee.add(deliveryPrice);
        return totalAmount;
    }

    @Override
    public void emulateSuccessfulPayment(UUID paymentId) {
        PaymentEntity paymentEntity = getPaymentById(paymentId);
        UUID orderId = paymentEntity.getOrderId();
        orderClient.payOrder(orderId);
        paymentEntity.setPaymentState(PaymentState.SUCCESS);
    }

    @Override
    public BigDecimal calculateProductsTotal(OrderDto orderDto) {
        Map<UUID, Long> productsInOrder = orderDto.getProducts();
        if (productsInOrder == null || productsInOrder.isEmpty()) {
            return BigDecimal.ZERO;
        }
        List<UUID> productIds = new ArrayList<>(productsInOrder.keySet());
        List<ProductDto> productInfos = new ArrayList<>();
        for (UUID productId : productIds) {
            productInfos.add(shoppingStoreClient.getProductById(productId));
        }
        Map<UUID, ProductDto> productInfoMap = productInfos.stream()
                .collect(Collectors.toMap(ProductDto::getProductId, product -> product));
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<UUID, Long> orderEntry : productsInOrder.entrySet()) {
            UUID productId = orderEntry.getKey();
            Long quantity = orderEntry.getValue();
            ProductDto productInfo = productInfoMap.get(productId);
            if (productInfo == null || productInfo.getPrice() == null) {
                throw new NotEnoughInfoInOrderToCalculateException(
                        String.format("Информация о товаре с uuid: %s не найдена.", productId));
            }
            BigDecimal subtotal = productInfo.getPrice().multiply(new BigDecimal(quantity));
            totalPrice = totalPrice.add(subtotal);
        }
        return totalPrice;
    }

    @Override
    public void emulatePaymentDeclined(UUID paymentId) {
        PaymentEntity paymentEntity = getPaymentById(paymentId);
        UUID orderId = paymentEntity.getOrderId();
        PaymentState currentPaymentState = paymentEntity.getPaymentState();
        if (currentPaymentState == PaymentState.FAILED || currentPaymentState == PaymentState.SUCCESS) {
            throw new BadRequestException(
                    String.format("Платёж имеет недопустимый для этого действия статус: %s", currentPaymentState));
        }
        orderClient.updateOrderStatusAfterPaymentFailure(orderId);
        paymentEntity.setPaymentState(PaymentState.FAILED);
    }

    private PaymentEntity getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(String.format("Платеж с uuid: %s не найден", paymentId)));
    }

    private BigDecimal calculateFeeTotal(OrderDto orderDto) {
        BigDecimal productPrice = Objects.requireNonNullElse(orderDto.getProductPrice(), BigDecimal.ZERO);
        return productPrice.multiply(ndsRate);
    }
}
