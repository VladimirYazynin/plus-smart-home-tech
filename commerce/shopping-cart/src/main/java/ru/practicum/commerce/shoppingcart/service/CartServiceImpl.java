package ru.practicum.commerce.shoppingcart.service;

import feign.FeignException;
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
    private final WarehouseClient warehouseClient;

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
    public ShoppingCartDto changeProductQuantityInCart(String username, ChangeProductQuantityRequest productQuantity) {
        checkUser(username);
        if (productQuantity == null) {
            throw new ValidationException("Запрос на обновление не может быть null");
        }
        CartEntity cartEntity = cartRepository.findByUsernameAndStatus(username, CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Активной корзины покупок для пользователя: %s не найдено", username)
                ));
        UUID productId = productQuantity.getProductId();
        Long newQuantity = productQuantity.getNewQuantity();
        Map<UUID, Long> products = cartEntity.getProducts();
        if (products == null) {
            products = new HashMap<>();
            cartEntity.setProducts(products);
        }
        if (!products.containsKey(productId)) {
            throw new ValidationException(String.format("Товар с uuid: %s отсутствует в корзине", productId));
        }
        Long oldQuantity = products.get(productId);
        if (newQuantity == 0) {
            products.remove(productId);
        } else {
            products.put(productId, newQuantity);
        }
        if (newQuantity > 0) {
            try {
                warehouseClient.checkProductQuantity(cartMapper.toShoppingCartDto(cartEntity));
            } catch (FeignException e) {
                products.put(productId, oldQuantity);
                throw new RuntimeException("В настоящее время склад недоступен", e);
            }
        }
        return cartMapper.toShoppingCartDto(cartRepository.save(cartEntity));
    }

    @Override
    @Transactional
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> products) {
        checkUser(username);
        if (products.isEmpty()) {
            throw new ValidationException("Список продуктов не может быть пустым");
        }
        CartEntity cartEntity = cartRepository.findByUsername(username)
                .orElseGet(() -> CartEntity.builder()
                        .username(username)
                        .status(CartState.ACTIVE)
                        .products(new HashMap<>())
                        .build());
        cartEntity = cartRepository.save(cartEntity);
        if (cartEntity.getProducts() == null) {
            cartEntity.setProducts(new HashMap<>());
        }
        for (var e : products.entrySet()) {
            cartEntity.getProducts().merge(e.getKey(), e.getValue(), Long::sum);
        }
        try {
            warehouseClient.checkProductQuantity(cartMapper.toShoppingCartDto(cartEntity));
        } catch (FeignException e) {
            throw new RuntimeException("Склад не доступен", e);
        }
        return cartMapper.toShoppingCartDto(cartRepository.save(cartEntity));
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
