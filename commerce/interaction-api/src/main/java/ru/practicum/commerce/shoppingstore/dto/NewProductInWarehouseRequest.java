package ru.practicum.commerce.shoppingstore.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {

    @NotNull
    private UUID productId;

    private Boolean fragile;

    @NotNull(message = "Необходимо указать размер")
    @JsonProperty("dimension")
    private DimensionDto dimensionDto;

    @NotNull(message = "Необходимо указать вес")
    @Min(value = 1, message = "Минимальное значение 1")
    private Double weight;
}
