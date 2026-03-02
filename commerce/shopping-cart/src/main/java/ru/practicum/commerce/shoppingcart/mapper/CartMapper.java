package ru.practicum.commerce.shoppingcart.mapper;

import org.mapstruct.Mapper;
import ru.practicum.commerce.shoppingcart.entity.CartEntity;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;

@Mapper(componentModel = "spring")
public interface CartMapper {

    ShoppingCartDto toShoppingCartDto(CartEntity cart);
}
