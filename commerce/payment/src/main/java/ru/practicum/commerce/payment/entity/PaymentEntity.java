package ru.practicum.commerce.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.commerce.shoppingstore.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@Builder
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue
    private UUID paymentId;

    private UUID orderId;

    @Builder.Default
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState = PaymentState.PENDING;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPayment;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryTotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal feeTotal;
}
