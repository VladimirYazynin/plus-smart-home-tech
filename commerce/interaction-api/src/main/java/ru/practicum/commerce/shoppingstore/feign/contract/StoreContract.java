package ru.practicum.commerce.shoppingstore.feign.contract;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;

import java.util.UUID;

public interface StoreContract {

    @GetMapping
    Page<ProductDto> getProducts(ProductCategory productCategory, Pageable pageable);

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);

    @PostMapping
    ProductDto createProduct(@Valid @RequestBody ProductDto productDto);

    @PutMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean updateProductQuantityState(@RequestBody ProductDto productDto);
}
