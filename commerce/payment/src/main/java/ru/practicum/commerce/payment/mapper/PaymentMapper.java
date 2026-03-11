package ru.practicum.commerce.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.commerce.payment.entity.PaymentEntity;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;
import ru.practicum.commerce.shoppingstore.dto.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toPaymentDto(PaymentEntity paymentEntity);

    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "paymentState", constant = "PENDING")
    @Mapping(target = "totalPayment", source = "totalPrice")
    @Mapping(target = "deliveryTotal", source = "deliveryPrice")
    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "feeTotal", ignore = true)
    PaymentEntity toPaymentEntity(OrderDto orderDto);
}
