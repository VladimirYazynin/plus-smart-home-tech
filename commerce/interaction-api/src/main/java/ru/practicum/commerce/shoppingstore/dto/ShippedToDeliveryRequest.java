package ru.practicum.commerce.shoppingstore.dto;

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
public class ShippedToDeliveryRequest {

    @NotNull(message = "orderId не может быть пустым")
    private UUID orderId;

    @NotNull(message = "deliveryId не может быть пустым")
    private UUID deliveryId;
}
