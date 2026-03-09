package ru.practicum.commerce.delivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.commerce.shoppingstore.enums.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class DeliveryEntity {

    @Id
    @GeneratedValue
    private UUID deliveryId;

    @NotNull(message = "Адрес отправителя должен быть указан")
    @JoinColumn(name = "from_address_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private AddressEntity fromAddress;

    @NotNull(message = "Адрес получателя обязателен")
    @JoinColumn(name = "to_address_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private AddressEntity toAddress;

    @Column(nullable = false)
    @NotNull(message = "Идентификатор orderId обязателен")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Статус доставки обязателен")
    @Builder.Default
    private DeliveryState deliveryState = DeliveryState.CREATED;
}
