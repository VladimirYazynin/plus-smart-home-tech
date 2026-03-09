package ru.practicum.commerce.payment.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.commerce.shoppingstore.feign.contract.StoreContract;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient extends StoreContract {
}
