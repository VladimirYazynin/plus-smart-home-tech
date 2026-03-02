package ru.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.practicum.commerce.shoppingstore.feign.contract.StoreContract;
import ru.practicum.commerce.shoppingstore.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ProductController implements StoreContract {

    private final ProductService productService;

    @Override
    @GetMapping
    public Page<ProductDto> getProducts(ProductCategory productCategory, Pageable pageable) {
        return productService.getProducts(productCategory, pageable);
    }

    @Override
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }

    @Override
    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.debug("Получен запрос на добавление нового товара: {}", productDto);
        ProductDto response = productService.createProduct(productDto);
        log.debug("Товар успешно добавлен: {}", response);
        return response;
    }

    @Override
    @PutMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.debug("Получен запрос на обновление продукта, тело запроса: {}", productDto);
        ProductDto response = productService.updateProduct(productDto);
        log.debug("Данные продукта успешно обновлены: {}",  response);
        return response;
    }

    @Override
    @PostMapping("/removeProductFromStore")
    public Boolean removeProductFromStore(@RequestBody UUID productId) {
        log.debug("Получен запрос на удаление товара с uuid: {}", productId);
        productService.deleteProduct(productId);
        log.debug("Удалён товар с uuid: {}", productId);
        return true;
    }

    @Override
    @PostMapping("/quantityState")
    public boolean updateProductQuantityState(@RequestBody ProductDto productDto) {
        log.debug("Получен запрос на обновление статуса количества товара с uuid: {}", productDto.getProductId());
        boolean result = productService.updateQuantityState(productDto.getProductId(), productDto.getQuantityState());
        log.debug("Статус товара успешно обновлён на: {}", productDto.getQuantityState());
        return result;
    }
}
