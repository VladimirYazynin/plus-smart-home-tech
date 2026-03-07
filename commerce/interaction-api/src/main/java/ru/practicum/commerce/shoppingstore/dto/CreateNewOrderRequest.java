package ru.practicum.commerce.shoppingstore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewOrderRequest {

    @Valid
    @NotNull
    private ShoppingCartDto shoppingCartDto;

    @Valid
    @NotNull
    private AddressDto deliveryAddress;
}
