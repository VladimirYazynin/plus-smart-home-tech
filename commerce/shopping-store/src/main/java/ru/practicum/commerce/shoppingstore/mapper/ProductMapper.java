package ru.practicum.commerce.shoppingstore.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.entity.ProductEntity;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toProductEntity(ProductDto productDto);

    ProductDto toProductDto(ProductEntity productEntity);

    @Mapping(target = "productId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateProductEntityFromProductDto(ProductDto productDto, @MappingTarget ProductEntity productEntity);
}
