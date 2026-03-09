package ru.practicum.commerce.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.commerce.shoppingstore.feign.contract.WarehouseContract;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient extends WarehouseContract {
}
