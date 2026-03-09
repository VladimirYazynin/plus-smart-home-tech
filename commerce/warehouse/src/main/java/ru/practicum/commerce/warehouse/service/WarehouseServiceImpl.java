package ru.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.commerce.shoppingstore.dto.AddProductToWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.AssemblyProductsForOrderRequest;
import ru.practicum.commerce.shoppingstore.dto.BookedProductsDto;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.shoppingstore.dto.ShippedToDeliveryRequest;
import ru.practicum.commerce.shoppingstore.dto.ShoppingCartDto;
import ru.practicum.commerce.shoppingstore.exception.NoOrderFoundException;
import ru.practicum.commerce.shoppingstore.exception.NoSpecifiedProductInWarehouseException;
import ru.practicum.commerce.shoppingstore.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.practicum.commerce.shoppingstore.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.commerce.warehouse.entity.OrderBookingEntity;
import ru.practicum.commerce.warehouse.entity.ProductStorageEntity;
import ru.practicum.commerce.warehouse.mapper.WarehouseMapper;
import ru.practicum.commerce.warehouse.repository.OrderBookingRepository;
import ru.practicum.commerce.warehouse.repository.ProductStorageRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductStorageRepository productStorageRepository;
    private final OrderBookingRepository orderBookingRepository;
    private final WarehouseMapper warehouseMapper;
    @Value("${scale.weight}")
    private int scaleWeight;
    @Value("${scale.volume}")
    private int scaleVolume;

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
        Map<UUID, Long> products = shoppingCartDto.getProducts();
        if (products == null || products.isEmpty()) {
            return BookedProductsDto.builder()
                    .deliveryWeight(BigDecimal.ZERO)
                    .deliveryVolume(BigDecimal.ZERO)
                    .fragile(false)
                    .build();
        }
        Set<UUID> productIds = products.keySet();
        Map<UUID, ProductStorageEntity> productsStorage = getWarehouseProducts(productIds);
        return calculateBookedProducts(products, productsStorage);
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

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest shippedToDeliveryRequest) {
        OrderBookingEntity orderBooking = getOrderBooking(shippedToDeliveryRequest.getOrderId());
        orderBooking.setDeliveryId(shippedToDeliveryRequest.getDeliveryId());
    }

    @Override
    public void returnProductsToWarehouse(Map<UUID, Long> returnProducts) {
        if (returnProducts.isEmpty()) {
            return;
        }
        Map<UUID, ProductStorageEntity> products = getWarehouseProducts(returnProducts.keySet());
        returnProducts.forEach((productId, quantityToReturn) -> {
            ProductStorageEntity productStorage = products.get(productId);
            if (productStorage == null) {
                throw new NoSpecifiedProductInWarehouseException(String.format(
                        "Товар с uuid: %s не найден на складе", productId));
            }
            productStorage.setQuantity(productStorage.getQuantity() + quantityToReturn);
        });
    }

    @Override
    public void assemblyOrderProducts(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest) {
        Map<UUID, Long> assemblyProducts = assemblyProductsForOrderRequest.getProducts();
        Set<UUID> productIds = assemblyProducts.keySet();
        Map<UUID, ProductStorageEntity> productsStorage = getWarehouseProducts(productIds);
        calculateBookedProducts(assemblyProducts, productsStorage);
        assemblyProducts.forEach((productId, requestedQuantity) -> {
            ProductStorageEntity productStorage = productsStorage.get(productId);
            long newQuantity = productStorage.getQuantity() - requestedQuantity;
            productStorage.setQuantity(newQuantity);
        });
        OrderBookingEntity orderBooking = OrderBookingEntity.builder()
                .orderId(assemblyProductsForOrderRequest.getOrderId())
                .products(assemblyProducts)
                .build();
        orderBookingRepository.save(orderBooking);
    }

    private OrderBookingEntity getOrderBooking(UUID orderId) {
        return orderBookingRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException(
                        String.format("Бронь заказа с uuid: %s не найдена", orderId))
                );
    }

    private Map<UUID, ProductStorageEntity> getWarehouseProducts(Set<UUID> productIds) {
        return productStorageRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(ProductStorageEntity::getProductId, Function.identity()));
    }

    private BookedProductsDto calculateBookedProducts(Map<UUID, Long> products, Map<UUID,
            ProductStorageEntity> productsStorageEntity) {
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;
        boolean hasFragileItems = false;
        for (Map.Entry<UUID, Long> productEntry : products.entrySet()) {
            UUID productId = productEntry.getKey();
            Long requestedQuantity = productEntry.getValue();
            ProductStorageEntity productStorageEntity = productsStorageEntity.get(productId);
            if (productStorageEntity == null) {
                throw new NoSpecifiedProductInWarehouseException(String.format(
                        "Товар с uuid: %s не найден на складе", productId));
            }
            if (productStorageEntity.getQuantity() < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(String.format(
                                "Недостаточно товара на складе. Товар с uuid: %s, запрошено: %d, доступно: %d",
                                productId, requestedQuantity, productStorageEntity.getQuantity())
                );
            }
            BigDecimal productWeight = BigDecimal.valueOf(productStorageEntity.getWeight());
            BigDecimal quantityBD = BigDecimal.valueOf(requestedQuantity);
            BigDecimal currentProductTotalWeight = productWeight.multiply(quantityBD);
            totalWeight = totalWeight.add(currentProductTotalWeight);
            BigDecimal width = productStorageEntity.getWidth();
            BigDecimal height = productStorageEntity.getHeight();
            BigDecimal depth = productStorageEntity.getDepth();
            BigDecimal singleProductVolume = width.multiply(height).multiply(depth);
            BigDecimal currentProductTotalVolume = singleProductVolume.multiply(quantityBD);
            totalVolume = totalVolume.add(currentProductTotalVolume);
            if (Boolean.TRUE.equals(productStorageEntity.getFragile())) {
                hasFragileItems = true;
            }
        }
        totalWeight = totalWeight.setScale(scaleWeight, RoundingMode.HALF_UP);
        totalVolume = totalVolume.setScale(scaleVolume, RoundingMode.HALF_UP);
        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragileItems)
                .build();
    }
}
