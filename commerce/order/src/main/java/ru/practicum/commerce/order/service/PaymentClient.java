package ru.practicum.commerce.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.commerce.shoppingstore.feign.contract.PaymentContract;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient extends PaymentContract {
}
