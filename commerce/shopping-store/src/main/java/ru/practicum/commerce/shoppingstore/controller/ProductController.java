package ru.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.commerce.shoppingstore.dto.PageResponseDto;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.practicum.commerce.shoppingstore.enums.QuantityState;
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
    public PageResponseDto<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        return productService.getProducts(category, pageable);
    }

    @Override
    @GetMapping("/{productId}")
    public ProductDto getProductById(@NotNull @PathVariable UUID productId) {
        return productService.getProductById(productId);
    }

    @Override
    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.debug("Получен запрос на обновление продукта, тело запроса: {}", productDto);
        ProductDto response = productService.updateProduct(productDto);
        log.debug("Данные продукта успешно обновлены: {}",  response);
        return response;
    }

    @Override
    @PutMapping
        public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.debug("Получен запрос на добавление нового товара: {}", productDto);
        ProductDto response = productService.createProduct(productDto);
        log.debug("Товар успешно добавлен: {}", response);
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
    public boolean updateProductQuantityState(@NotNull @RequestParam UUID productId,
                                              @NotNull @RequestParam QuantityState quantityState) {
        log.debug("Получен запрос на обновление статуса количества товара с uuid: {}", productId);
        boolean result = productService.updateQuantityState(productId, quantityState);
        log.debug("Статус товара успешно обновлён на: {}", quantityState);
        return result;
    }
}
