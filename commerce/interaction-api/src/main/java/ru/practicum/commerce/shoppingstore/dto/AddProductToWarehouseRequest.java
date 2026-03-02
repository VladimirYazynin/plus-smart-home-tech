package ru.practicum.commerce.shoppingstore.dto;

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
public class AddProductToWarehouseRequest {

    @NotNull
    private UUID productId;

    @Min(value = 1, message = "Минимальное количество 1")
    @NotNull(message = "Количество товара должно быть указано")
    private Long quantity;
}
