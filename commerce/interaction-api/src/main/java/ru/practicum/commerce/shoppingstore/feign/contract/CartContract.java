package ru.practicum.commerce.shoppingstore.feign.contract;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.commerce.shoppingstore.dto.ChangeProductQuantityRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CartContract {

    @GetMapping
    ShoppingCartDto getUserShoppingCart(@RequestParam(value = "username") String username);

    @PostMapping("/remove")
    ShoppingCartDto deleteProductsFromCart(@RequestParam(value = "username") String username,
                                           @RequestBody Set<UUID> productUUIDs);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantityInCart(@RequestParam(value = "username") String username,
                                                @RequestBody ChangeProductQuantityRequest newQuantity);

    @PutMapping
    ShoppingCartDto addProducts(@RequestParam(value = "username") String username,
                                @RequestBody Map<UUID, Long> products);

    @DeleteMapping
    void deactivateUserCart(@RequestParam(value = "username") String username);

    @GetMapping("/name/{cartId}")
    String getUsernameById(@PathVariable("cartId") UUID cartId);
}
