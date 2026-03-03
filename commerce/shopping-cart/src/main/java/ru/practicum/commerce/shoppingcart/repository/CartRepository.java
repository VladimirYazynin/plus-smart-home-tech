package ru.practicum.commerce.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.commerce.shoppingcart.entity.CartEntity;
import ru.practicum.commerce.shoppingstore.enums.CartState;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    Optional<CartEntity> findByUsernameAndStatus(String username, CartState status);

    Optional<CartEntity> findByUsername(String username);
}
