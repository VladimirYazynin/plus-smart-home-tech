package ru.practicum.commerce.shoppingcart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.commerce.shoppingcart.service.CartService;
import ru.practicum.commerce.shoppingstore.dto.ChangeProductQuantityRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ShoppingCartDto getUserShoppingCart(@RequestParam(value = "username") String username) {
        return cartService.getUserShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto deleteProductsFromCart(@RequestParam(value = "username") String username,
                                                  @RequestBody Set<UUID> productUUIDs) {
        log.debug("Получен запрос на удаление товаров: {} из корзины пользователя: {}", productUUIDs, username);
        ShoppingCartDto result = cartService.deleteProducts(username, productUUIDs);
        log.debug("Товары удалены, текущее состояние корзины: {}", result);
        return result;
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantityInCart(@RequestParam(value = "username") String username,
                                                       @RequestBody ChangeProductQuantityRequest newQuantity) {
        log.debug("Получен запрос на обновление количества товара: {}, в корзине пользователя: {}",
                newQuantity,
                username);
        ShoppingCartDto result = cartService.changeProductQuantityInCart(username, newQuantity);
        log.debug("Количество товара успешно обновлено: {}", result);
        return result;
    }

    @PutMapping
    public ShoppingCartDto addProducts(@RequestParam(value = "username") String username,
                           @RequestBody Map<UUID, Long> products) {
        log.debug("Получен запрос на добавление товаров: {}, в корзину пользователя: {}", products, username);
        ShoppingCartDto result = cartService.addProducts(username, products);
        log.debug("Товары успешно добавлены: {}", result);
        return result;
    }

    @DeleteMapping
    public void deactivateUserCart(@RequestParam(value = "username") String username) {
        log.debug("Получен запрос на деактивацию корзины пользователя: {}", username);
        cartService.deactivateUserCart(username);
        log.debug("Корзина успешно деактивирована");
    }
}
