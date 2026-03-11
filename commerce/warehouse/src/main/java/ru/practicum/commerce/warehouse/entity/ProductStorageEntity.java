package ru.practicum.commerce.warehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "product_storage")
public class ProductStorageEntity {

    @Id
    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "fragile")
    private Boolean fragile;

    @Column(name = "width", nullable = false)
    private BigDecimal width;

    @Column(name = "height", nullable = false)
    private BigDecimal height;

    @Column(name = "depth", nullable = false)
    private BigDecimal depth;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "quantity")
    private Long quantity = 0L;
}
