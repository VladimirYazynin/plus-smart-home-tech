package ru.practicum.commerce.shoppingstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    @NotNull(message = "Необходимо указать общий вес доставки")
    private BigDecimal deliveryWeight;

    @NotNull(message = "Необходимо указать общие объём доставки")
    private BigDecimal deliveryVolume;

    @NotNull(message = "Необходимо указать есть ли хрупкие вещи в доставке")
    private Boolean fragile;
}
