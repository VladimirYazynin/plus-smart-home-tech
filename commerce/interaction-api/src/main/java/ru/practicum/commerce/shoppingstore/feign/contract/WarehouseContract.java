package ru.practicum.commerce.shoppingstore.feign.contract;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.commerce.shoppingstore.dto.AddProductToWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;

public interface WarehouseContract {

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PutMapping
    void addNewProductToWarehouse(@Valid @RequestBody NewProductInWarehouseRequest newProduct);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantity(@Valid @RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void addProductsToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest product);
}
