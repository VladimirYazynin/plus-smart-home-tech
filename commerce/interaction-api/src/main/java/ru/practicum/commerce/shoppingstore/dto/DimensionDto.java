package ru.practicum.commerce.shoppingstore.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {

    @NotNull(message = "Необходимо указать ширину")
    @Min(value = 1, message = "Минимальное значение 1")
    private Double width;

    @NotNull(message = "Необходимо указать высоту")
    @Min(value = 1, message = "Минимальное значение 1")
    private Double height;

    @NotNull(message = "Необходимо указать глубину")
    @Min(value = 1, message = "Минимальное значение 1")
    private Double depth;
}
