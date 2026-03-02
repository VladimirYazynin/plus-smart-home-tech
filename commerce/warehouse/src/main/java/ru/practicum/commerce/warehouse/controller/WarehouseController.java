package ru.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commerce.shoppingstore.dto.*;
import ru.practicum.commerce.warehouse.service.WarehouseService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }

    @PutMapping
    public void addNewProductToWarehouse(@Valid @RequestBody NewProductInWarehouseRequest newProduct) {
        log.debug("Получен запрос на добавление нового товара на склад: {}", newProduct);
        warehouseService.addNewProductToWarehouse(newProduct);
        log.debug("Новый товар успешно добавлен на склад");
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantity(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductQuantityInWarehouse(shoppingCartDto);
    }

    @PostMapping("/add")
    public void addProductsToWarehouse(AddProductToWarehouseRequest product) {
        log.debug("Получен запрос на пополнение товара: {}", product);
        warehouseService.updateProductToWarehouse(product);
        log.debug("Товар успешно пополнен");
    }
}
