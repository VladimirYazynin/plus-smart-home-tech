package ru.practicum.commerce.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.commerce.shoppingstore.feign.contract.DeliveryContract;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient extends DeliveryContract {
}
