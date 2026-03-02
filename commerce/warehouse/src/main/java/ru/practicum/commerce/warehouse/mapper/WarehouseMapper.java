package ru.practicum.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.commerce.shoppingstore.dto.NewProductInWarehouseRequest;
import ru.practicum.commerce.warehouse.entity.ProductStorageEntity;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(target = "quantity", constant = "0L")
    @Mapping(target = "width",  source = "dimensionDto.width")
    @Mapping(target = "height", source = "dimensionDto.height")
    @Mapping(target = "depth",  source = "dimensionDto.depth")
    ProductStorageEntity toProductStorageEntity(NewProductInWarehouseRequest request);
}
