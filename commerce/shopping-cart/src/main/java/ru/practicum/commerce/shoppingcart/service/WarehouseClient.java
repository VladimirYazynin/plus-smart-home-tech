package ru.practicum.commerce.shoppingcart.service;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.commerce.shoppingstore.dto.AddProductToWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;
import ru.practicum.commerce.shoppingstore.feign.contract.WarehouseContract;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient extends WarehouseContract {

    @Override
    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @Override
    @PutMapping
    void addNewProductToWarehouse(@Valid @RequestBody NewProductInWarehouseRequest newProduct);

    @Override
    @PostMapping("/check")
    BookedProductsDto checkProductQuantity(@Valid @RequestBody ShoppingCartDto shoppingCartDto);

    @Override
    @PostMapping("/add")
    void addProductsToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest product);
}
