package ru.practicum.commerce.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.commerce.delivery.entity.AddressEntity;
import ru.practicum.commerce.delivery.entity.DeliveryEntity;
import ru.practicum.commerce.shoppingstore.dto.AddressDto;
import ru.practicum.commerce.shoppingstore.dto.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "deliveryId", ignore = true)
    @Mapping(target = "fromAddress", source = "fromAddress", qualifiedByName = "toAddressEntity")
    @Mapping(target = "toAddress", source = "toAddress", qualifiedByName = "toAddressEntity")
    DeliveryEntity toDeliveryEntity(DeliveryDto dto);

    @Mapping(target = "fromAddress", source = "fromAddress", qualifiedByName = "toAddressDto")
    @Mapping(target = "toAddress", source = "toAddress", qualifiedByName = "toAddressDto")
    DeliveryDto toDeliveryDto(DeliveryEntity entity);

    @Named("toAddressEntity")
    @Mapping(target = "addressId", ignore = true)
    AddressEntity toAddressEntity(AddressDto dto);

    @Named("toAddressDto")
    AddressDto toAddressDto(AddressEntity entity);
}
