package ru.practicum.commerce.shoppingstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {

    @NotNull
    private UUID shoppingCartId;

    @NotNull(message = "Необходимо указать идентификатор товара и его количество")
    private Map<UUID, Long> products;
}
