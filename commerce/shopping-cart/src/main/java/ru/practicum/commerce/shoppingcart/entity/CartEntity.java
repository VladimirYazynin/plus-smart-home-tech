package ru.practicum.commerce.shoppingcart.entity;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.commerce.shoppingstore.enums.CartState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "cart")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CartEntity {

    @Id
    @UuidGenerator
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @ElementCollection
    @Column(name = "quantity")
    @MapKeyColumn(name = "product_id")
    @CollectionTable(name = "cart_product", joinColumns = @JoinColumn(name = "cart_id"))
    private Map<UUID, Long> products = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CartState status = CartState.ACTIVE;

    @Column(name = "username", nullable = false, unique = true)
    String username;
}
