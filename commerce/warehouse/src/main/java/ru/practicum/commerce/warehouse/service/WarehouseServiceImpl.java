package ru.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.commerce.shoppingstore.dto.AddProductToWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;
import ru.practicum.commerce.shoppingstore.exception.NoSpecifiedProductInWarehouseException;
import ru.practicum.commerce.shoppingstore.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.practicum.commerce.shoppingstore.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.commerce.warehouse.entity.ProductStorageEntity;
import ru.practicum.commerce.warehouse.mapper.WarehouseMapper;
import ru.practicum.commerce.warehouse.repository.ProductStorageRepository;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductStorageRepository productStorageRepository;
    private final WarehouseMapper warehouseMapper;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest) {
        UUID productId = newProductInWarehouseRequest.getProductId();
        if (productStorageRepository.existsById(productId)) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    String.format("Товар с uuid: %s уже заведен на склад", productId)
            );
        }
        ProductStorageEntity productStorage = warehouseMapper.toProductStorageEntity(newProductInWarehouseRequest);
        productStorageRepository.save(productStorage);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantityInWarehouse(ShoppingCartDto shoppingCartDto) {
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragileItems = false;
        for (Map.Entry<UUID, Long> productEntry : shoppingCartDto.getProducts().entrySet()) {
            UUID productId = productEntry.getKey();
            Long requestedQuantity = productEntry.getValue();
            ProductStorageEntity productStorage = productStorageRepository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            String.format("Товар с uuid: %s не найден на складе", productId)
                    ));
            if (productStorage.getQuantity() < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        String.format("Недостаточно товара на складе. Uuid товара: %s, необходимо: %d, в наличии: %d",
                                productId,
                                requestedQuantity,
                                productStorage.getQuantity())
                );
            }
            totalWeight += productStorage.getWeight() * requestedQuantity;
            totalVolume += productStorage.getWidth() * productStorage.getHeight()
                    * productStorage.getDepth() * requestedQuantity;
            if (productStorage.getFragile()) {
                hasFragileItems = true;
            }
        }
        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragileItems)
                .build();
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    @Override
    @Transactional
    public void updateProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        UUID productUuid = addProductToWarehouseRequest.getProductId();
        ProductStorageEntity productStorage = productStorageRepository.findById(productUuid)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        String.format("Товар с uuid: %s не найден на складе", productUuid)
                ));
        productStorage.setQuantity(productStorage.getQuantity() + addProductToWarehouseRequest.getQuantity());
        productStorageRepository.save(productStorage);
    }
}
