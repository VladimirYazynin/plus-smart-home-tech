package ru.practicum.commerce.payment.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.commerce.shoppingstore.feign.contract.OrderContract;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient extends OrderContract {
}
