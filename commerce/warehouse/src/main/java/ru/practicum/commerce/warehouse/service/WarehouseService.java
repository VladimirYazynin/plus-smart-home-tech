package ru.practicum.commerce.warehouse.service;

import ru.practicum.commerce.shoppingstore.dto.AddProductToWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.AssemblyProductsForOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.ShippedToDeliveryRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void addNewProductToWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    BookedProductsDto checkProductQuantityInWarehouse(ShoppingCartDto shoppingCartDto);

    AddressDto getWarehouseAddress();

    void updateProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest);

    void shippedToDelivery(ShippedToDeliveryRequest shippedToDeliveryRequest);

    void returnProductsToWarehouse(Map<UUID, Long> returnProducts);

    void assemblyOrderProducts(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);
}
