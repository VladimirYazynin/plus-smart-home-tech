package ru.practicum.commerce.shoppingstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    @NotNull(message = "Необходимо указать общий вес доставки")
    private Double deliveryWeight;

    @NotNull(message = "Необходимо указать общие объём доставки")
    private Double deliveryVolume;

    @NotNull(message = "Необходимо указать есть ли хрупкие вещи в доставке")
    private Boolean fragile;
}
