package ru.practicum.commerce.shoppingcart.service;

import ru.practicum.commerce.shoppingstore.dto.ChangeProductQuantityRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CartService {

    ShoppingCartDto getUserShoppingCart(String username);

    ShoppingCartDto deleteProducts(String username, Set<UUID> request);

    ShoppingCartDto changeProductQuantityInCart(String username, ChangeProductQuantityRequest newQuantity);

    ShoppingCartDto addProducts(String username, Map<UUID, Long> products);

    void deactivateUserCart(String username);

    String getUsernameById(UUID cartId);
}
