package ru.practicum.commerce.shoppingstore.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import ru.practicum.commerce.shoppingstore.dto.PageResponseDto;
import ru.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.practicum.commerce.shoppingstore.dto.SortOrderDto;
import ru.practicum.commerce.shoppingstore.entity.ProductEntity;

import java.util.List;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toProductEntity(ProductDto productDto);

    ProductDto toProductDto(ProductEntity productEntity);

    @Mapping(target = "sort", expression = "java(toSortOrders(page.getSort()))")
    PageResponseDto<ProductDto> toPageResponseDto(Page<ProductDto> page);

    default List<SortOrderDto> toSortOrders(Sort sort) {
        if (sort == null) return List.of();
        return sort.stream()
                .map(o -> new SortOrderDto(o.getDirection().name(), o.getProperty()))
                .toList();
    }

    @Mapping(target = "productId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateProductEntityFromProductDto(ProductDto productDto, @MappingTarget ProductEntity productEntity);
}
