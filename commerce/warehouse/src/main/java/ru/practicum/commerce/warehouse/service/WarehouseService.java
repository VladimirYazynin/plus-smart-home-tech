package ru.practicum.commerce.warehouse.service;

import ru.practicum.commerce.shoppingstore.dto.AddProductToWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;

public interface WarehouseService {

    void addNewProductToWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    BookedProductsDto checkProductQuantityInWarehouse(ShoppingCartDto shoppingCartDto);

    AddressDto getWarehouseAddress();

    void updateProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest);
}
