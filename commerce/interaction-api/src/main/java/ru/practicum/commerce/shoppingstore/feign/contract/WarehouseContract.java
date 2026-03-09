package ru.practicum.commerce.shoppingstore.feign.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.commerce.shoppingstore.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseContract {

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PutMapping
    void addNewProductToWarehouse(@Valid @RequestBody NewProductInWarehouseRequest newProduct);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantity(@Valid @RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void addProductsToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest product);

    @PostMapping("/shipped")
    void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequest shippedToDeliveryRequest);

    @PostMapping("/return")
    void returnProductsToWarehouse(@RequestBody @NotEmpty Map<@NotNull UUID, @NotNull @Positive Long> returnProducts);

    @PostMapping("/assembly")
    void assemblyOrderProducts(@Valid @RequestBody AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);
}
