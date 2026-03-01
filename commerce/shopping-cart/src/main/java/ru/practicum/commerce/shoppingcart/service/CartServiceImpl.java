package ru.practicum.commerce.shoppingcart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.commerce.shoppingcart.entity.CartEntity;
import ru.practicum.commerce.shoppingcart.mapper.CartMapper;
import ru.practicum.commerce.shoppingcart.repository.CartRepository;
import ru.practicum.commerce.shoppingstore.dto.ChangeProductQuantityRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;
import ru.practicum.commerce.shoppingstore.enums.CartState;
import ru.practicum.commerce.shoppingstore.exception.NotAuthorizedUserException;
import ru.practicum.commerce.shoppingstore.exception.NotFoundException;
import ru.practicum.commerce.shoppingstore.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getUserShoppingCart(String username) {
        checkUser(username);
        CartEntity cartEntity = cartRepository.findByUsername(username)
                .orElseGet(() -> CartEntity.builder()
                        .username(username)
                        .status(CartState.ACTIVE)
                        .products(new HashMap<>())
                        .build());
        return cartMapper.toShoppingCartDto(cartEntity);
    }

    @Override
    @Transactional
    public ShoppingCartDto deleteProducts(String username, Set<UUID> request) {
        checkUser(username);
        if (request == null || request.isEmpty()) {
            throw new ValidationException("Список товаров для удаления не может быть пустым или null");
        }
        CartEntity cart = cartRepository.findByUsernameAndStatus(username, CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Активной корзины покупок для пользователя %s не найдено", username)
                ));
        if (cart.getProducts() != null) {
            cart.getProducts().keySet().removeAll(request);
        }
        return cartMapper.toShoppingCartDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantityInCart(String username, ChangeProductQuantityRequest newQuantity) {
        //TODO для реализации нужен Feign
        return null;
    }

    @Override
    @Transactional
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> products) {
        //TODO для реализации нужен Feign
        return null;
    }

    @Override
    @Transactional
    public void deactivateUserCart(String username) {
        checkUser(username);
        CartEntity cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Корзина для пользователя %s не найдена", username)
                ));
        if (cart.getStatus() != CartState.DEACTIVATE) {
            cart.setStatus(CartState.DEACTIVATE);
            cartRepository.save(cart);
        }
    }

    private void checkUser(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }
    }
}
