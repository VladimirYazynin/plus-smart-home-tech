package ru.practicum.commerce.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.commerce.shoppingstore.feign.contract.CartContract;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartClient extends CartContract {
}
