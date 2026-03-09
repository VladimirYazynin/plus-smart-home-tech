package ru.practicum.commerce.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.commerce.order.entity.OrderEntity;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.CreateNewOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.OrderDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(OrderEntity order);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "shoppingCartId", source = "request.shoppingCartDto.shoppingCartId")
    @Mapping(target = "products", source = "request.shoppingCartDto.products")
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "deliveryId", ignore = true)
    @Mapping(target = "state", constant = "NEW")
    @Mapping(target = "deliveryWeight", source = "bookedProductsDto.deliveryWeight")
    @Mapping(target = "deliveryVolume", source = "bookedProductsDto.deliveryVolume")
    @Mapping(target = "fragile", source = "bookedProductsDto.fragile")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "deliveryPrice", ignore = true)
    @Mapping(target = "productPrice", ignore = true)
    @Mapping(target = "username", source = "username")
    OrderEntity toNewOrder(CreateNewOrderRequest request, BookedProductsDto bookedProductsDto, String username);

    default Page<OrderDto> toOrderDtoPage(Page<OrderEntity> ordersPage) {
        List<OrderDto> dtos = ordersPage.getContent().stream()
                .map(this::toOrderDto)
                .toList();
        return new PageImpl<>(dtos, ordersPage.getPageable(), ordersPage.getTotalElements());
    }
}
