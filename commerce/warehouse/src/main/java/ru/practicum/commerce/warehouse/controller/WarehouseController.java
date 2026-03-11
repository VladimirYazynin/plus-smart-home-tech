package ru.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.commerce.shoppingstore.dto.AddProductToWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.AssemblyProductsForOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.ShippedToDeliveryRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;
import ru.practicum.commerce.shoppingstore.feign.contract.WarehouseContract;
import ru.practicum.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseContract {

    private final WarehouseService warehouseService;

    @Override
    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }

    @Override
    @PutMapping
    public void addNewProductToWarehouse(@Valid @RequestBody NewProductInWarehouseRequest newProduct) {
        log.debug("Получен запрос на добавление нового товара на склад: {}", newProduct);
        warehouseService.addNewProductToWarehouse(newProduct);
        log.debug("Новый товар успешно добавлен на склад");
    }

    @Override
    @PostMapping("/check")
    public BookedProductsDto checkProductQuantity(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductQuantityInWarehouse(shoppingCartDto);
    }

    @Override
    @PostMapping("/add")
    public void addProductsToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest product) {
        log.debug("Получен запрос на пополнение товара: {}", product);
        warehouseService.updateProductToWarehouse(product);
        log.debug("Товар успешно пополнен");
    }

    @Override
    @PostMapping("/shipped")
    public void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequest shippedToDeliveryRequest) {
        log.debug("Получен запрос на передачу товаров в доставку. Тело запроса: {}", shippedToDeliveryRequest);
        warehouseService.shippedToDelivery(shippedToDeliveryRequest);
        log.debug("Товара успешно переданы в доставку");
    }

    @Override
    @PostMapping("/return")
    public void returnProductsToWarehouse(@RequestBody @NotEmpty Map<
                @NotNull UUID,
                @NotNull @Positive Long> returnProducts) {
        log.debug("Получен запрос на возврат на склад товаров: {}", returnProducts);
        warehouseService.returnProductsToWarehouse(returnProducts);
        log.debug("Товары: {} возвращены", returnProducts);
    }

    @Override
    @PostMapping("/assembly")
    public void assemblyOrderProducts(
            @Valid @RequestBody AssemblyProductsForOrderRequest assemblyProductsForOrderRequest) {
            log.debug("Получен запрос на сборку товаров и подготовке к отправке: {}", assemblyProductsForOrderRequest);
            warehouseService.assemblyOrderProducts(assemblyProductsForOrderRequest);
            log.debug("Товары собраны и подготовлены к отправке");
    }
}
