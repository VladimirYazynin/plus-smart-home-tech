package ru.practicum.commerce.shoppingstore.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.commerce.shoppingstore.dto.PageResponseDto;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.practicum.commerce.shoppingstore.enums.QuantityState;

import java.util.UUID;

public interface ProductService {

    PageResponseDto<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto newProductDto);

    ProductDto updateProduct(ProductDto updateProductDto);

    boolean deleteProduct(UUID productId);

    boolean updateQuantityState(UUID productId, QuantityState quantityState);

    ProductDto getProductById(UUID productId);
}
