package ru.practicum.commerce.shoppingstore.feign.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.commerce.shoppingstore.dto.PageResponseDto;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.practicum.commerce.shoppingstore.enums.QuantityState;

import java.util.UUID;

public interface StoreContract {

    @GetMapping
    PageResponseDto<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable);

    @GetMapping("/{productId}")
    ProductDto getProductById(@NotNull @PathVariable UUID productId);

    @PostMapping
    ProductDto createProduct(@Valid @RequestBody ProductDto productDto);

    @PutMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean updateProductQuantityState(@NotNull @RequestParam UUID productId,
                                              @NotNull @RequestParam QuantityState quantityState);
}
