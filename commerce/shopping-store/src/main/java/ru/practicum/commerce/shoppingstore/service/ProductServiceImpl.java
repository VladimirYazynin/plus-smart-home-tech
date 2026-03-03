package ru.practicum.commerce.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.commerce.shoppingstore.dto.PageResponseDto;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.entity.ProductEntity;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.practicum.commerce.shoppingstore.enums.ProductState;
import ru.practicum.commerce.shoppingstore.enums.QuantityState;
import ru.practicum.commerce.shoppingstore.exception.NotFoundException;
import ru.practicum.commerce.shoppingstore.mapper.ProductMapper;
import ru.practicum.commerce.shoppingstore.repository.ProductRepository;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository storeRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<ProductEntity> products = storeRepository.findAllByProductCategory(category, pageable);
        Page<ProductDto> productDto = products.map(productMapper::toProductDto);
        return productMapper.toPageResponseDto(productDto);
    }

    @Override
    public ProductDto createProduct(ProductDto newProductDto) {
        ProductEntity product = productMapper.toProductEntity(newProductDto);
        return productMapper.toProductDto(storeRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(ProductDto updateProductDto) {
        ProductEntity product = validateProductExist(updateProductDto.getProductId());
        productMapper.updateProductEntityFromProductDto(updateProductDto, product);
        return productMapper.toProductDto(storeRepository.save(product));
    }

    @Override
    public boolean deleteProduct(UUID productId) {
        ProductEntity product = validateProductExist(productId);
        product.setProductState(ProductState.DEACTIVATE);
        storeRepository.save(product);
        return true;
    }

    @Override
    public boolean updateQuantityState(UUID productId, QuantityState quantityState) {
        if (productId == null) {
            throw new IllegalArgumentException("productId не может быть null");
        }
        if (quantityState == null) {
            throw new IllegalArgumentException("quantityState не может быть null");
        }
        ProductEntity product = validateProductExist(productId);
        product.setQuantityState(quantityState);
        storeRepository.save(product);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        ProductEntity product = validateProductExist(productId);
        return productMapper.toProductDto(product);
    }

    private ProductEntity validateProductExist(UUID productId) {
        return storeRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Товар c uuid = %s не найден", productId)));
    }
}
